package luo.lai.fa.esearch1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jna.Function;

import sun.security.provider.DSAKeyFactory;



public class EsDemo {
	
	TransportClient client;
	
	@Before
	public void Before() throws UnknownHostException
	{
		client = new PreBuiltTransportClient(Settings.EMPTY)
		        .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.103"), 9300));
	}
	
	@After
	public void After()
	{
		client.close();
	}

	//@Test
	public void get() throws UnknownHostException
	{
		GetResponse response = client.prepareGet("user_db", "user", "cWmq7mYBUcmN6jCyTSNP").get();
		String result = response.getSourceAsString();
		System.out.println(result);
		
	}
	
	
	//@Test
	public void add() throws IOException
	{
		IndexResponse response = client.prepareIndex("user_db", "user", "1000")
								        .setSource(jsonBuilder()
								                    .startObject()
								                        .field("name", "kimchy")
								                        .field("bdate", new Date())
								                        .field("age", "32")
								                        .field("info", "this is doubi!")
								                    .endObject()
								                  )
								        .get();
		// Index name
		String _index = response.getIndex();
		System.out.println(_index);
		// Type name
		String _type = response.getType();
		System.out.println(_type);
		// Document ID (generated or not)
		String _id = response.getId();
		System.out.println(_id);
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		System.out.println(_version);
		// status has stored current instance statement.
		RestStatus status = response.status();
		System.out.println(status);
	}
	
	//@Test
	public void delete()
	{
		DeleteResponse response = client.prepareDelete("user_db", "user", "100").get();
		System.out.println(response.status());
		

	}
	
	
	//@Test
	public void DeleteByQueryAction()
	{
		
		BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
			    .filter(QueryBuilders.matchQuery("user", "kimchy")) 
			    .source("twitter")                                  
			    .get();                                             
		long deleted = response.getDeleted(); 
		System.out.println(deleted);
	}
	
	//@Test
	public void update() throws IOException, InterruptedException, ExecutionException
	{
		UpdateRequest updateRequest = new UpdateRequest("user_db", "user", "cWmq7mYBUcmN6jCyTSNP")
		        .doc(jsonBuilder()
		            .startObject()
		                .field("name", "luolaifa")
		            .endObject());
		UpdateResponse update = client.update(updateRequest).get();
		System.out.println(update.status());
	}
	
