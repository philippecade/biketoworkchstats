package com.github.philippecade.biketoworkstats.model;

/**
 * A member with history.
 * @author XPHC
 */
public class HistorizedMember extends AbstractHistorizedParticipant {
	
	private String email;
	
	public HistorizedMember(String memberName, String email) {
		super(memberName);
		this.email = email;
	}
	
	String getEmail() {
		return this.email;
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
