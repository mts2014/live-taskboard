package jp.mts.taskmanage.rest.presentation.model;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.Group;

public class GroupSave {

	//input
	private String name;
	private String description;

	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//output
	private Group group;

	public String getGroupId(){
		if(group == null) throw new IllegalStateException();
		return group.groupId().value();
	}

	//exec
	public void create(String memberId, GroupAppService groupAppService){
		this.group = groupAppService.register(memberId, name, description);
	}
	
}
