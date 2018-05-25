package com.github.philippecade.biketoworkstats;

import java.util.List;

/**
 * A participant with historical data
 * @author XPHC
 */
interface IHistorizedParticipant {
	
	String getName();
	
	List<HistoricalData> getData();

}
