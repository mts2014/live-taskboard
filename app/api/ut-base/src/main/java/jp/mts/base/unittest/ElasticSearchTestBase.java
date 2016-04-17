package jp.mts.base.unittest;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.net.InetAddress;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHits;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("integration-test")
@ContextConfiguration(classes=ElasticSearchTestBase.TestConfig.class)
public abstract class ElasticSearchTestBase {
	
	@Autowired TransportClient transportClient;
	
	protected TransportClient transportClient() {
		return transportClient;
	}
	
	protected void await(long seconds) {
		try {
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Configuration
	@Profile("integration-test")
	public static class TestConfig {
		@Bean
		public TransportClient transportClient() throws Exception {
			return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(
						InetAddress.getByName("192.168.77.10"), 9300));
		}
		@Bean
		public IndicesAdminClient indicesAdminClient() throws Exception {
			return transportClient().admin().indices();
		}
	}

	public static class ESClean extends ExternalResource {
		private String index;
		private ElasticSearchTestBase testTarget;
		
		public ESClean(String index, ElasticSearchTestBase testTarget) {
			this.index = index;
			this.testTarget = testTarget;
		}

		@Override
		protected void before() throws Throwable {
			TransportClient transportClient = testTarget.transportClient;
			BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
			SearchHits hits = transportClient.prepareSearch(index)
				.setQuery(matchAllQuery())
				.get()
				.getHits();


			hits.forEach(hit -> {
				DeleteRequest deleteRequest = new DeleteRequest(hit.getIndex(), hit.getType(), hit.getId());
				if(hit.field("_parent") != null){
					deleteRequest.parent((String)hit.field("_parent").getValue());
				}
				bulkRequestBuilder.add(deleteRequest);
			});
			if(bulkRequestBuilder.numberOfActions() <= 0) return;
			bulkRequestBuilder.get();
			
			testTarget.await(1);
		}

		@Override
		protected void after() {
		}
	}
}