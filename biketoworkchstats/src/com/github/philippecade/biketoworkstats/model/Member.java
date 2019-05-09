package com.github.philippecade.biketoworkstats.model;

/*****************************************************************************
 * Bike To Work member
 *
 * @author  xphc
 * @since May 4, 2016
 ****************************************************************************/
public class Member extends AbstractParticipant {
	
	private String email;
	private double kmPerDay;

	public Member() {
		// empty
	}

	public void setEmail(String memberEmail) {
		this.email = memberEmail;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setKmPerDay(double kmPerDay) {
		this.kmPerDay = kmPerDay;
	}
	
	public double getKmPerDay() {
		return this.kmPerDay;
	}

	/**
	 * Compares by kilometer per descending and then by name
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static int compareKmPerDay(Member m1, Member m2) {
		int cmp = Double.valueOf(m2.getKmPerDay()).compareTo(m1.getKmPerDay());
		if (cmp != 0) {
			return cmp;
		}
		return m1.getName().compareTo(m2.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Member [getName()=");
		builder.append(getName());
		builder.append(", getEmail()=");
		builder.append(this.email);
		builder.append(", kmPerDay=");
		builder.append(this.kmPerDay);
		builder.append("]");
		return builder.toString();
	}
	
}
