package com.github.philippecade.biketoworkstats.model;

import java.util.Date;

/**
 * Historical data with a time stamp.
 * @author XPHC
 */
public class HistoricalData {
	
	private Date timestamp;
	private ParticipantData data;

	HistoricalData(Date timestamp, double km, int nBikeDays, int nNonBikeDays) {
		this.timestamp = timestamp;
		this.data = new ParticipantData(km, nBikeDays, nNonBikeDays);
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public double getKm() {
		return this.data.getKm();
	}

	public void setKm(double km) {
		this.data.setKm(km);
	}
	
	public int getBikeDays() {
		return this.data.getBikeDays();
	}
	
	public int getNonBikeDays() {
		return this.data.getNonBikeDays();
	}
	
	public void setBikeDays(int nBikeDays, int nNonBikeDays) {
		this.data.setBikeDays(nBikeDays, nNonBikeDays);
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
