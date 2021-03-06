package jp.mts.taskmanage.domain.model.group.join;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupJoinApplicated")
public class GroupJoinApplicated extends DomainEvent {

	private GroupJoin groupJoinApplication;

	public GroupJoinApplicated(GroupJoin groupJoinApplication) {
		this.groupJoinApplication = groupJoinApplication;
	}
	public String getGroupId() {
		return groupJoinApplication.groupId().value();
	}
	public String getApplicantId() {
		return groupJoinApplication.applicationMemberId().value();
	}

}
