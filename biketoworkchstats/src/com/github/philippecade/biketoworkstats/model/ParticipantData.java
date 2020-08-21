package com.github.philippecade.biketoworkstats.model;

/**
 * Kilometer and number of days per bicycle for a participant
 * @author XPHC
 */
class ParticipantData {
	private double km;
	private int nBikeDays;
	private int nNonBikeDays;

	ParticipantData() {
		// empty
	}

	ParticipantData(double km, int nBikeDays, int nNonBikeDays) {
		this.km = km;
		this.nBikeDays = nBikeDays;
		this.nNonBikeDays = nNonBikeDays;
	}

	double getKm() {
		return this.km;
	}

	void setKm(double km) {
		this.km = km;
	}
	
	int getBikeDays() {
		return this.nBikeDays;
	}
	
	int getNonBikeDays() {
		return this.nNonBikeDays;
	}
	
	void setBikeDays(int nBikeDays, int nNonBikeDays) {
		this.nBikeDays = nBikeDays;
		this.nNonBikeDays = nNonBikeDays;
	}

	double getByBike() {
		if (this.nBikeDays == 0) {
			return 0;
		}
		return 100d * this.nBikeDays / (this.nBikeDays + this.nNonBikeDays);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParticipantData [km=");
		builder.append(this.km);
		builder.append(", byBike=");
		builder.append(getByBike());
		builder.append("]");
		return builder.toString();
	}

}