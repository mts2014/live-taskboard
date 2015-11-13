package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingFixture;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;

import org.junit.Test;

import com.google.common.collect.Lists;

public class JdbcGroupRepositoryTest extends JdbcTestBase {
	
	JdbcGroupRepository groupRepository = new JdbcGroupRepository();
	JdbcGroupBelongingRepository groupBelongingRepository = new JdbcGroupBelongingRepository();
	
	@Test
	public void test_UUID表示() {
		JdbcGroupRepository target = new JdbcGroupRepository();
		System.out.println(
			target.newGroupId().value()
		);
	}

	@Test
	public void test_persistence() {
		Group group = new GroupFixture().get();
		groupRepository.save(group);
		
		Group found = groupRepository.findById(group.groupId()).get();
		assertThat(group, is(found));
		
		found.changeAttributes("group_changed_name", "group changed");
		groupRepository.save(found);

		found = groupRepository.findById(group.groupId()).get();
		assertThat(found.name(), is("group_changed_name"));
	}
	
	@Test
	public void test_find_by_ids() {
		Group group2 = new GroupFixture("g02", "m01").get();
		groupRepository.save(group2);
		Group group1 = new GroupFixture("g01", "m01").get();
		groupRepository.save(group1);
		
		List<Group> found = groupRepository.findByIds(Lists.newArrayList(new GroupId("g02"),new GroupId("g01")));
		
		assertThat(found.size(), is(2));
		assertThat(found.get(0), is(group1));
		assertThat(found.get(1), is(group2));
	}
	
	@Test
	public void test_remove() {
		
		Group group = new GroupFixture("g01").get();
		groupRepository.save(group);
		GroupBelonging groupBelonging = new GroupBelongingFixture("g01", "m01").get();
		groupBelongingRepository.save(groupBelonging);
		
		groupRepository.remove(group);
		
		Optional<Group> foundGroup = groupRepository.findById(new GroupId("g01"));
		assertThat(foundGroup.isPresent(), is(false));
		
		GroupBelonging foundGroupBelonging = groupBelongingRepository.findById(new MemberId("m01"), new GroupId("g01"));
		assertThat(foundGroupBelonging, is(nullValue()));
	}
}
