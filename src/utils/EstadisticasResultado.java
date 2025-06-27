package utils;

import java.util.Collections;
import java.util.List;
//Clase que sirve para armar las estadisticas y devolverlas en la interfaz, es una clase auxiliar
public class EstadisticasResultado {
    private List<String> ciudades;       // Lista de nombres de ciudades
    private List<Integer> frecuencias;  // Frecuencia de cada ciudad
    private String ciudadMasFrecuente;   // La ciudad con mayor frecuencia
    
    public EstadisticasResultado(List<String> ciudades, 
                                 List<Integer> frecuencias, 
                                 String ciudadMasFrecuente) {
        this.ciudades = ciudades != null ? ciudades : Collections.emptyList();
        this.frecuencias = frecuencias != null ? frecuencias : Collections.emptyList();
        this.ciudadMasFrecuente = ciudadMasFrecuente != null ? ciudadMasFrecuente : "";
    }
    
    // Getters
    public List<String> getCiudades() { return ciudades; }
    public List<Integer> getFrecuencias() { return frecuencias; }
    public String getCiudadMasFrecuente() { return ciudadMasFrecuente; }
}



	
    
    

