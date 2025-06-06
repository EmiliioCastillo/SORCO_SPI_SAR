package paginator;
/*
##########################################################################################
Autor: Emilio Eduardo Castillo															 #
Universidad: Siglo 21                                                   				 #
Proyecto/Procliente: SORCO (Sistema de Optimización de Rutas y Consultas Operativas)     #
Fecha de creación: 2025-05-26															 #
Marca: Desarrollo propio, sin frameworks externos.										 #
Descripción:																			 #
Clase utilizada para representar un ítem de paginación dentro de la interfaz de usuario. #
Cada PageItem representa un número de página con un indicador booleano que señala si     #
esa página es la actualmente seleccionada.											     #
Se utiliza comúnmente en la generación de controles de navegación entre páginas de datos.#
##########################################################################################
MODIFICACIONES																			 #
- 2025-05-26 - Creación inicial de la clase. - EEC										 #
##########################################################################################
*/

public class PageItem {
	  private int numero; //Numero de la pagina
	    private boolean actual; //Indica si esa página es la actual seleccionada en la paginación (true).

	    public PageItem(int numero, boolean actual) {
	        this.numero = numero;
	        this.actual = actual;
	    }

	    public int getNumero() {
	        return numero;
	    }

	    public void setNumero(int numero) {
	        this.numero = numero;
	    }

	    public boolean isActual() {
	        return actual;
	    }

	    public void setActual(boolean actual) {
	        this.actual = actual;
	    }
}
