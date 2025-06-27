package service;

import java.sql.SQLException;
import java.util.Date;

import config.Conexion;
import dao.HistorialRutaDao;
import dao.HistorialRutaDaoImpl;
import dao.NodoDaoImpl;
import model.HistorialRuta;
import model.Ruta;
import paginator.PageRender;

public class HistorialRutaServiceImpl implements HistorialRutaService{
	
	private HistorialRutaDao hrDao;
	
	public HistorialRutaServiceImpl() {
		
		
		 try {
   		 //Inicializamos la conexion a la base de datos
			this.hrDao = new HistorialRutaDaoImpl(Conexion.getConexion());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public PageRender<HistorialRuta> consultarTodasLasRutas(int pagina, int tamañoPagina) {
			return hrDao.consultarHistorialRutas(pagina, tamañoPagina);
	}


	@Override
	public PageRender<HistorialRuta> consultarPorNombreOFecha(String nombre_ciudad, Date fechaConsulta, int pagina, int tamañoPagina) {
	   
	    String normalizedNombre = (nombre_ciudad != null && nombre_ciudad.isEmpty()) ? null : nombre_ciudad;
	    return hrDao.consultarPorNombreOFecha(normalizedNombre, fechaConsulta, pagina, tamañoPagina);
	}


	@Override
	public void guardarHistorialRuta(Long idRuta) {
		hrDao.guardarHistorial(idRuta);
		
	}


	

}
