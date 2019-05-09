package com.github.philippecade.biketoworkstats.model;

/**
 * Base implementation for a team or member.
 *
 * @author  xphc
 * @since May 4, 2016
 */
public class AbstractParticipant implements IParticipant {

	private String name;
	private ParticipantData data = new ParticipantData();

	public void setName(String name) {
		this.name = name.trim();
	}

	public void setKm(double km) {
		this.data.setKm(km);
	}

	public void setByBike(double byBike) {
		this.data.setByBike(byBike);
	}

	@Override
	public double getKm() {
		return this.data.getKm();
	}

	@Override
	public double getByBike() {
		return this.data.getByBike();
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
	public static int compareByBike(IParticipant p1, IParticipant p2) {
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
	public static int compareKm(IParticipant p1, IParticipant p2) {
		int cmp = Double.valueOf(p2.getKm()).compareTo(p1.getKm());
		if (cmp != 0) {
			return cmp;
		}
		return p1.getName().compareTo(p2.getName());
	}
	
}