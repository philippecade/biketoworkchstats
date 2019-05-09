package com.github.philippecade.biketoworkstats.model;

import java.util.List;

/**
 * A participant with historical data
 * @author XPHC
 */
public interface IHistorizedParticipant {
	
	String getName();
	
	List<HistoricalData> getData();

}
