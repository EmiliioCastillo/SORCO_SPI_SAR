package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import config.Conexion;
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

	 //Constructor para la conexion con la base de datos.
	 public RutaDaoImpl(Connection conexion) {
	        this.conexion = conexion;
			try {
	            try {
					this.conexion = Conexion.getConexion();
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

	


	
	 
	 
	 
	}

	

	
	    
	    
	


