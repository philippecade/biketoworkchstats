package com.github.philippecade.biketoworkstats;

/**
 * Base implementation for a team or member.
 *
 * @author  xphc
 * @since May 4, 2016
 */
class AbstractParticipant implements IParticipant {

	private String name;
	private int km;
	private double byBike;

	void setName(String name) {
		this.name = name.trim();
	}

	void setKm(int km) {
		this.km = km;
	}

	void setByBike(double byBike) {
		this.byBike = byBike;
	}

	@Override
	public int getKm() {
		return this.km;
	}

	@Override
	public double getByBike() {
		return this.byBike;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Compares by bike descending and then by name
	 * @param p1
	 * @param p2
	 * @return
	 */
	static int compareByBike(IParticipant p1, IParticipant p2) {
		int cmp = Double.valueOf(p2.getByBike()).compareTo(p1.getByBike());
		if (cmp != 0) {
			return cmp;
		}
		return p1.getName().compareTo(p2.getName());
	}
	
	/**
	 * Compares by kilometer descending and then by name
	 * @param p1
	 * @param p2
	 * @return
	 */
	static int compareKm(IParticipant p1, IParticipant p2) {
		int cmp = Integer.valueOf(p2.getKm()).compareTo(p1.getKm());
		if (cmp != 0) {
			return cmp;
		}
		return p1.getName().compareTo(p2.getName());
	}
	
}