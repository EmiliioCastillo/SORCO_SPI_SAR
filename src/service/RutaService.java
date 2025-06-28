package service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

import dto.RutaHistorialDTO;
import model.Nodo;
import model.Ruta;
import utils.EstadisticasResultado;


public interface RutaService {
	
	Long guardarRuta(Long origen, Long destino, double distancia);
	EstadisticasResultado obtenerEstadisticasRuta(LocalDate fecha);
	//el metodo este debe recibir una lista que es los datos que vamos a escribir en el pdf y el string que sera el nombre del pdf
	void exportarPDF(String nombrePdf);
}
