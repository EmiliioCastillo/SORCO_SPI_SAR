package service;


import org.json.JSONObject;

import model.Nodo;

public interface NodoService {
	public void consultarCoordenadasYGuardar(String ciudad,  boolean esOrigen);
	
	void calcularRutaDesdeBD(String ciudadOrigen, String ciudadDestino);
	public String generarMapa(Nodo origen, Nodo destino, JSONObject respuesta);
	
}
