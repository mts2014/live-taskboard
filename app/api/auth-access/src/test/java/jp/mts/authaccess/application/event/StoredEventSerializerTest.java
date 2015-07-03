package jp.mts.authaccess.application.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.authaccess.domain.model.DomainEvent;

import org.junit.Test;


public class StoredEventSerializerTest {

	@Test
	public void test() {
		StoredEventSerializer target = new StoredEventSerializer();
		
		StoredEvent serialized = target.serialize(new TestEvent());
		EventBody deserializeBody = target.deserializeBody(serialized);
		
		assertThat(deserializeBody.asString("testId.value"), is("id value"));
	}
	
	
	public static class TestEvent extends DomainEvent{
		private Id testId = new Id("id value");
	}
	
	public static class Id {
		private String value;

		public Id(String value) {
			this.value = value;
		}
	}

}
