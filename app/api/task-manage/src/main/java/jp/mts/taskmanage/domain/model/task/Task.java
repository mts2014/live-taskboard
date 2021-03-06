package jp.mts.taskmanage.domain.model.task;

import java.util.Date;

import jp.mts.base.domain.model.DomainEntity;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberId;

public class Task extends DomainEntity<TaskId>{

	private GroupId groupId;
	private TaskStatus taskStatus;
	private String name;
	private Date deadline;
	private MemberId assignedMemberId;
	private String memo;
	
	public Task(
			GroupId groupId, 
			TaskId taskId, 
			String name, 
			MemberId assignedMemberId, 
			Date deadline) {
		super(taskId);
		this.groupId = groupId;
		this.taskStatus = TaskStatus.TODO; 
		this.name = name;
		this.assignedMemberId = assignedMemberId;
		this.deadline = deadline;
	}

	public GroupId groupId(){
		return groupId;
	}
	public TaskId taskId(){
		return id();
	}
	public TaskStatus status() {
		return taskStatus;
	}
	public String name() {
		return name;
	}
	public Date deadline() {
		return deadline;
	}
	public MemberId assignedMemberId() {
		return assignedMemberId;
	}
	public String memo() {
		return memo;
	}
	
	public void changeSummary(
			String taskName, 
			Member assignedMember, 
			Date deadline,
			TaskStatus status,
			Member modifier) {
		
		setName(taskName);
		setAssignedMemberId(assignedMember.memberId());
		setDeadline(deadline);
		setStatus(status);
		
		domainEventPublisher.publish(new TaskModified(this, modifier));
	}

	public void changeDetail(
			String memo,
			Member modifier) {

		setMemo(memo);
		domainEventPublisher.publish(new TaskModified(this, modifier));
	}
	
	void setStatus(TaskStatus status) {
		this.taskStatus = status;
	}
	void setName(String name) {
		this.name = name;
	}
	void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	void setAssignedMemberId(MemberId assignedMemberId) {
		this.assignedMemberId = assignedMemberId;
	}
	void setMemo(String memo) {
		this.memo = memo;
	}

}
