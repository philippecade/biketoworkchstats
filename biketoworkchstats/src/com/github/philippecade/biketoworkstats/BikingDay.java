package com.github.philippecade.biketoworkstats;

/**
 * Description of a bike to work day
 * @author xphc
 */
public class BikingDay {
	
	private int numRiders;
	private double km;

	void addRider() {
		this.numRiders++;
	}

	void addDistance(double kmPerDay) {
		this.km += kmPerDay;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BikingDay [numRiders=");
		builder.append(this.numRiders);
		builder.append(", km=");
		builder.append(this.km);
		builder.append("]");
		return builder.toString();
	}

	int getNumRiders() {
		return this.numRiders;
	}

	double getKm() {
		return this.km;
	}

}
