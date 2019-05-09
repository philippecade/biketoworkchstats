package com.github.philippecade.biketoworkstats.model;

/**
 * Kilometer and number of days per bicycle for a participant
 * @author XPHC
 */
class ParticipantData {
	private double km;
	private double byBike;

	ParticipantData() {
		// empty
	}

	ParticipantData(double km, double bikeDays) {
		this.km = km;
		this.byBike = bikeDays;
	}

	double getKm() {
		return this.km;
	}

	void setKm(double km) {
		this.km = km;
	}

	double getByBike() {
		return this.byBike;
	}

	void setByBike(double byBike) {
		this.byBike = byBike;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParticipantData [km=");
		builder.append(this.km);
		builder.append(", byBike=");
		builder.append(this.byBike);
		builder.append("]");
		return builder.toString();
	}

}