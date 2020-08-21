package com.github.philippecade.biketoworkstats.model;

/*****************************************************************************
 * Bike To Work member
 *
 * @author  xphc
 * @since May 4, 2016
 ****************************************************************************/
public class Member extends AbstractParticipant {
	
	private String email;
	private String teamName;

	public Member() {
		// empty
	}

	public void setEmail(String memberEmail) {
		this.email = memberEmail;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	@Override
	public String toString() {
		return "Member [email=" + this.email + ", teamName=" + this.teamName + "]";
	}

}
