package model;

import java.util.Date;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Entidad que representa el historial de rutas consultadas por el usuario.                 #
Almacena información sobre qué ruta fue consultada y en qué fecha.                       #
Incluye:                                                                                  #
- Identificador del historial de consulta.                                                #
- Objeto Ruta que fue consultado.                                                         #
- Fecha exacta en la que se realizó la consulta.                                          #
Esta entidad se utiliza para generar reportes, analizar tendencias de uso                #
y para mostrar un historial al usuario final.                                             #
##########################################################################################
MODIFICACIONES																			 #
- 2025-05-26 - Creación inicial de la entidad. - EEC									 #
##########################################################################################
*/

public class HistorialRuta {
    private int idHistorial;
    private Ruta rutaConsultada;
    private Date fechaConsulta; //FechaConsulta no es la consulta del historial, sino la fecha en que se realizo el calculo de la ruta
    
    
	
	public int getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(int idHistorial) {
		this.idHistorial = idHistorial;
	}
	public Ruta getRutaConsultada() {
		return rutaConsultada;
	}
	public void setRutaConsultada(Ruta rutaConsultada) {
		this.rutaConsultada = rutaConsultada;
	}
	public Date getFechaConsulta() {
		return fechaConsulta;
	}
	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

}


