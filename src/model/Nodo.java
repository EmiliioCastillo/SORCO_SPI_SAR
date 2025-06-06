package model;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Entidad que representa un nodo geográfico dentro del sistema SORCO.                      #
Un nodo puede ser una ciudad, punto de partida, destino o punto intermedio.              #
Contiene:                                                                                 #
- ID único del nodo.                                                                      #
- Nombre identificatorio (por ejemplo: nombre de la ciudad o punto de interés).          #
- Coordenadas geográficas (latitud y longitud) para su ubicación precisa.                #
Se utiliza para construir rutas, graficar mapas y calcular distancias.                   #
##########################################################################################
MODIFICACIONES																			 #
- 2025-05-26 - Creación inicial de la entidad. - EEC									 #
##########################################################################################
*/

public class Nodo {
	private Long id_nodo;
    private String nombre; // Nombre del nodo, puede ser una ciudad o un punto de interés
    private double latitud;
    private double longitud;

    
    public Nodo() {
    	
    }
    
    public Nodo(Long id_nodo, String nombre, double latitud, double longitud) {
		
		this.id_nodo = id_nodo;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public Nodo(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

   


    public Long getId_nodo() {
		return id_nodo;
	}
	public void setId_nodo(Long id_nodo) {
		this.id_nodo = id_nodo;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    @Override
    public String toString() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return String.format("Nodo [latitud=%.7f, longitud=%.7f]", latitud, longitud);
        } else {
            return String.format("Nodo [nombre=%s, latitud=%.7f, longitud=%.7f]", nombre, latitud, longitud);
        }
    }


   
}
