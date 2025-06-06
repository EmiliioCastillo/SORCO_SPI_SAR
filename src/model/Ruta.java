package model;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Entidad que representa una ruta entre dos nodos dentro del sistema SORCO.				 #
Una ruta está compuesta por:															 #
- Nodo de origen.																		 #
- Nodo de destino.																		 #
- Distancia en kilómetros entre ambos nodos.											 #
Esta clase se utiliza para almacenar y manipular las rutas calculadas o consultadas,     #
sirviendo como estructura base para los algoritmos de optimización de caminos.           #
##########################################################################################
MODIFICACIONES																			 #
- 2025-05-26 - Creación inicial de la entidad. - EEC									 #
##########################################################################################
*/

public class Ruta {
	private Long id_ruta;
    private Nodo origen;  // Nodo de origen
    private Nodo destino; // Nodo de destino
    private double distancia; // Distancia entre el origen y el destino

    public Ruta() {
		super();
	}

	// Constructor
    public Ruta(Nodo origen, Nodo destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public Long getId_ruta() {
		return id_ruta;
	}

	public void setId_ruta(Long id_ruta) {
		this.id_ruta = id_ruta;
	}

	// Getters y Setters
    public Nodo getOrigen() {
        return origen;
    }

    public void setOrigen(Nodo origen) {
        this.origen = origen;
    }

    public Nodo getDestino() {
        return destino;
    }

    public void setDestino(Nodo destino) {
        this.destino = destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

	@Override
	public String toString() {
		return "Ruta [origen=" + origen + ", destino=" + destino + ", distancia=" + distancia + "]";
	}

  

}
