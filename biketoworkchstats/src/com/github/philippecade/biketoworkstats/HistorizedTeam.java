package com.github.philippecade.biketoworkstats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A team with history
 * @author XPHC
 */
class HistorizedTeam extends AbstractHistorizedParticipant {
	
	private List<HistorizedMember> members;
	
	HistorizedTeam(String teamName) {
		super(teamName);
		this.members = new ArrayList<>();
	}

	Optional<HistorizedMember> getMember(String memberEmail) {
		return this.members.stream().filter(m -> m.getEmail().equals(memberEmail)).findFirst();
	}

	void addMember(HistorizedMember historizedMember) {
		this.members.add(historizedMember);
	}
	
	List<HistorizedMember> getMembers() {
		return this.members;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HistorizedTeam [getName()=");
		builder.append(getName());
		builder.append(", members=");
		builder.append(this.members);
		builder.append(", getData()=");
		builder.append(getData());
		builder.append("]");
		return builder.toString();
	}

}
