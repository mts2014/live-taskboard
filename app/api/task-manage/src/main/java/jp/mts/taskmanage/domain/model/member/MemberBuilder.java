package jp.mts.taskmanage.domain.model.member;

import java.util.Collection;

public class MemberBuilder {

	private Member member;

	public MemberBuilder(Member member) {
		this.member = member;
	}

	public MemberBuilder setEmail(String email) {
		member.setEmail(email);
		return this;
	}
	
	public MemberBuilder addGroupBelonging(GroupBelonging groupBelonging) {
		member.addGroupBelonging(groupBelonging);
		return this;
	}
	public MemberBuilder addGroupBelongings(Collection<GroupBelonging> groupBelongings) {
		member.addGroupBelongings(groupBelongings);
		return this;
	}
	
	public Member get() {
		return member;
	}

	
}
