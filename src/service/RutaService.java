package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

import model.Nodo;
import model.Ruta;
import utils.EstadisticasResultado;


public interface RutaService {
	
	Long guardarRuta(Long origen, Long destino, double distancia);
	EstadisticasResultado obtenerEstadisticasRuta(LocalDate fecha);
}
