package com.github.philippecade.biketoworkstats;

/**
 * A Bike To Work participant.
 *
 * @author  xphc
 * @since May 4, 2016
 */
interface IParticipant {

	/**
	 * @return The participant's name
	 */
	String getName();

	/**
	 * @return Total number of kilometers ridden
	 */
	double getKm();

	/**
	 * @return Days per bike in percent (0..1)
	 */
	double getByBike();

}