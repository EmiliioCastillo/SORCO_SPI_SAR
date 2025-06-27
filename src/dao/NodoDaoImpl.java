package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import config.Conexion;
import model.Nodo;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Implementación concreta de la interfaz NodoDao. Se encarga de gestionar todas las        #
operaciones relacionadas con la persistencia de la entidad Nodo en la base de datos.     #
Incluye lógica para:																	 #
- Insertar un nuevo nodo solo si no existe, devolviendo su ID (clave primaria).         #
- Consultar un nodo por su nombre.													     #
- Actualizar la latitud y longitud de un nodo existente.								 #
La clase establece la conexión con la base de datos utilizando el helper Conexion y maneja#
excepciones de SQL de forma controlada.												     #
##########################################################################################
MODIFICACIONES																			 #
##########################################################################################
*/


public class NodoDaoImpl implements NodoDao{
	 private Connection conexion;
	 private Nodo nodo;
	 public NodoDaoImpl(Connection conexion) {
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
	    /*
	     * GuardarNodo tiene que devolver un ID porque nos va a servir para determinar si lo que
	     * El usuario guardo, fue una ciudad de origen o fue una ciudad de destino.
	     * */
	 public int guardarNodo(Nodo nodo) {
	        int id = -1;
	        try {
	           
	            String verificarSQL = "SELECT id_nodo FROM nodo WHERE nombre_ciudad = ?";
	            PreparedStatement verificarStmt = conexion.prepareStatement(verificarSQL);
	            verificarStmt.setString(1, nodo.getNombre());
	            ResultSet rs = verificarStmt.executeQuery();
	            
	            if (rs.next()) {
	                id = rs.getInt("id_nodo"); 
	               
	            } else {
	                // Si no existe, lo insertamos
	                String insertSQL = "INSERT INTO nodo (nombre_ciudad, latitud, longitud) VALUES (?, ?, ?)";
	               
	                PreparedStatement insertStmt = conexion.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	                insertStmt.setString(1, nodo.getNombre());
	                insertStmt.setDouble(2, nodo.getLatitud());
	                insertStmt.setDouble(3, nodo.getLongitud());
	                insertStmt.executeUpdate();
	              
	             
	                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    id = generatedKeys.getInt(1);
	                }
	                insertStmt.close();
	            }
	            verificarStmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return id;
	    }
 
	 public void actualizarNodo(Nodo nodo) {
		    String sql = "UPDATE nodo SET latitud = ?, longitud = ? WHERE id_nodo = ?";

		    try ( 
		         PreparedStatement stmt = conexion.prepareStatement(sql)) {
		         
		        stmt.setDouble(1, nodo.getLatitud());
		        stmt.setDouble(2, nodo.getLongitud());
		        stmt.setLong(3, nodo.getId_nodo());

		        stmt.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

	 @Override
	 public Nodo obtenerNodoPorNombre(String nombre) {
	     String consultaSql = "SELECT * FROM nodo WHERE nombre_ciudad = ?";
	   
	     try (PreparedStatement stmt = conexion.prepareStatement(consultaSql)) {
	         stmt.setString(1, nombre);
	         ResultSet rs = stmt.executeQuery();
	         //Si la query resulta correct
	         if (rs.next()) {
	             nodo = new Nodo();
	             nodo.setId_nodo(rs.getLong("id_nodo"));
	             nodo.setNombre(rs.getString("nombre_ciudad"));
	             nodo.setLatitud(rs.getDouble("latitud"));
	             nodo.setLongitud(rs.getDouble("longitud"));
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     
	     return nodo;  // Retorna el nodo si se encontró, o null si no existe
	 }
	 
	 public Nodo obtenerNodoPorId(Long id_nodo) {
	      
	        String sql = "SELECT id_nodo, nombre_ciudad, latitud, longitud FROM nodo WHERE id_nodo = ?";

	        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

	            ps.setLong(1, id_nodo);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                nodo = new Nodo();
	                nodo.setId_nodo(rs.getLong("id_nodo"));
	                nodo.setNombre(rs.getString("nombre_ciudad"));
	                nodo.setLatitud(rs.getDouble("latitud"));
	                nodo.setLongitud(rs.getDouble("longitud"));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return nodo;
	    }
	
		   
}



