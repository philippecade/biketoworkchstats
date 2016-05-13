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

	private static final int TEAM_NAME_COLUMN = 9;
	private static final int TEAM_KM = 33;
	private static final int MEMBER_1_NAME = 12;
	private static final int MEMBER_1_KM = 38;
	private static final int MEMBER_2_NAME = 21;
	private static final int MEMBER_2_KM = 42;
	private static final int MEMBER_3_NAME = 25;
	private static final int MEMBER_3_KM = 46;
	private static final int MEMBER_4_NAME = 29;
	private static final int MEMBER_4_KM = 50;
	
	/**
	 * Reads the status file and returns the data model
	 * @param file
	 * @return
	 * @throws IOException
	 */
	List<Team> readStatusFile(File file) throws IOException {
		List<Team> teams = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-16"))) {
			// skip the first header line
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split("\t");
				if (columns.length < TEAM_NAME_COLUMN) {
					continue;
				}

				Team team = new Team(columns[TEAM_NAME_COLUMN]);
				team.setKm(Integer.parseInt(columns[TEAM_KM]));
				team.setByBike(Double.parseDouble(columns[TEAM_KM+1])/100);

				team.addMember(getMember(columns, MEMBER_1_NAME, MEMBER_1_KM));
				team.addMember(getMember(columns, MEMBER_2_NAME, MEMBER_2_KM));
				team.addMember(getMember(columns, MEMBER_3_NAME, MEMBER_3_KM));
				team.addMember(getMember(columns, MEMBER_4_NAME, MEMBER_4_KM));
				
				teams.add(team);
			}
		}
		return teams;
	}
	
	/**
	 * Returns a member
	 * @param columns
	 * @param nameIndex
	 * @param kmIndex
	 * @return
	 */
	Member getMember(String[] columns, int nameIndex, int kmIndex) {
		Member member = new Member();
		member.setName(columns[nameIndex]+" "+columns[nameIndex+1]);
		member.setKm(Integer.parseInt(columns[kmIndex]));
		member.setByBike(Double.parseDouble(columns[kmIndex+1])/100);
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
			out.println("--------");
			out.println("Team Name,Average By Bike");
			dumpReport(out, generateReport(teams, AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBikeFormatted));
			out.println("--------");
			out.println("Member Name,Average By Bike");
			dumpReport(out, generateReport(getAllMembers(teams), AbstractParticipant::compareByBike, 
					AbstractParticipant::getByBikeFormatted));
			out.println("--------");
			out.println("Team Name,Total Kilometers");
			dumpReport(out, generateReport(teams, AbstractParticipant::compareKm, 
					AbstractParticipant::getKmFormatted));
			out.println("--------");
			out.println("Member Name,Total Kilometers");
			dumpReport(out, generateReport(getAllMembers(teams), AbstractParticipant::compareKm, 
					AbstractParticipant::getKmFormatted));
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
		String outputName = inputName.replace(".xls", " formatted.csv");
		return new File(inputFile.getParent(), outputName);
	}
	
	/**
	 * Writes the data to the given stream
	 * @param out
	 * @param data
	 */
	private void dumpReport(PrintStream out, List<Object[]> data) {
		data.stream().forEach(row -> {
			out.println(row[0]+","+row[1]);
		});
	}

	/**
	 * Generates a report
	 * @param participants
	 * @param comparator
	 * @param valueFunction
	 * @return table data
	 */
	private List<Object[]> generateReport(List<? extends IParticipant> participants, 
			Comparator<IParticipant> comparator,
			Function<IParticipant, String> valueFunction) {
		List<Object[]> data = new ArrayList<Object[]>();
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
			map(team -> team.getMembers()).
			flatMap(List::stream).
			collect(Collectors.toList());
	}

}