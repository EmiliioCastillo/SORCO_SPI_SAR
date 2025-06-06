package service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class OSRMServiceImpl implements OSRMService{

	 @Override
	    public JSONObject consultarOSRM(String url) {
	        try {
	            HttpClient client = HttpClient.newHttpClient();

	            HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(url))
	                .header("User-Agent", "SPI_SAR/1.0")
	                .header("Accept", "application/json")
	                .build();

	            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	            return new JSONObject(response.body());

	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	
	

}
