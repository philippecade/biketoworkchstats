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
		this.members = new ArrayList<Member>();
	}
	
	void addMember(Member member) {
		this.members.add(member);
	}

	List<Member> getMembers() {
		return this.members;
	}

}