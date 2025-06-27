package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import config.Conexion;
import model.EstadisticaRuta;
import model.Nodo;
import model.Ruta;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Implementacion concreta de la interfaz RutaDao, donde contiene el metodo guardarRuta que #
inserta registros en la base de datos.													 #
##########################################################################################
MODIFICACIONES																			 #
##########################################################################################
*/
public class RutaDaoImpl implements RutaDao {
	 private Connection conexion;
	 private NodoDao nodoDao;
	 //Constructor para la conexion con la base de datos.
	 public RutaDaoImpl(Connection conexion) {
	        this.conexion = conexion;
	    	
			try {
	            try {
					this.conexion = Conexion.getConexion();
					this.nodoDao = new NodoDaoImpl(Conexion.getConexion());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } catch (SQLException e) {
	            throw new RuntimeException("Error al conectar con la base de datos", e);
	        }
	    }
	 
	 public Long guardarRuta(Long origenId, Long destinoId, double distancia) {
		    String insertSQL = "INSERT INTO ruta (origen_id, destino_id, distancia) VALUES (?, ?, ?)";
		    try {
		        PreparedStatement stmt = conexion.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		        stmt.setLong(1, origenId);
		        stmt.setLong(2, destinoId);
		        stmt.setDouble(3, distancia);
		        stmt.executeUpdate();

		        ResultSet rs = stmt.getGeneratedKeys();
		        if (rs.next()) {
		            return rs.getLong(1); // ID de la nueva ruta
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return -1L;
		}

	
	public Map<Ruta, Integer> obtenerEstadisticaRuta(LocalDate fecha) {
		 Map<Ruta, Integer> conteoRutas = new LinkedHashMap<>();
		 String sql = """
		            SELECT 
		                r.id_ruta, 
		                r.origen_id, 
		                r.destino_id, 
		                r.distancia, 
		                COUNT(*) AS cantidad
		            FROM 
		                historial_ruta hr
		            JOIN 
		                ruta r ON hr.rutaConsultada = r.id_ruta
		            WHERE 
		                hr.fecha_consulta >= ?
		            GROUP BY 
		                r.id_ruta, r.origen_id, r.destino_id, r.distancia
		            ORDER BY 
		                cantidad DESC
		        """;

		 try (PreparedStatement stmt = conexion.prepareStatement(sql)){
			 //Rellenamos el valor de la fecha
			 	stmt.setDate(1, Date.valueOf(fecha));
			 	//Ejecutamos la consulta
	            ResultSet rs = stmt.executeQuery();
	            
	            while (rs.next()) {
	                Ruta ruta = new Ruta();
	                ruta.setId_ruta(rs.getLong("id_ruta"));

	                Nodo origen = nodoDao.obtenerNodoPorId(rs.getLong("origen_id"));
	                Nodo destino = nodoDao.obtenerNodoPorId(rs.getLong("destino_id"));

	                ruta.setOrigen(origen);
	                ruta.setDestino(destino);
	                ruta.setDistancia(rs.getDouble("distancia"));

	                int cantidad = rs.getInt("cantidad");
	                conteoRutas.put(ruta, cantidad);
	            }


		 }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
		 return conteoRutas;
	}


	
	 
	 
	 
	}

	

	
	    
	    
	


