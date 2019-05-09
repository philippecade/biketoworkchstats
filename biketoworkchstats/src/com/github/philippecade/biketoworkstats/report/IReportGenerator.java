package com.github.philippecade.biketoworkstats.report;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.philippecade.biketoworkstats.model.DataPoint;

/**
 * Generates a report from the statistics
 * @author XPHC
 */
public interface IReportGenerator extends Closeable {
	
	void addTable(List<String> columnHeaders, List<DataPoint<Double>> dataPoints) throws IOException;
	
	List<File> getOutputFiles();

}
