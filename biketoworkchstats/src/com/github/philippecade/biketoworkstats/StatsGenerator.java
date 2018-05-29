package com.github.philippecade.biketoworkstats;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 * Extracts data from the bike to work status file
 *
 * @author  xphc
 * @since May 4, 2016
 */
public class StatsGenerator {

	private static final String SEPARATOR = "--------";
	private static final int TEAM_NAME_COLUMN = 5;
	private static final int TEAM_KM = 52;
	private static final int TEAM_PERCENT_BY_BIKE = 49;
	private static final int MEMBER_1_EMAIL = 18;
	private static final int MEMBER_1_TOTAL_KM = 33;
	private static final int MEMBER_1_PERCENT_BY_BIKE = 36;
	private static final int MEMBER_1_KM_PER_DAY = 19;
	private static final int MEMBER_2_EMAIL = 22;
	private static final int MEMBER_2_TOTAL_KM = 37;
	private static final int MEMBER_2_PERCENT_BY_BIKE = 40;
	private static final int MEMBER_2_KM_PER_DAY = 23;
	private static final int MEMBER_3_EMAIL = 26;
	private static final int MEMBER_3_TOTAL_KM = 41;
	private static final int MEMBER_3_PERCENT_BY_BIKE = 44;
	private static final int MEMBER_3_KM_PER_DAY = 27;
	private static final int MEMBER_4_EMAIL = 30;
	private static final int MEMBER_4_TOTAL_KM = 45;
	private static final int MEMBER_4_PERCENT_BY_BIKE = 48;
	private static final int MEMBER_4_KM_PER_DAY = 31;
	
	/**
	 * Reads the status file and returns the data model
	 * @param file
	 * @return
	 * @throws IOException
	 */
	List<Team> readStatusFile(File file) throws IOException {
		List<Team> teams = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			// skip the first header line
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] columns = split(line);
				if (columns.length < TEAM_NAME_COLUMN) {
					continue;
				}

				Team team = new Team(columns[TEAM_NAME_COLUMN]);
				team.setKm(Double.parseDouble(columns[TEAM_KM]));
				team.setByBike(Double.parseDouble(columns[TEAM_PERCENT_BY_BIKE]));

				addNonEmptyMember(team, getMember(columns, MEMBER_1_EMAIL, MEMBER_1_TOTAL_KM, MEMBER_1_PERCENT_BY_BIKE, MEMBER_1_KM_PER_DAY));
				addNonEmptyMember(team, getMember(columns, MEMBER_2_EMAIL, MEMBER_2_TOTAL_KM, MEMBER_2_PERCENT_BY_BIKE, MEMBER_2_KM_PER_DAY));
				addNonEmptyMember(team, getMember(columns, MEMBER_3_EMAIL, MEMBER_3_TOTAL_KM, MEMBER_3_PERCENT_BY_BIKE, MEMBER_3_KM_PER_DAY));
				addNonEmptyMember(team, getMember(columns, MEMBER_4_EMAIL, MEMBER_4_TOTAL_KM, MEMBER_4_PERCENT_BY_BIKE, MEMBER_4_KM_PER_DAY));
				
