package tests;

import Utils.JSONPathFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import static Utils.JSONValueReplacer.generateData;

public class UsersTest {
    public static ObjectMapper mapper = new ObjectMapper();
    public static CloseableHttpClient httpClient;
    public static ResourceBundle resourceBundle;
    public static ResponseHandler<String> responseHandler;


    @BeforeTest
    public void setUp() {
        httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .build())
                .build();
        resourceBundle = ResourceBundle.getBundle("config");
    }

    @Test
    public void testGet() throws IOException {
        HttpGet request = new HttpGet(resourceBundle.getString("baseurl") + "/users?page=2");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        //a. Status Code:
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status Code--->" + statusCode);

        Assert.assertEquals(statusCode, 200, "Status code is not 200");

        //b. Json String:
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONObject responseJson = new JSONObject(responseString);
        //System.out.println("Response JSON from API---> " + responseJson);

        //single value assertion:
        //per_page:
        int perPageValue = (Integer) JSONPathFinder.getValueByJPath(responseString, "$.per_page");
        System.out.println("value of per page is-->" + perPageValue);
        Assert.assertEquals(perPageValue, 6);

        //total:
        int totalValue = (Integer) JSONPathFinder.getValueByJPath(responseString, "$.total");
        System.out.println("value of total is-->" + totalValue);
        Assert.assertEquals(totalValue, 12);

        //get the value from JSON ARRAY:
        String lastName = (String) JSONPathFinder.getValueByJPath(responseString, "$.data[0].last_name");
        int id = (Integer) JSONPathFinder.getValueByJPath(responseString, "$.data[0].id");
        String avatar = (String) JSONPathFinder.getValueByJPath(responseString, "$.data[0].avatar");
        String firstName = (String) JSONPathFinder.getValueByJPath(responseString, "$.data[0].first_name");

        System.out.println(id);
        System.out.println(avatar);
        System.out.println(firstName);
        System.out.println(lastName);
        //c. All Headers
        Header[] headersArray = response.getAllHeaders();
        HashMap<String, String> allHeaders = new HashMap<String, String>();
        for (Header header : headersArray) {
            allHeaders.put(header.getName(), header.getValue());
        }
        System.out.println("Headers Array-->" + allHeaders);
    }

    @Test
    public void test_GETCallResponse() throws IOException {
        HttpGet request = new HttpGet(resourceBundle.getString("baseurl") + "/users?page=2");
        request.setHeader("Content-Type", "application/json; charset=UTF-8");
        request.setHeader("Accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            responseHandler = new BasicResponseHandler();
            String body = responseHandler.handleResponse(response);
            Assert.assertNotNull(body);
        } else {
            System.out.println("response code : " + response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void test_POSTCallResponse() throws IOException {
        HttpPost httpPost = new HttpPost(resourceBundle.getString("baseurl") + "/users");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        String payload = generateData();
        HttpEntity postEntity = new StringEntity(payload, "UTF-8");
        httpPost.setEntity(postEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 201) {
            String body = responseHandler.handleResponse(response);
            Assert.assertNotNull(body);
        } else {
            System.out.println("response code : " + response.getStatusLine().getStatusCode());
        }
    }
}