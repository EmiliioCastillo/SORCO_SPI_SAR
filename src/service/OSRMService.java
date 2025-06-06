package service;

import org.json.JSONObject;

public interface OSRMService {

	public JSONObject consultarOSRM(String query);
}
