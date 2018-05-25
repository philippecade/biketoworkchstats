package com.github.philippecade.biketoworkstats;

/**
 * A member with history.
 * @author XPHC
 */
class HistorizedMember extends AbstractHistorizedParticipant {
	
	private String email;
	
	HistorizedMember(String memberName, String email) {
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
