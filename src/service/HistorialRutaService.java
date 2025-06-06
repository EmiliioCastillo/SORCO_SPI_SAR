package service;

import java.util.Date;

import model.HistorialRuta;
import model.Ruta;
import paginator.PageRender;

public interface HistorialRutaService {

	
	PageRender<HistorialRuta> consultarTodasLasRutas(int pagina, int tamañoPagina);
	PageRender<HistorialRuta> consultarPorNombreOFecha(String nombre, Date fechaConsulta, int pagina, int tamañoPagina);
	void guardarHistorialRuta(Long idRuta);
}
