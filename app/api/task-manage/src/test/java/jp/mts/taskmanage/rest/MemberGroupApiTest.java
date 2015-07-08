package jp.mts.taskmanage.rest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.taskmanage.application.GroupAppService;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.rest.presentation.model.GroupList;
import jp.mts.taskmanage.rest.presentation.model.GroupList.GroupView;
import jp.mts.taskmanage.rest.presentation.model.GroupSave;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MemberGroupApiTest {

	MemberGroupApi target;
	@Mocked GroupAppService groupAppService;
	
	@Before
	public void setup(){
		target = new MemberGroupApi();
		Deencapsulation.setField(target, groupAppService);
	}

	@Test
	public void test_register() {
		new Expectations() {{
			groupAppService.register("member01", "group01", "this is a test group");
				result = new GroupFixture().get();
		}};
		
		GroupSave group = new GroupSave();
		group.setName("group01");
		group.setDescription("this is a test group");
		RestResponse<GroupSave> response = target.createGroupOnMember("member01", group);
		
		assertThat(response.getData().getGroupId(), is("g01"));
	}

	@Test
	public void test_list_belonging_groups() {
		new Expectations() {{
			groupAppService.listBelonging("m01");
				result = newArrayList(
						new GroupFixture("g01").get(), 
						new GroupFixture("g02").get());
		}};

		RestResponse<GroupList> response = target.listBelongingGroups("m01");

		List<GroupView> groups = response.getData().getGroups();
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).getGroupId(), is("g01"));
		assertThat(groups.get(1).getGroupId(), is("g02"));
	}
}
