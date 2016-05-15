package com.github.philippecade.biketoworkstats;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/*****************************************************************************
 * Bike To Work member
 *
 * @author  xphc
 * @since May 4, 2016
 ****************************************************************************/
class Member extends AbstractParticipant implements IParticipant {
	
	private static final NumberFormat BY_KM_PER_DAY_FORMAT = new DecimalFormat("#0.0");

	private int memberId;
	private double kmPerDay;

	Member() {
		// empty
	}

	void setId(int memberId) {
		this.memberId = memberId;
	}
	
	int getId() {
		return this.memberId;
	}
	
	void setKmPerDay(double kmPerDay) {
		this.kmPerDay = kmPerDay;
	}
	
	double getKmPerDay() {
		return this.kmPerDay;
	}

	public static String getKmPerDayFormatted(Member m) {
		return BY_KM_PER_DAY_FORMAT.format(m.getKmPerDay());
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
	
}
