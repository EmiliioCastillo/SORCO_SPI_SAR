package paginator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageRender<T> {
    private List<T> contenidoPaginaActual; // Elementos de la página actual
    private int tamañoPagina;              // Elementos por página
    private int pagActual;                 // Página actual (base 1)
    private int totalPaginas;              // Total de páginas = elementos en total / elementos por pagina
    private int totalElementos;            // Total de elementos en todas las páginas
    private List<PageItem> paginas;        // Para representar los botones de paginación

    
    public PageRender(List<T> contenidoPaginaActual, int tamañoPagina, int pagActual, int totalElementos) {
        if (tamañoPagina <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser positivo");
        }
        if (totalElementos < 0) {
            throw new IllegalArgumentException("El total de elementos no puede ser negativo");
        }
        //Si hay elementos se le asigna la lista sino retorna un emptyList
        this.contenidoPaginaActual = contenidoPaginaActual != null ? contenidoPaginaActual : Collections.emptyList();
        this.tamañoPagina = tamañoPagina;
        this.totalElementos = totalElementos;
        
        //TotalPaginas = el calculo de las paginas que seria
        //si hay 10 elementos en total / 5 elementos por paginas = 2 paginas
        this.totalPaginas = calcularTotalPaginas(totalElementos, tamañoPagina);
        //a pagina actual se le asigna el minimo entre la pagina actual y el total de paginas
        this.pagActual = validarPagActual(pagActual, totalPaginas);
        this.paginas = new ArrayList<>();
        generarPaginas();
    }

    private int calcularTotalPaginas(int totalElementos, int tamañoPagina) {
        if (totalElementos == 0) return 0;
        return (int) Math.ceil((double) totalElementos / tamañoPagina);
    }

    private int validarPagActual(int pagActual, int totalPaginas) {
        if (pagActual < 1) return 1;
        if (totalPaginas == 0) return 0; // No hay páginas si no hay elementos
        return Math.min(pagActual, totalPaginas);
    }

    private void generarPaginas() {
        if (totalPaginas == 0) return;
        //Desde donde se va a generar la pagina o sea para atras si estamos en la 3 se muestra 2 y 1 
        int desde = Math.max(1, pagActual - 2);
        //Hasta donde va a ir, o sea si hay 10 paginas se va a mostrar si estamos en la 2 se va a mostrar 3 y 4 y la flecha
        int hasta = Math.min(totalPaginas, pagActual + 2);

        for (int i = desde; i <= hasta; i++) {
            paginas.add(new PageItem(i, i == pagActual));
        }
    }

    // Getters
    public List<T> getContenidoPaginaActual() {
        return contenidoPaginaActual; // Ya es la página actual
    }

    public boolean isHasPrevious() {
        return pagActual > 1;
    }

    public boolean isHasNext() {
        return pagActual < totalPaginas;
    }

    public int getPagActual() {
        return pagActual;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }
}
    


