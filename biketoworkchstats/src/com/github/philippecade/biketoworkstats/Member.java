package com.github.philippecade.biketoworkstats;

/*****************************************************************************
 * Bike To Work member
 *
 * @author  xphc
 * @since May 4, 2016
 ****************************************************************************/
class Member extends AbstractParticipant implements IParticipant {
	
	private int memberId;

	Member() {
		// empty
	}

	void setId(int memberId) {
		this.memberId = memberId;
	}
	
	int getId() {
		return this.memberId;
	}

}
