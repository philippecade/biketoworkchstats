package com.github.philippecade.biketoworkstats;

import java.util.ArrayList;
import java.util.List;

/**
 * Bike to Work team
 *
 * @author  xphc
 * @since May 4, 2016
 */
class Team extends AbstractParticipant {
	
	private final List<Member> members;

	Team(String name) {
		setName(name);
		this.members = new ArrayList<>();
	}
	
	void addMember(Member member) {
		this.members.add(member);
	}

	List<Member> getMembers() {
		return this.members;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Team [getName()=");
		builder.append(getName());
		builder.append(", getMembers()=");
		builder.append(getMembers());
		builder.append(", getKm()=");
		builder.append(getKm());
		builder.append(", getByBike()=");
		builder.append(getByBike());
		builder.append("]");
		return builder.toString();
	}

}