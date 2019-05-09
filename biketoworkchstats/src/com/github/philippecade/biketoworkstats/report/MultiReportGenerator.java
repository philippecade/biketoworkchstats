package com.github.philippecade.biketoworkstats.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.philippecade.biketoworkstats.model.DataPoint;

public class MultiReportGenerator implements IReportGenerator {
	
	private List<IReportGenerator> reportGenerators = new ArrayList<>();

	public MultiReportGenerator(IReportGenerator... reportGenerators) {
		for (IReportGenerator reportGenerator: reportGenerators) {
			this.reportGenerators.add(reportGenerator);
		}
	}
	

	@Override
	public void addTable(List<String> columnHeaders, List<DataPoint<Double>> dataPoints) throws IOException {
		for (IReportGenerator reportGenerator: this.reportGenerators) {
			reportGenerator.addTable(columnHeaders, dataPoints);
		}
	}

	@Override
	public List<File> getOutputFiles() {
		List<File> result = new ArrayList<>();
		for (IReportGenerator reportGenerator : this.reportGenerators) {
			result.addAll(reportGenerator.getOutputFiles());
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		for (IReportGenerator reportGenerator: this.reportGenerators) {
			reportGenerator.close();
		}
	}

}
