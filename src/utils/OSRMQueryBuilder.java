package utils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.json.JSONArray;
public class OSRMQueryBuilder {

	public static final String construirQueryOSRM(double latOrigen,
	        double lonOrigen, double latDestino, double lonDestino) {

	    return String.format(Locale.US,
	        "http://localhost:5000/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=geojson",
	        lonOrigen, latOrigen, lonDestino, latDestino);
	}

}
