package service;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import config.Conexion;
import dao.NodoDao;
import dao.NodoDaoImpl;
import dao.RutaDao;
import dao.RutaDaoImpl;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.Nodo;
import service.RutaService;
import service.RutaServiceImpl;
import utils.OSRMQueryBuilder;


public class NodoServiceImpl implements NodoService{

	private NodoDao nodoDao;
	private HistorialRutaService hrService = new HistorialRutaServiceImpl();
	 private RutaService rutaService = new RutaServiceImpl();
	 private OSRMService osrmService = new OSRMServiceImpl();
	 private OSRMQueryBuilder osrmQueryBuilder;
	
	  private static final String BASE_URL = "https://nominatim.openstreetmap.org/search?q=";
	    private static final String FORMAT = "&format=json";
	    private Long idOrigenActual = null;
	    private Long idDestinoActual = null;
	    private Double latOrigen, lonOrigen, latDestino, lonDestino;
	    
	   
	    

	   
	    
	public NodoServiceImpl() {
		
	
		 try {
    		 //Inicializamos la conexion a la base de datos
			this.nodoDao = new NodoDaoImpl(Conexion.getConexion());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void consultarCoordenadasYGuardar(String ciudad, boolean esOrigen) {
	    String url = BASE_URL + ciudad.replace(" ", "+") + FORMAT;
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(url))
	            .header("User-Agent", "SPI_SAR/1.0") // Requerido por Nominatim
	            .build();
	    HttpResponse<String> response = null;
	    try {
	        response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    } catch (IOException | InterruptedException e) {
	        e.printStackTrace();
	    }
	    if (response != null && response.statusCode() == 200) {
	        JSONArray resultados = new JSONArray(response.body());
	       
	        //Obtenemos el json de nominatim
	        JSONObject primerResultado = resultados.getJSONObject(0);
	        
	        double latitud = Double.parseDouble(primerResultado.getString("lat"));
	        double longitud = Double.parseDouble(primerResultado.getString("lon"));
	     
	        Nodo nodoExistente = nodoDao.obtenerNodoPorNombre(ciudad);
	        
	        Long idNodo;
	        //Si la ciudad ya existe
	        if (nodoExistente != null) {
	         //traemos el id
	            idNodo = nodoExistente.getId_nodo();
	         
	           
	         
	           //Si esa latitud es diferente a la que existe ya, osea
	            //Santiago del EStero, Santiago del Estero != Santiago del Estero, Argentina vas a actualizar 
	            //Al nuevo que seria Santiago del EStero Argentina
	            if (nodoExistente.getLatitud() != latitud || nodoExistente.getLongitud() != longitud) {
	            
	                nodoExistente.setLatitud(latitud);
	                nodoExistente.setLongitud(longitud);
	                nodoDao.actualizarNodo(nodoExistente);
	            }
	            
	        } else {
	     //Si no existe creamos uno nuevo
	            Nodo nodoCiudad = new Nodo();
	            nodoCiudad.setNombre(ciudad);
	            nodoCiudad.setLatitud(latitud);
	            nodoCiudad.setLongitud(longitud);
	           
	            idNodo = (long) nodoDao.guardarNodo(nodoCiudad);
	          
	        }
	      //Si el booleano que pasamos de App es true quiere decir que es de la casilla de origen
	        if (esOrigen) {
	            idOrigenActual = idNodo;
	            latOrigen = latitud;
	            lonOrigen = longitud;
	        } else {
	            idDestinoActual = idNodo;
	            latDestino = latitud;
	            lonDestino = longitud;
	        }
	        
	        
	    } else {
	        System.err.println("No se pudo obtener la respuesta de la API.");
	    }
	}

	
	   private void mostrarMapaHTML(String html) {
		   
	        WebView webView = new WebView();
	        webView.getEngine().loadContent(html, "text/html");

	        Stage stage = new Stage();
	        stage.setTitle("Ruta entre ciudades");
	        stage.setScene(new Scene(webView, 800, 600));
	        stage.show();
	    }

	@Override
	public void calcularRutaDesdeBD(String ciudadOrigen, String ciudadDestino) {
	    Nodo nodoOrigen = nodoDao.obtenerNodoPorNombre(ciudadOrigen);
	    Nodo nodoDestino = nodoDao.obtenerNodoPorNombre(ciudadDestino);

	    if (nodoOrigen == null || nodoDestino == null) {
	        System.out.println("Una o ambas ciudades no están registradas.");
	      
	    }

	    double latOrigen = nodoOrigen.getLatitud();
	    double lonOrigen = nodoOrigen.getLongitud();
	    double latDestino = nodoDestino.getLatitud();
	    double lonDestino = nodoDestino.getLongitud();
	    
	   
	    String query = OSRMQueryBuilder.construirQueryOSRM(latOrigen, lonOrigen, latDestino, lonDestino);
	    JSONObject respuesta = osrmService.consultarOSRM(query);
	    
	    if(respuesta != null) {
	    	String html = generarMapa(nodoOrigen, nodoDestino, respuesta);
	    	mostrarMapaHTML(html);
	    	
	    	  JSONArray routes = respuesta.getJSONArray("routes");
	    	  JSONObject ruta = routes.getJSONObject(0);
	    	  
	    	  double distancia = ruta.getDouble("distance");
	    	 double distanciaFormateada = distancia / 1000;
	    	 Long idGuardado = rutaService.guardarRuta(nodoOrigen.getId_nodo(), nodoDestino.getId_nodo(), distanciaFormateada);
	    	 hrService.guardarHistorialRuta(idGuardado);
	    }
	}


	@Override
	public String generarMapa(Nodo origen, Nodo destino, JSONObject respuesta) {
	    StringBuilder html = new StringBuilder();
	    
	    try {
	    	//Capturamos estas propiedades del json
	        JSONArray routes = respuesta.getJSONArray("routes");
	        JSONObject primeraRuta = routes.getJSONObject(0);
	       
	        JSONObject geometry = primeraRuta.getJSONObject("geometry");
	        JSONArray coordinates = geometry.getJSONArray("coordinates");
	        JSONArray waypoints = respuesta.getJSONArray("waypoints");
	        double distancia = primeraRuta.getDouble("distance"); // en metros
	        double duracion = primeraRuta.getDouble("duration"); // en segundos
	     
	        int horas = (int) (duracion / 3600);
	        int minutos = (int) ((duracion % 3600) / 60);
	        String tiempoFormateado = String.format("%d h %02d min", horas, minutos);
	        // Convertimos a unidades más legibles
	        double distanciaKm = distancia / 1000;
	        double duracionHoras = duracion / 3600;
	        double velocidad = (duracionHoras > 0) ? (distanciaKm / duracionHoras) : 0;
	        // 2. Validación de waypoints(calles)
	        if (waypoints.length() < 2) {
	            return generarHtmlError("waypoints faltantes");
	        }

	        // 3. Extraemos datos
	        String calleOrigen = waypoints.getJSONObject(0).optString("name", "Calle desconocida");
	        String calleDestino = waypoints.getJSONObject(1).optString("name", "Calle desconocida");

	        // 4. Construimos las coordenadas
	        StringBuilder rutaCoords = new StringBuilder("[");
	        for (int i = 0; i < coordinates.length(); i++) {
	        	 //Aqui es donde inicia el punto y vamos iterando por todas las coordenadas, desde el principio a final
	            JSONArray punto = coordinates.getJSONArray(i);
	           
	            
	            double lon = punto.getDouble(0);
	            double lat = punto.getDouble(1);
	            rutaCoords.append(String.format(Locale.US, "[%.6f,%.6f]", lon, lat));
	           
	            if (i < coordinates.length() - 1) rutaCoords.append(",");
	        }
	        rutaCoords.append("]");

	        // 5. Escapar caracteres especiales
	        String popupOrigen = escapeHtmlJs(origen.getNombre()) + "<br/>Calle: " + escapeHtmlJs(calleOrigen);
	        String popupDestino = escapeHtmlJs(destino.getNombre()) + "<br/>Calle: " + escapeHtmlJs(calleDestino);

	        // 6. Construcción del HTML
	        html.append("<!DOCTYPE html>")
            .append("<html><head>")
            .append("<meta charset='UTF-8'>")
            .append("<title>Ruta</title>")
            .append("<link rel='stylesheet' href='https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.15.1/css/ol.css'>")
            .append("<script src='https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.15.1/build/ol.js'></script>")
            .append("<style>")
            .append("#map { width: 100%; height: 600px; }")
            .append(".info-panel { position: absolute; top: 10px; right: 10px; z-index: 1000; background: rgba(255,255,255,0.9); padding: 15px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.2); font-family: Arial, sans-serif; }")
            .append("#info-content p { margin: 5px 0; }")
            .append("</style>")
            .append("</head><body>")
            .append("<div class='info-panel'>")
            .append("<h3 style='margin-top: 0;'>Detalles de la Ruta</h3>")
            .append("<div id='info-content'>")
            .append("<p>Distancia total: <span id='distancia'>").append(String.format("%.2f km", distanciaKm)).append("</span></p>")
            .append("<p>Duración estimada: <span id='duracion'>").append(String.format("%.1f horas", duracionHoras)).append("</span></p>")
            // La velocidad debe estar aquí en el panel HTML
            .append("<p>Velocidad promedio: <span id='velocidad'>").append(String.format("%.1f km/h", velocidad)).append("</span></p>")
            .append("</div>")
            .append("</div>")
            .append("<div id='map'></div>")
            .append("<script>")
            .append("var rutaCoords = ").append(rutaCoords).append(";")
            
            .append("var rutaTransformada = rutaCoords.map(coord => ol.proj.fromLonLat(coord));")
            
            .append("var origenFeature = new ol.Feature({")
            .append("  geometry: new ol.geom.Point(ol.proj.fromLonLat([")
            .append(origen.getLongitud()).append(",").append(origen.getLatitud())
            .append("])), name: '").append(popupOrigen).append("'});")
            
            .append("var destinoFeature = new ol.Feature({")
            .append("  geometry: new ol.geom.Point(ol.proj.fromLonLat([")
            .append(destino.getLongitud()).append(",").append(destino.getLatitud())
            .append("])), name: '").append(popupDestino).append("'});")
            
            .append("var rutaFeature = new ol.Feature({")
            .append("  geometry: new ol.geom.LineString([])")
            .append("});")
            
            .append("var currentCoords = [];")
            .append("var distanciaTotal = ").append(distancia).append(";")  // Distancia en metros
            
            .append("function animarRuta(indice) {")
            .append("  if (indice < rutaTransformada.length) {")
            .append("    currentCoords.push(rutaTransformada[indice]);")
            .append("    rutaFeature.getGeometry().setCoordinates(currentCoords);")
            // Actualizar distancia durante la animación
            .append("    var distanciaActual = (indice / rutaTransformada.length) * distanciaTotal;")
            .append("    document.getElementById('distancia').textContent = (distanciaActual/1000).toFixed(2) + ' km';")
            .append("    setTimeout(() => animarRuta(indice + 1), 5);")
            .append("  }")
            .append("}")
            .append("animarRuta(0);")
            
            .append("var iconStyle = new ol.style.Style({")
            .append("  image: new ol.style.Circle({")
            .append("    radius: 8,")
            .append("    fill: new ol.style.Fill({ color: '#FF0000' }),")
            .append("    stroke: new ol.style.Stroke({ color: '#FFFFFF', width: 2 })")
            .append("  })")
            .append("});")
            
            .append("var lineStyle = new ol.style.Style({")
            .append("  stroke: new ol.style.Stroke({")
            .append("    color: '#FF5722', width: 4") 
            .append("  })")
            .append("});")
            
            .append("var vectorSource = new ol.source.Vector({")
            .append("  features: [origenFeature, destinoFeature, rutaFeature]")
            .append("});")
            
            .append("var vectorLayer = new ol.layer.Vector({")
            .append("  source: vectorSource,")
            .append("  style: function(feature) {")
            .append("    return feature.getGeometry().getType() === 'LineString' ? lineStyle : iconStyle;")
            .append("  }")
            .append("});")
            
            .append("var map = new ol.Map({")
            .append("  target: 'map',")
            .append("  layers: [")
            .append("    new ol.layer.Tile({ source: new ol.source.OSM() }),")
            .append("    vectorLayer")
            .append("  ],")
            .append("  view: new ol.View({")
            .append("    center: ol.proj.fromLonLat([")
            .append((origen.getLongitud() + destino.getLongitud())/2).append(",")
            .append((origen.getLatitud() + destino.getLatitud())/2).append("]),")
            .append("    zoom: 13")
            .append("  })")
            .append("});")
            
            .append("map.getView().fit(vectorSource.getExtent(), {")
            .append("  padding: [50, 50, 50, 50],")
            .append("  duration: 1000")
            .append("});")
            
            .append("var popup = new ol.Overlay({ element: document.createElement('div') });")
            .append("map.addOverlay(popup);")
            .append("map.on('click', function(evt) {")
            .append("  var feature = map.forEachFeatureAtPixel(evt.pixel, function(f) { return f; });")
            .append("  if (feature) {")
            .append("    popup.getElement().innerHTML = feature.get('name');")
            .append("    popup.setPosition(evt.coordinate);")
            .append("  }")
            .append("});")
            
            .append("</script>")
            .append("</body></html>");

	    } catch (JSONException e) {
	        return generarHtmlError("Error procesando datos: " + e.getMessage());
	    }
	    
	    return html.toString();
	}
	
	// Método auxiliar para generar errores
	private String generarHtmlError(String mensaje) {
	    return "<html><body style='padding:20px;color:red;'><h3>Error:</h3><p>" + escapeHtmlJs(mensaje) + "</p></body></html>";
	}

	// Método para escapar caracteres especiales
	private String escapeHtmlJs(String input) {
	    return input.replace("'", "\\'")
	               .replace("\"", "&quot;")
	               .replace("<", "&lt;")
	               .replace(">", "&gt;");
	}

}
