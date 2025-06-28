package dto;

import java.time.LocalDate;

public class RutaHistorialDTO {
    private Long idHistorial;
    private LocalDate fechaConsulta;
    private Long idRuta;
    private String nombreOrigen;
    private String nombreDestino;
    private Double distancia;
	public Long getIdHistorial() {
		return idHistorial;
	}
	public void setIdHistorial(Long idHistorial) {
		this.idHistorial = idHistorial;
	}
	public LocalDate getFechaConsulta() {
		return fechaConsulta;
	}
	public void setFechaConsulta(LocalDate fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	public Long getIdRuta() {
		return idRuta;
	}
	public void setIdRuta(Long idRuta) {
		this.idRuta = idRuta;
	}
	public String getNombreOrigen() {
		return nombreOrigen;
	}
	public void setNombreOrigen(String nombreOrigen) {
		this.nombreOrigen = nombreOrigen;
	}
	public String getNombreDestino() {
		return nombreDestino;
	}
	public void setNombreDestino(String nombreDestino) {
		this.nombreDestino = nombreDestino;
	}
	public Double getDistancia() {
		return distancia;
	}
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

    // getters y setters
    
    
}
