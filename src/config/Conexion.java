package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Clase encargada de establecer y cerrar la conexión con la base de datos utilizando 	     #
credenciales almacenadas como variables de entorno para mayor seguridad. 				 #
Incluye soporte para manejo de excepciones y uso del driver oficial de MySQL.			 #
Se definen atributos para las credenciales, se establece un constructor que permitira ini#
ciar la conexion en los demas modulos.													 #
##########################################################################################
MODIFICACIONES																			 #
																						 #
##########################################################################################
*/

public class Conexion {
	 private static final String URL = "jdbc:mysql://localhost:3306/SPI_SAR";
	 private static final String USUARIO = System.getenv("DATABASE_USERNAME");
	 private static final String CONTRASEÑA = System.getenv("DATABASE_PASSWORD");
	 private static final String driver = "com.mysql.cj.jdbc.Driver";
	 
	 Connection cx;

	 public Conexion() {
		 
	 }
	 public static Connection getConexion() throws SQLException, ClassNotFoundException {
		 	Class.forName(driver);
	        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA); //Hacemos la cadena de conexion
	    }
	 
	 public void desconexion() {
		 try {
			cx.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
}
