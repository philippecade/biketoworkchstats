package com.github.philippecade.biketoworkstats.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import com.github.philippecade.biketoworkstats.model.DataPoint;

/**
 * Generates a CSV report
 * @author XPHC
 */
public class CsvReportGenerator implements IReportGenerator {

	private static final String SEPARATOR = "--------";

	private BufferedWriter writer;

	private File outputFile;

	public CsvReportGenerator(File inputFile) throws IOException {
		String inputName = inputFile.getName();
		String outputName = inputName.replace(".csv", " formatted.csv");
		this.outputFile = new File(inputFile.getParent(), outputName);
		this.writer = Files.newBufferedWriter(this.outputFile.toPath());
	}

	@Override
	public void addTable(List<String> columnHeaders, List<DataPoint<Double>> dataPoints) throws IOException {
		this.writer.write(String.join(",", columnHeaders));
		this.writer.newLine();
		this.writer.write(SEPARATOR);
		this.writer.newLine();
		for (DataPoint<Double> dataPoint: dataPoints) {
			this.writer.write("\""+dataPoint.getName()+"\","+dataPoint.getValue());
			this.writer.newLine();
		}
	}

	@Override
	public List<File> getOutputFiles() {
		return Arrays.asList(this.outputFile);
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
