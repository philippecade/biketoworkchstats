package com.github.philippecade.biketoworkstats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Base implementation for a team or member with history
 * @author XPHC
 */
abstract class AbstractHistorizedParticipant implements IHistorizedParticipant {
	
	private String name;
	private List<HistoricalData> data;

	public AbstractHistorizedParticipant(String name) {
		this.name = name;
		this.data = new ArrayList<>();
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<HistoricalData> getData() {
		return this.data;
	}

	void addKmAndBikeDays(Date timestamp, double km, double bikeDays) {
		this.data.add(new HistoricalData(timestamp, km, bikeDays));
	}

}
