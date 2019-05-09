package com.github.philippecade.biketoworkstats.model;

import java.util.Date;

/**
 * Historical data with a time stamp.
 * @author XPHC
 */
public class HistoricalData {
	
	private Date timestamp;
	private ParticipantData data;

	HistoricalData(Date timestamp, double km, double bikeDays) {
		this.timestamp = timestamp;
		this.data = new ParticipantData(km, bikeDays);
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public double getKm() {
		return this.data.getKm();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HistoricalData [timestamp=");
		builder.append(this.timestamp);
		builder.append(", data=");
		builder.append(this.data);
		builder.append("]");
		return builder.toString();
	}

}
