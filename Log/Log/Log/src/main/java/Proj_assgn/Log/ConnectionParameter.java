package Proj_assgn.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//import java.util.logging.Logger;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionParameter {

	// The config parameters for the connection
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";

	private static RestHighLevelClient restHighLevelClient;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static final String INDEX = "logdata2";
	private static final String TYPE = "log";

	/**
	 * Implemented Singleton pattern here so that there is just one connection at a
	 * time.
	 * 
	 * @return RestHighLevelClient Elastic search client
	 */
	private static synchronized RestHighLevelClient makeConnection() {

		if (restHighLevelClient == null) {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(new HttpHost(HOST, PORT_ONE, SCHEME), new HttpHost(HOST, PORT_TWO, SCHEME)));
		}

		return restHighLevelClient;
	}

	// Closing the client connection
	private static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}

	/**
	 * Creating a Map for data Creating a index request for new index in
	 * elasticsearch
	 * @throws JsonProcessingException
	 */
	private static MyLogClass insertLog(MyLogClass log) throws JsonProcessingException {
		log.setLogId(UUID.randomUUID().toString());
		System.out.println(log.getLogId());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("logId", log.getLogId());
		dataMap.put("date", log.getDate());
		dataMap.put("timeStamp", log.getTimeStamp());
		dataMap.put("loglevel", log.getloglevel());
		dataMap.put("className", log.getClassName());
		dataMap.put("lineNumber", log.getLineNumber());
		dataMap.put("message", log.getMessage());

		System.out.println(dataMap);

		/**
		 * Creating json format data for sending it to elasticsearch
		 * Code for converting map to json
				 
				JSONArray jsonList = new JSONArray();

				
				JSONObject obj = new JSONObject();
				for (Entry<String, Object> entry : dataMap.entrySet()) {
					obj.put(entry.getKey(), entry.getValue());
				}
				jsonList.put(obj);
				System.out.println(obj);
				System.out.println(jsonList.toString(2));
				*/
				
		/**
		* Using Jackson to converting data to elasticformat
				
				byte[] stringifiedJson = objectMapper.writeValueAsBytes(obj);

				System.out.println(stringifiedJson);
		*/
			
				//Creating the elastic index request

		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, log.getLogId())
	            .source(dataMap, XContentType.JSON);
	    try {
	        IndexResponse response = restHighLevelClient.index(indexRequest);
	    } catch(ElasticsearchException e) {
	        e.getDetailedMessage();
	    } catch (java.io.IOException ex){
	        ex.getLocalizedMessage();
	    }
	    return log;
	}

	/**
	 * Getting the resposne from elsactisearch by ID
	 */
	private static String getLogById(String id){
	    GetRequest getLogRequest = new GetRequest(INDEX, TYPE, id);
	    GetResponse getResponse = null;
	    try {
	        getResponse = restHighLevelClient.get(getLogRequest);
	    } catch (java.io.IOException e){
	        e.getLocalizedMessage();
	    }
	    //return getResponse != null ? objectMapper.convertValue(getResponse.getSourceAsMap(), MyLogClass.class) : null; 
		return getResponse.getSourceAsString();
	}

	
	
	public static void main(String[] args) throws IOException {
		 
	    makeConnection();
		try {
			
		//Log4j 2 config file setting as ealstic was throwing error without it 		
		
		System.setProperty("log4j.configurationFile","D:\\Shivani\\VS Code Projects\\Shivani\\Log\\Log\\src\\main\\log4j2.properties");
		
		
		
	   
	    
	    File file = new File("D:\\Shivani\\VS Code Projects\\Shivani\\sample.log.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		while ((st = br.readLine()) != null) {
			String[] data_1 = st.split(" - ");
			String[] data_2 = data_1[0].split("\\s+");
			String[] data_3 = data_2[3].split(":");
			
			MyLogClass log = new MyLogClass();
			log.setDate(data_2[0]);
			log.setTimeStamp(data_2[1]);
			log.setloglevel(data_2[2]);
			log.setLineNumber(data_3[1]);
			log.setClassName(data_3[0]);
			log.setMessage(data_1[1]);
			
			System.out.println("Inserting Data...");
			log = insertLog(log);

			System.out.println("Getting Data...");
			String LogFromDB = getLogById((log.getLogId()));

			System.out.println(LogFromDB);
			
			

		    
		}
		}catch(ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex){
			ex.getLocalizedMessage();
			}catch (Exception e) {
				System.out.println(e.getMessage());
		}finally {
			//Statements to be executed
			closeConnection();
		}

	 
	   
	}
}
