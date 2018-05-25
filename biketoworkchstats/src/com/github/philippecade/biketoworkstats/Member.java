package com.github.philippecade.biketoworkstats;

/*****************************************************************************
 * Bike To Work member
 *
 * @author  xphc
 * @since May 4, 2016
 ****************************************************************************/
class Member extends AbstractParticipant {
	
	private String email;
	private double kmPerDay;

	Member() {
		// empty
	}

	void setEmail(String memberEmail) {
		this.email = memberEmail;
	}
	
	String getEmail() {
		return this.email;
	}
	
	void setKmPerDay(double kmPerDay) {
		this.kmPerDay = kmPerDay;
	}
	
	double getKmPerDay() {
		return this.kmPerDay;
	}

	/**
	 * Compares by kilometer per descending and then by name
	 * @param m1
	 * @param m2
	 * @return
	 */
	static int compareKmPerDay(Member m1, Member m2) {
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
