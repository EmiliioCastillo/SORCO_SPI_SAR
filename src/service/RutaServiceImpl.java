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




	
	
		   

	}
	
    

	


	  

	     
		

	  
	

	
	
	
	
	
	



	

