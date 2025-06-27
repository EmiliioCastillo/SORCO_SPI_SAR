package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import config.Conexion;
import model.HistorialRuta;
import model.Nodo;
import model.Ruta;
import paginator.PageRender;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Implementación de la interfaz HistorialRutaDao. Encargada de gestionar el acceso		 #
y manipulación de los registros del historial de rutas consultadas en la base de datos.  #
Incluye operaciones para:																 #
- Consultar el historial paginado de rutas.												 #
- Registrar una nueva consulta en el historial.										     #
- (Pendiente) Filtrar historial por nombre o fecha.										 #
Utiliza SQL nativo mediante JDBC y estructuras propias sin el uso de ORM ni frameworks.  #
Conexión establecida directamente con la clase Conexion, utilizando variables de entorno.#
##########################################################################################
MODIFICACIONES																			 #
- [Fecha] - [Descripción del cambio] - [Autor]											 #
- 2025-05-26 - Se crea la clase y se implementan métodos de consulta y guardado. - EEC   #
##########################################################################################
*/

public class HistorialRutaDaoImpl implements HistorialRutaDao{

	 private Connection conexion;
	
	 public HistorialRutaDaoImpl(Connection conexion) {
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
	
	
	
	 @Override
	 public PageRender<HistorialRuta> consultarHistorialRutas(int pagina, int tamañoPagina) {
		 //offset es 0 y tamaño de pagina ahora es 10
		 int offset = (pagina - 1) * tamañoPagina;
		
	     String sql = """
	         SELECT 
	             historial_ruta.id_historial,
	             nodo1.nombre_ciudad AS origen,
	             nodo2.nombre_ciudad AS destino,
	             historial_ruta.fecha_consulta
	         FROM 
	             historial_ruta
	         INNER JOIN 
	             ruta ON historial_ruta.rutaConsultada = ruta.id_ruta
	         INNER JOIN 
	             nodo nodo1 ON ruta.origen_id = nodo1.id_nodo
	         INNER JOIN 
	             nodo nodo2 ON ruta.destino_id = nodo2.id_nodo
	         LIMIT ? OFFSET ?
	     """;

	     String countSql = "SELECT COUNT(*) FROM historial_ruta";
	     List<HistorialRuta> lista = new ArrayList<>();

	     try {
	         PreparedStatement stmt = conexion.prepareStatement(sql);
	         stmt.setInt(1, tamañoPagina);
	         stmt.setInt(2, offset);

	         ResultSet rs = stmt.executeQuery();
	         while (rs.next()) {
	             HistorialRuta historial = new HistorialRuta();
	           
	             historial.setFechaConsulta(rs.getDate("fecha_consulta"));

	             Nodo origen = new Nodo();
	             origen.setNombre(rs.getString("origen"));

	             Nodo destino = new Nodo();
	             destino.setNombre(rs.getString("destino"));

	             Ruta ruta = new Ruta();
	             ruta.setOrigen(origen);
	             ruta.setDestino(destino);

	             historial.setRutaConsultada(ruta);
	             lista.add(historial);
	         }

	         PreparedStatement countStmt = conexion.prepareStatement(countSql);
	         ResultSet countRs = countStmt.executeQuery();

	         int totalElementos = 0;
	         if (countRs.next()) {
	             totalElementos = countRs.getInt(1);
	         }

	         return new PageRender<>(lista, pagina, tamañoPagina, totalElementos);
	     } catch (SQLException e) {
	         e.printStackTrace();
	         throw new RuntimeException("Error al consultar el historial", e);
	     }
	 }



	 @Override
	 public PageRender<HistorialRuta> consultarPorNombreOFecha(String nombre_ciudad, Date fechaConsulta, int pagina, int tamañoPagina) {
	     
	     String selectSQL = "SELECT \n"
	          + "    hr.id_historial,\n"
	          + "    n1.nombre_ciudad AS origen,\n"
	          + "    n2.nombre_ciudad AS destino,\n"
	          + "    hr.fecha_consulta\n"
	          + "FROM historial_ruta hr\n"
	          + "INNER JOIN ruta r ON hr.rutaConsultada = r.id_ruta\n"
	          + "INNER JOIN nodo n1 ON r.origen_id = n1.id_nodo\n"
	          + "INNER JOIN nodo n2 ON r.destino_id = n2.id_nodo\n"
	          + "WHERE (LOWER(n1.nombre_ciudad) LIKE LOWER(?) OR LOWER(n2.nombre_ciudad) LIKE LOWER(?))\n"
	          + "    OR hr.fecha_consulta = ?\n"
	          + "LIMIT ? OFFSET ?";

	     String countSql = "SELECT COUNT(*) \n"
	          + "FROM historial_ruta hr\n"
	          + "INNER JOIN ruta r ON hr.rutaConsultada = r.id_ruta\n"
	          + "INNER JOIN nodo n1 ON r.origen_id = n1.id_nodo\n"
	          + "INNER JOIN nodo n2 ON r.destino_id = n2.id_nodo\n"
	          + "WHERE (LOWER(n1.nombre_ciudad) LIKE LOWER(?) OR LOWER(n2.nombre_ciudad) LIKE LOWER(?))\n"
	          + "    OR hr.fecha_consulta = ?";

	     List<HistorialRuta> lista = new ArrayList<>();
	     int totalResultados = 0;

	     try (PreparedStatement stmt = conexion.prepareStatement(selectSQL)) {
	    	 int offset = (pagina <= 0 ? 0 : pagina - 1) * tamañoPagina;


	         stmt.setString(1, "%" + nombre_ciudad + "%");
	         stmt.setString(2, "%" + nombre_ciudad + "%");
	         stmt.setDate(3, new java.sql.Date(fechaConsulta.getTime()));
	         stmt.setInt(4, tamañoPagina);
	         stmt.setInt(5, offset);
	        
	         try (ResultSet rs = stmt.executeQuery()) {
	             while (rs.next()) {
	                 HistorialRuta historial = new HistorialRuta();

	                 Nodo origen = new Nodo();
	                 origen.setNombre(rs.getString("origen"));

	                 Nodo destino = new Nodo();
	                 destino.setNombre(rs.getString("destino"));

	                 Ruta ruta = new Ruta();
	                 ruta.setOrigen(origen);
	                 ruta.setDestino(destino);

	                 historial.setIdHistorial(rs.getInt("id_historial"));
	                 historial.setFechaConsulta(rs.getDate("fecha_consulta"));
	                 historial.setRutaConsultada(ruta);

	                 lista.add(historial);
	                 
	                 
	             }
	         }
	     } catch (SQLException e) {
	         e.printStackTrace(); 
	     }
	     try (PreparedStatement countStmt = conexion.prepareStatement(countSql)) {
	         countStmt.setString(1, "%" + nombre_ciudad + "%");
	         countStmt.setString(2, "%" + nombre_ciudad + "%");
	         countStmt.setDate(3, new java.sql.Date(fechaConsulta.getTime()));

	         try (ResultSet countRs = countStmt.executeQuery()) {
	             if (countRs.next()) {
	                 totalResultados = countRs.getInt(1);
	             }
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     
	     return new PageRender<>(lista, pagina, tamañoPagina, totalResultados);
	     
	 }



	@Override
	public void guardarHistorial(Long rutaConsultada) {
	    String insertSQL = "INSERT INTO historial_ruta (rutaConsultada, fecha_consulta) VALUES (?, ?)";
	    try {
	        PreparedStatement stmt = conexion.prepareStatement(insertSQL);
	        stmt.setLong(1, rutaConsultada);

	       
	        Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
	        stmt.setTimestamp(2, fechaActual);

	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



}
