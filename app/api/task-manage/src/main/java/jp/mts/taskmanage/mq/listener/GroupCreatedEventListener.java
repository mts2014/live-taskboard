package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.taskmanage.application.GroupAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventListenerConfig(targetEventTypes={"jp.mts.taskmanage.domain.model.GroupCreated"})
public class GroupCreatedEventListener extends EventListener {

	@Autowired
	private GroupAppService groupAppService;
	
	@Override
	protected void doProccess(long eventId, Date occurred, EventBody eventBody) {
		groupAppService.entryMember(
				eventBody.asString("groupId.value"), 
				eventBody.asString("creator.value"));
		groupAppService.changeGroupAvailable(
				eventBody.asString("groupId.value"));
	}
}
