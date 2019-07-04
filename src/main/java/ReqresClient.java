import Entities.RegisterUser;
import Entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ReqresClient {

    private String apiUrl = "https://reqres.in/api/";
    private int statusCode = 0;

    private HttpResponse Get(String url){
        HttpUriRequest request = new HttpGet(url );
        HttpResponse response = null;
        try {
            response = HttpClientBuilder.create().build().execute( request );
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.statusCode = response.getStatusLine().getStatusCode();
        return response;
    }

    private HttpResponse Post(String url, String json){
        HttpPost post = new HttpPost(url);

        post.addHeader("Content-Type", "application/json");
        StringEntity input = null;
        try {
            input = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        post.setEntity(input);
        HttpResponse response = null;
        try {
            response = HttpClientBuilder.create().build().execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.statusCode = response.getStatusLine().getStatusCode();
        return response;
    }

    private  <T> T retrieveResourceFromResponse(HttpResponse response, Class<T> clazz)
    {
        T result = null;
        String jsonFromResponse = HttpResponseToString(response);
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        try {
            result = mapper.readValue(jsonFromResponse, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    JsonNode getJson(HttpResponse response){
        String jsonFromResponse = HttpResponseToString(response);
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = null;
        try {
              jsonNode = mapper.readTree(jsonFromResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    int GetStatusCode(){
        return this.statusCode;
    }

    User GetUser(int id){
        return retrieveResourceFromResponse(Get(String.format("%susers/%s", this.apiUrl, id)), User.class);
    }

    String HttpResponseToString(HttpResponse response){
        String jsonFromResponse = null;
        try {
            jsonFromResponse = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonFromResponse;
    }
    JsonNode Register(RegisterUser user){
        String json = null;
        try {
              json  = new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  getJson(Post(String.format("%sregister", this.apiUrl), json));
    }
}
