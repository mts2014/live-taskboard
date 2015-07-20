package jp.mts.authaccess.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.authaccess.domain.model.User;
import jp.mts.authaccess.domain.model.UserFixture;
import jp.mts.base.unittest.JdbcTestBase;

import org.junit.Before;
import org.junit.Test;

public class JdbcUserRepositoryTest extends JdbcTestBase {

	JdbcUserRepository target;

	@Before
	public void setup(){
		target = new JdbcUserRepository();
	}
	
	@Test
	public void test_persistence() {

		User user = new UserFixture().get();
		target.save(user);

		assertThat(target.findById(user.id()), is(user));
	}
	
	@Test
	public void test_findByAuthCredential(){

		User user = new UserFixture().get();
		target.save(user);

		assertThat(target.findByAuthCredential(user.id(), user.encryptedPassword()), is(user));
	}
}