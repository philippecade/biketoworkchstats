package com.github.philippecade.biketoworkstats;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
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
			StatsReader statsReader = new StatsReader();
			List<Team> teams = statsReader.readStatusFile(inputFile);

			out.println("Team Name,Average By Bike");
			List<DataPoint<Double>> teamPerBike = generateDataPoints(teams, AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBike);
			dumpReport(out, teamPerBike);
			images.add(new ChartsGenerator().generateBarChartImage("Average By Bike", "%", teamPerBike));

			out.println("Member Name,Average By Bike");
			List<DataPoint<Double>> memberPerBike = generateDataPoints(getAllMembers(teams), AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBike);
			dumpReport(out, memberPerBike);
			images.add(new ChartsGenerator().generateBarChartImage("Average By Bike", "%", memberPerBike));

			out.println("Team Name,Total Kilometers");
			List<DataPoint<Double>> teamKm = generateDataPoints(teams, AbstractParticipant::compareKm,
					AbstractParticipant::getKm);
			dumpReport(out, teamKm);
			images.add(new ChartsGenerator().generateBarChartImage("Total Kilometers", "km", teamKm));

			out.println("Member Name,Total Kilometers");
			List<DataPoint<Double>> memberTotalKm = generateDataPoints(getAllMembers(teams), AbstractParticipant::compareKm, 
					AbstractParticipant::getKm);
			dumpReport(out, memberTotalKm);
			images.add(new ChartsGenerator().generateBarChartImage("Total Kilometers", "km", memberTotalKm));

			out.println("Member Name, Km per Day");
			List<DataPoint<Double>> memberKmPerDay = generateDataPoints(getAllMembers(teams), Member::compareKmPerDay, 
					Member::getKmPerDay);
			dumpReport(out, memberKmPerDay);
			images.add(new ChartsGenerator().generateBarChartImage("Km Per Day", "km", memberKmPerDay));
			
			BufferedImage combinedImage = new ChartsGenerator().combineImages(images);
			ImageIO.write(combinedImage, "PNG", getGraphOutputFile(inputFile));

			return reportOutputFile;
		}
	}
	
	/**
	 * Generates line graphs showing the history for teams and members
	 * @param files
	 * @throws IOException
	 */
	public void generateHistoricalReports(File...files) throws IOException {
		StatsReader statsReader = new StatsReader();
		List<HistorizedTeam> historizedTeams = statsReader.readStatusFiles(files);
		// one chart for teams
		List<DataPoint<List<Double>>> teamData = generateHistoricalTeamDataPoints(historizedTeams);
		BufferedImage teamImage = new ChartsGenerator().generateLinesChartImage("Teams Progression", "km", teamData);
		ImageIO.write(teamImage, "PNG", new File(files[0].getParent(), "teams history.png"));
		// one chart for people
		List<DataPoint<List<Double>>> memberData = generateHistoricalMembersDataPoints(historizedTeams);
		BufferedImage peopleImage = new ChartsGenerator().generateLinesChartImage("People Progression", "km", memberData);
		ImageIO.write(peopleImage, "PNG", new File(files[0].getParent(), "people history.png"));
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
	
	private List<DataPoint<List<Double>>> generateHistoricalTeamDataPoints(List<HistorizedTeam> teams) {
		List<DataPoint<List<Double>>> data = new ArrayList<>();
		for (HistorizedTeam team: teams) {
			List<HistoricalData> historicalData = team.getData();
			Collections.sort(historicalData, (d1, d2) -> d1.getTimestamp().compareTo(d2.getTimestamp()));
			List<Double> values = historicalData.stream().map(HistoricalData::getKm).collect(Collectors.toList());
			data.add(new DataPoint<>(team.getName(), values));
		}
		return data;
	}
	
	private List<DataPoint<List<Double>>> generateHistoricalMembersDataPoints(List<HistorizedTeam> teams) {
		List<DataPoint<List<Double>>> data = new ArrayList<>();
		for (HistorizedTeam team: teams) {
			for (HistorizedMember member: team.getMembers()) {
				List<HistoricalData> historicalData = member.getData();
				Collections.sort(historicalData, (d1, d2) -> d1.getTimestamp().compareTo(d2.getTimestamp()));
				List<Double> values = historicalData.stream().map(HistoricalData::getKm).collect(Collectors.toList());
				data.add(new DataPoint<>(member.getName(), values));
			}
		}
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