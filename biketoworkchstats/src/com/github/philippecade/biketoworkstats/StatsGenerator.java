package com.github.philippecade.biketoworkstats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Extracts data from the bike to work status file
 *
 * @author  xphc
 * @since May 4, 2016
 */
public class StatsGenerator {

	private static final String SEPARATOR = "--------";
	private static final int TEAM_NAME_COLUMN = 5;
	private static final int TEAM_KM = 49;
	private static final int TEAM_PERCENT_BY_BIKE = 46;
	private static final int MEMBER_1_EMAIL = 9;
	private static final int MEMBER_1_KM = 30;
	private static final int MEMBER_2_EMAIL = 19;
	private static final int MEMBER_2_KM = 34;
	private static final int MEMBER_3_EMAIL = 23;
	private static final int MEMBER_3_KM = 38;
	private static final int MEMBER_4_EMAIL = 27;
	private static final int MEMBER_4_KM = 42;
	
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
				String[] columns = line.split(";");
				if (columns.length < TEAM_NAME_COLUMN) {
					continue;
				}

				Team team = new Team(columns[TEAM_NAME_COLUMN]);
				team.setKm(Integer.parseInt(columns[TEAM_KM]));
				team.setByBike(Double.parseDouble(columns[TEAM_PERCENT_BY_BIKE]));

				addNonEmptyMember(team, getMember(columns, MEMBER_1_EMAIL, MEMBER_1_KM));
				addNonEmptyMember(team, getMember(columns, MEMBER_2_EMAIL, MEMBER_2_KM));
				addNonEmptyMember(team, getMember(columns, MEMBER_3_EMAIL, MEMBER_3_KM));
				addNonEmptyMember(team, getMember(columns, MEMBER_4_EMAIL, MEMBER_4_KM));
				
				teams.add(team);
			}
		}
		return teams;
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
	 * @param kmIndex
	 * @return
	 */
	Member getMember(String[] columns, int emailIndex, int kmIndex) {
		Member member = new Member();
		member.setEmail(columns[emailIndex]);
		member.setName(columns[emailIndex-2]+" "+columns[emailIndex-1]);
		String km = columns[kmIndex];
		if (!km.isEmpty()) {
			member.setKm(Integer.parseInt(km));
		}
		String byBike = columns[kmIndex+3];
		if (!byBike.isEmpty()) {
			member.setByBike(Double.parseDouble(byBike));
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
		File outputFile = getOutputFile(inputFile);
		try (PrintStream out = new PrintStream(outputFile)) {
			List<Team> teams = readStatusFile(inputFile);
			out.println(SEPARATOR);
			out.println("Team Name,Average By Bike");
			dumpReport(out, generateReport(teams, AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBikeFormatted));
			out.println(SEPARATOR);
			out.println("Member Name,Average By Bike");
			dumpReport(out, generateReport(getAllMembers(teams), AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBikeFormatted));
			out.println(SEPARATOR);
			out.println("Team Name,Total Kilometers");
			dumpReport(out, generateReport(teams, AbstractParticipant::compareKm, 
					AbstractParticipant::getKmFormatted));
			out.println(SEPARATOR);
			out.println("Member Name,Total Kilometers");
			dumpReport(out, generateReport(getAllMembers(teams), AbstractParticipant::compareKm, 
					AbstractParticipant::getKmFormatted));
			out.println(SEPARATOR);
			out.println("Member Name, Km per Day");
			dumpReport(out, generateReport(getAllMembers(teams), Member::compareKmPerDay, 
					Member::getKmPerDayFormatted));
			return outputFile;
		}
	}
	
	/**
	 * Generates a name for the output file from the input file
	 * @param inputFile
	 * @return
	 */
	private File getOutputFile(File inputFile) {
		String inputName = inputFile.getName();
		String outputName = inputName.replace(".csv", " formatted.csv");
		return new File(inputFile.getParent(), outputName);
	}
	
	/**
	 * Writes the data to the given stream
	 * @param out
	 * @param data
	 */
	private void dumpReport(PrintStream out, List<Object[]> data) {
		data.stream().forEach(row -> out.println(row[0]+","+row[1]));
	}

	/**
	 * Generates a report
	 * @param participants
	 * @param comparator
	 * @param valueFunction
	 * @return table data
	 */
	private <T extends IParticipant> List<Object[]> generateReport(List<T> participants, 
			Comparator<T> comparator,
			Function<T, String> valueFunction) {
		List<Object[]> data = new ArrayList<>();
		participants.stream().
			sorted(comparator).
			forEach(participant -> data.add(new Object[] { participant.getName(), valueFunction.apply(participant) }));
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