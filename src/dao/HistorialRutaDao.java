package dao;

import java.util.Date;
import java.util.List;

import model.HistorialRuta;
import paginator.PageRender;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Interfaz que define las operaciones relacionadas al historial de rutas consultadas.		 #
Incluye métodos para paginar resultados, filtrar por nombre o fecha, y guardar registros.#
Utiliza el modelo PageRender para paginación manual sin frameworks externos.			 #
##########################################################################################
MODIFICACIONES																			 #
- [Fecha] - [Descripción del cambio] - [Autor]											 #
##########################################################################################
*/
public interface HistorialRutaDao {

	PageRender<HistorialRuta> consultarHistorialRutas(int pagina, int tamañoPagina);
	PageRender<HistorialRuta> consultarPorNombreOFecha(String nombre, Date fechaConsulta, int pagina, int tamañoPagina);
	void guardarHistorial(Long idRuta);
}
	