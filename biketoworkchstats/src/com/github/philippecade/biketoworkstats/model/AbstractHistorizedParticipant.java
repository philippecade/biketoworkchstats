package com.github.philippecade.biketoworkstats.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

	public void addKmAndBikeDays(Date timestamp, double km, int nBikeDays, int nNonBikeDays) {
		HistoricalData dta = getOrCreateDataForTimeStamp(timestamp);
		dta.setKm(dta.getKm() + km);
		dta.setBikeDays(nBikeDays, nNonBikeDays);
	}

	private HistoricalData getOrCreateDataForTimeStamp(Date timestamp) {
		Optional<HistoricalData> optional = this.data.stream().filter(dta -> dta.getTimestamp().equals(timestamp)).findFirst();
		if (optional.isPresent()) {
			return optional.get();
		}

		HistoricalData dta = new HistoricalData(timestamp, 0, 0, 0);
		this.data.add(dta);
		return dta;
	}

}
