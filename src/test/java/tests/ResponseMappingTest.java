package tests;

import Entity.Facts;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class ResponseMappingTest extends BaseTest {
    public static ObjectMapper mapper = new ObjectMapper();

    @Test
    public static void test_GetResponseMapping() throws IOException {
        HttpGet request = new HttpGet
                ("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);
        List<Facts> factsList = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Facts>>() {
        });
        factsList.stream().filter(value -> value.getUpvotes() > 0).forEach(System.out::println);
    }
}
