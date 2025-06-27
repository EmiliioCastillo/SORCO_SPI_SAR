package service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import config.Conexion;
import dao.NodoDaoImpl;
import dao.RutaDao;
import dao.RutaDaoImpl;

import model.Nodo;
import model.Ruta;
import utils.EstadisticasResultado;


public class RutaServiceImpl implements RutaService{
	
	private RutaDao rutaDao;
	
	
	
	public RutaServiceImpl() {
		try {
			this.rutaDao = new RutaDaoImpl(Conexion.getConexion());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public Long guardarRuta(Long origen, Long destino, double distancia) {
		return rutaDao.guardarRuta(origen, destino, distancia);
		
	}



	@Override
	public EstadisticasResultado obtenerEstadisticasRuta(LocalDate fecha) {
	    Map<Ruta, Integer> rutaConsultas = rutaDao.obtenerEstadisticaRuta(fecha);
	    
	   
	    
	    // Mapa para contar frecuencia de cada ciudad
	    Map<String, Integer> ciudadFrecuencia = new HashMap<>();
	    
	    // Contamos apariciones de cada ciudad
	    for (Map.Entry<Ruta, Integer> entry : rutaConsultas.entrySet()) {
	        Ruta ruta = entry.getKey();
	        int cantidad = entry.getValue();

	        String origen = ruta.getOrigen().getNombre();
	        String destino = ruta.getDestino().getNombre();
	        
	        // Sumamos frecuencia para ambas ciudades, obtenemos el valor asociado a una clave en este caso del map ciudadFrecuencia
	        ciudadFrecuencia.put(origen, ciudadFrecuencia.getOrDefault(origen, 0) + cantidad);
	        ciudadFrecuencia.put(destino, ciudadFrecuencia.getOrDefault(destino, 0) + cantidad);
	    }
	    
	    // Ordenamos ciudades por frecuencia (mayor a menor)
	    List<Map.Entry<String, Integer>> ciudadesOrdenadas = new ArrayList<>(ciudadFrecuencia.entrySet());
	    ciudadesOrdenadas.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
	    
	    // Separamos en listas para el resultado
	    List<String> nombresCiudades = new ArrayList<>();
	    List<Integer> frecuencias = new ArrayList<>();
	    
	    for (Map.Entry<String, Integer> entry : ciudadesOrdenadas) {
	        nombresCiudades.add(entry.getKey());//[Buenos aires, sgo, etc]
	        frecuencias.add(entry.getValue()); //[5,3,2,1]
	    }
	    
	    // La ciudad más frecuente es la primera de la lista ordenada
	    String ciudadMasFrecuente = !nombresCiudades.isEmpty() ? nombresCiudades.get(0) : "";
	    
	    return new EstadisticasResultado(nombresCiudades, frecuencias, ciudadMasFrecuente);
	}
	private String obtenerCiudadMasFrecuente(Map<String, Integer> frecuenciaMap) {
	    return frecuenciaMap.entrySet().stream()
	        .max((e1, e2) -> {
	            int cmp = e1.getValue().compareTo(e2.getValue());
	            if (cmp != 0) return cmp;
	            
	            // Desempate: ciudad con nombre más corto primero
	            return Integer.compare(
	                e2.getKey().length(), 
	                e1.getKey().length()
	            );
	        })
	        .map(Map.Entry::getKey)
	        .orElse("");
	}

	 private String abreviar(String nombre) {
		    if (nombre.contains(",")) {
		        return nombre.split(",")[0];
		    }
		    return nombre;
		}

	
	
		   

	}
	
    

	


	  

	     
		

	  
	

	
	
	
	
	
	



	

