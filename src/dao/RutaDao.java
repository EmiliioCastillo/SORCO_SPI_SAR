package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

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
Interfaz que define las operaciones relacionadas a las rutas.							 #
Incluye el metodo guardarRuta que este funciona guardando la ruta con el origen,         # 
el destino y la distancia que posteriormente sera utilizada para generar el              #
historial de las rutas.																	 #
##########################################################################################
MODIFICACIONES																			 #
##########################################################################################
*/

public interface RutaDao {

	Long guardarRuta(Long origen_id, Long destino_id ,double distancia);
	
	Map<Ruta, Integer> obtenerEstadisticaRuta(LocalDate fecha);
}
