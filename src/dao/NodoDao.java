package dao;

import model.Nodo;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Interfaz que define las operaciones básicas de acceso a datos relacionadas con la entidad#
Nodo. Permite abstraer la lógica de persistencia para mantener bajo acoplamiento y alta  #
cohesión en el diseño.																     #
Incluye métodos para:																	 #
- Guardar un nuevo nodo en la base de datos.											 #
- Obtener un nodo existente por su nombre.												 #
- Actualizar la información de un nodo.													 #
##########################################################################################
MODIFICACIONES																			 #
##########################################################################################
*/

public interface NodoDao {
	 int guardarNodo(Nodo nodo);
	    Nodo obtenerNodoPorNombre(String nombre);
void actualizarNodo(Nodo nodo);
}
