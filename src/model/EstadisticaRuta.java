package model;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio.																 #
Descripción:																			 #
Entidad que representa una estadística asociada a una ruta consultada.                   #
Se utiliza para almacenar y manipular información sobre la cantidad de veces que una     #
ruta específica fue consultada dentro del sistema.                                        #
Incluye:                                                                                  #
- Identificador de la estadística.                                                        #
- Objeto Ruta al que pertenece la estadística.                                            #
              
##########################################################################################
MODIFICACIONES																			 #
- 2025-05-26 - Creación inicial de la entidad. - EEC									 #
##########################################################################################
*/


public class EstadisticaRuta {
		private Long idEstadistica;
		private Ruta ruta;
	  
		public Ruta getRuta() {
			return ruta;
		}
		public void setRuta(Ruta ruta) {
			this.ruta = ruta;
		}
	

}
