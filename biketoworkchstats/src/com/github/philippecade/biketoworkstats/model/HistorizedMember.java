package com.github.philippecade.biketoworkstats.model;

/**
 * A member with history.
 * @author XPHC
 */
public class HistorizedMember extends AbstractHistorizedParticipant {
	
	private String email;
	private String teamName;
	
	public HistorizedMember(String memberName, String email) {
		super(memberName);
		this.email = email;
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
		StringBuilder builder = new StringBuilder();
		builder.append("HistorizedMember [getName()=");
		builder.append(getName());
		builder.append(", email=");
		builder.append(this.email);
		builder.append(", getData()=");
		builder.append(getData());
		builder.append("]");
		return builder.toString();
	}

}