	//@Test
	public void multget()
	{
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
			    .add("user_db", "user", "cWmq7mYBUcmN6jCyTSNP")            
			    .add("user_db", "user", "emmr7mYBUcmN6jCyQSNt")     
			    .get();

			for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
			    GetResponse response = itemResponse.getResponse();
			    if (response.isExists()) {                      
			        String json = response.getSourceAsString(); 
			        System.out.println(json);
			    }
			}
			
	}
	
	//@Test
	public void bulk() throws IOException
	{
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		// either use client#prepare, or use Requests# to directly build index/delete requests
		bulkRequest.add(client.prepareIndex("user_db", "user", "1")
		        .setSource(jsonBuilder()
		                    .startObject()
		                    .field("name", "kimchy1")
	                        .field("bdate", new Date())
	                        .field("age", "32")
	                        .field("info", "this is doubi!")
		                    .endObject()
		                  )
		        );

		bulkRequest.add(client.prepareIndex("user_db", "user", "2")
		        .setSource(jsonBuilder()
		                    .startObject()
		                    .field("name", "kimchy2")
	                        .field("bdate", new Date())
	                        .field("age", "32")
	                        .field("info", "this is doubi!")
		                    .endObject()
		                  )
		        );

		BulkResponse bulkResponse = bulkRequest.get();
		System.out.println(bulkResponse.status());
		if (bulkResponse.hasFailures()) {
		    // process failures by iterating through each bulk response item
		}
	}
	
	//@Test
	public void updateQuery()
	{
		UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
		updateByQuery.source("user_db")
		    .filter(QueryBuilders.termQuery("name", "kimchy2"))
		    .size(1000)
		    .script(new Script("ctx._source.age = '22'"));
		BulkByScrollResponse response = updateByQuery.get();
		System.out.println(response.getStatus());
	}
	
	//@Test
	public void multsearch()
	{
		SearchRequestBuilder srb1 = client
			    .prepareSearch().setQuery(QueryBuilders.queryStringQuery("gIAQjBXggsWcTFINTsmdMuoobVjbzEeJ")).setSize(1);
			SearchRequestBuilder srb2 = client
			    .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "MeizHPSiEl")).setSize(1);

			MultiSearchResponse sr = client.prepareMultiSearch()
			        .add(srb1)
			        .add(srb2)
			        .get();

			// You will get all individual responses from MultiSearchResponse#getResponses()
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse response = item.getResponse();
			    System.out.println(response.toString());
			    nbHits += response.getHits().getTotalHits();
			}
			System.out.println(nbHits);
	}
	
	//@Test
	public void aggsearch()
	{
		SearchResponse sr = client.prepareSearch()
			    .setQuery(QueryBuilders.matchAllQuery())
			    .addAggregation(
			            AggregationBuilders.terms("agg1").field("age")
			    )
			    .addAggregation(
			            AggregationBuilders.dateHistogram("agg2")
			                    .field("bdate")
			                    .dateHistogramInterval(DateHistogramInterval.YEAR)
			    )
			    .get();

			// Get your facet results
			Terms agg1 = sr.getAggregations().get("agg1");
			Histogram agg2 = sr.getAggregations().get("agg2");
			System.out.println(agg1.toString());
			System.out.println(agg2.toString());
	}
	
	//@Test
	public void aggmin()
	{
		MinAggregationBuilder aggregation =
		        AggregationBuilders
		                .min("agg")
		                .field("age");
		
		SearchResponse response = client.prepareSearch().addAggregation(aggregation).get();
		
		Min agg = response.getAggregations().get("agg");
		int value = (int) agg.getValue();
		System.out.println(value);
		
	}
	
	//@Test
	public void aggmax()
	{
		MaxAggregationBuilder aggregation =
		        AggregationBuilders
		                .max("agg")
		                .field("age");
		
		SearchResponse response = client.prepareSearch().addAggregation(aggregation).get();
		System.out.println(response);
		Max agg = response.getAggregations().get("agg");
		int value = (int) agg.getValue();
		System.out.println(value);
		
	}
	
	//@Test
	public void aggsum()
	{
		SumAggregationBuilder aggregation =
		        AggregationBuilders
		                .sum("agg")
		                .field("age");
		SearchResponse response = client.prepareSearch().addAggregation(aggregation).get();
		Sum agg = response.getAggregations().get("agg");
		double value = agg.getValue();
		System.out.println(value);
	}
	
	//@Test
	public void ttt()
	{
		SearchResponse sr = client.prepareSearch()
			    .setQuery(QueryBuilders.wildcardQuery(
			            "name",                                              
			            "*k*"))
			    .setQuery(QueryBuilders.rangeQuery("age")                                          
			    .from(5)                                                 
			    .to(10000)                                                  
			    .includeLower(true)                                      
			    .includeUpper(false))
			    .get();
		System.out.println(QueryBuilders.rangeQuery("age")                                          
			    .from(5)                                                 
			    .to(10)                                                  
			    .includeLower(true)                                      
			    .includeUpper(false).toString());
		
		System.out.println(sr);
	}
	
	//@Test
    public void timeAgg(){
        String[] includes = {"name", "info","bdate","age"};
        SearchResponse response = client.prepareSearch("user_db").setTypes("user")
                .addAggregation(AggregationBuilders.dateHistogram("timeAgg").field("bdate")
                        .dateHistogramInterval(DateHistogramInterval.DAY) //这里是按照day分组，也可以按照month和year
                .subAggregation(AggregationBuilders.topHits("top")
                        .fetchSource(includes,Strings.EMPTY_ARRAY).size(3)))
                .get();

        Histogram timeAgg = response.getAggregations().get("timeAgg");
        for(Histogram.Bucket entry:timeAgg.getBuckets()){
            System.out.println(entry.getKey()+"-----"+entry.getDocCount());
            TopHits topHits = entry.getAggregations().get("top");
            for (SearchHit hit : topHits.getHits()) {
                Map<String, Object> map = hit.getSourceAsMap();
                System.out.println(map);
            }
        }
    }
	
	
	@Test
    public void ageAgg(){
    	
        String[] includes = {"name", "info","bdate","age"};
        SearchResponse response = client.prepareSearch("user_db").setTypes("user")
                .addAggregation(AggregationBuilders.terms("ageAgg").field("age")
                .subAggregation(AggregationBuilders.topHits("top")
                        .fetchSource(includes,Strings.EMPTY_ARRAY).size(3)))
                .get();
        
        SearchHits hits = response.getHits();

        Terms timeAgg = response.getAggregations().get("ageAgg");
        for (Terms.Bucket entry : timeAgg.getBuckets()) {
			String key = entry.getKeyAsString(); // bucket key
			long docCount = entry.getDocCount(); // Doc count
			System.out.println("key " + key + " doc_count " + docCount);

			TopHits topHits = entry.getAggregations().get("top");
			for (SearchHit hit : topHits.getHits().getHits()) {
				System.out.println(" -> id " + hit.getId() + " _source [{}]"
						+ hit.getSourceAsString());
				;
			}
		}
		System.out.println(hits.getTotalHits());
    }
}