				teams.add(team);
			}
		}
		return teams;
	}
	
	private String[] split(String line) {
		return Arrays.stream(line.split(";")).map(this::stripQuotes).toArray(String[]::new);
	}
	
	private String stripQuotes(String s) {
		String result = s;
		if (result.startsWith("\"")) {
			result = result.substring(1);
		}
		if (result.endsWith("\"")) {
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
	private void addNonEmptyMember(Team team, Member member) {
		if (!member.getEmail().isEmpty()) {
			team.addMember(member);
		}
	}
	
	/**
	 * Returns a member
	 * @param columns
	 * @param emailIndex
	 * @param totalKmIndex
	 * @param daysPerBike
	 * @return
	 */
	Member getMember(String[] columns, int emailIndex, int totalKmIndex, int daysPerBike, int kmPerDay) {
		Member member = new Member();
		member.setEmail(columns[emailIndex]);
		member.setName(columns[emailIndex-2]+" "+columns[emailIndex-1]);
		String km = columns[totalKmIndex];
		if (!km.isEmpty()) {
			member.setKm(Double.parseDouble(km));
		}
		String byBike = columns[daysPerBike];
		if (!byBike.isEmpty()) {
			member.setByBike(Double.parseDouble(byBike));
		}
		
		String kmPerDayValue = columns[kmPerDay];
		if (!kmPerDayValue.isEmpty()) {
			member.setKmPerDay(Double.parseDouble(kmPerDayValue));
		}
		return member;
	}
	
	/**
	 * Generates the reports for a given week number
	 * @param inputFile
	 * @return Result file
	 * @throws IOException 
	 */
	public File generateReports(File inputFile) throws IOException {
		File reportOutputFile = getReportOutputFile(inputFile);
		List<BufferedImage> images = new ArrayList<>();
		try (PrintStream out = new PrintStream(reportOutputFile)) {
			List<Team> teams = readStatusFile(inputFile);

			out.println("Team Name,Average By Bike");
			List<DataPoint<Double>> teamPerBike = generateDataPoints(teams, AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBike);
			dumpReport(out, teamPerBike);
			images.add(new ChartsGenerator().generateChartImage("Average By Bike", "%", teamPerBike));

			out.println("Member Name,Average By Bike");
			List<DataPoint<Double>> memberPerBike = generateDataPoints(getAllMembers(teams), AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBike);
			dumpReport(out, memberPerBike);
			images.add(new ChartsGenerator().generateChartImage("Average By Bike", "%", memberPerBike));

			out.println("Team Name,Total Kilometers");
			List<DataPoint<Double>> teamKm = generateDataPoints(teams, AbstractParticipant::compareKm,
					AbstractParticipant::getKm);
			dumpReport(out, teamKm);
			images.add(new ChartsGenerator().generateChartImage("Total Kilometers", "km", teamKm));

			out.println("Member Name,Total Kilometers");
			List<DataPoint<Double>> memberTotalKm = generateDataPoints(getAllMembers(teams), AbstractParticipant::compareKm, 
					AbstractParticipant::getKm);
			dumpReport(out, memberTotalKm);
			images.add(new ChartsGenerator().generateChartImage("Total Kilometers", "km", memberTotalKm));

			out.println("Member Name, Km per Day");
			List<DataPoint<Double>> memberKmPerDay = generateDataPoints(getAllMembers(teams), Member::compareKmPerDay, 
					Member::getKmPerDay);
			dumpReport(out, memberKmPerDay);
			images.add(new ChartsGenerator().generateChartImage("Km Per Day", "km", memberKmPerDay));
			
			BufferedImage combinedImage = new ChartsGenerator().combineImages(images);
			ImageIO.write(combinedImage, "PNG", getGraphOutputFile(inputFile));

			return reportOutputFile;
		}
	}
	
	/**
	 * Generates a name for the output file from the input file
	 * @param inputFile
	 * @return
	 */
	private File getReportOutputFile(File inputFile) {
		String inputName = inputFile.getName();
		String outputName = inputName.replace(".csv", " formatted.csv");
		return new File(inputFile.getParent(), outputName);
	}
	
	/**
	 * Generates a name for the graphs that are generated
	 * @param inputFile
	 * @return
	 */
	private File getGraphOutputFile(File inputFile) {
		String inputName = inputFile.getName();
		String outputName = inputName.replace(".csv", " graphs.png");
		return new File(inputFile.getParent(), outputName);
	}
	
	/**
	 * Writes the data to the given stream
	 * @param out
	 * @param data
	 */
	private <T> void dumpReport(PrintStream out, List<DataPoint<T>> data) {
		out.println(SEPARATOR);
		data.stream().forEach(row -> out.println("\""+row.getName()+"\","+row.getValue()));
	}

	/**
	 * Generates a report
	 * @param participants
	 * @param comparator
	 * @param valueFormatter
	 * @return table data
	 */
	private <P extends IParticipant, T> List<DataPoint<T>> generateDataPoints(List<P> participants, 
			Comparator<P> comparator,
			Function<P, T> valueFormatter) {
		List<DataPoint<T>> data = new ArrayList<>();
		participants.stream().
			sorted(comparator).
			forEach(participant -> data.add(new DataPoint<>(participant.getName(), valueFormatter.apply(participant))));
		return data;
	}
	
	/**
	 * Returns members of all teams
	 * @param teams
	 * @return
	 */
	private List<Member> getAllMembers(List<Team> teams) {
		return teams.stream().
			map(Team::getMembers).
			flatMap(List::stream).
			collect(Collectors.toList());
	}

}