package main;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import dto.RutaHistorialDTO;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.HistorialRuta;
import paginator.PageRender;
import service.NodoService;
import service.NodoServiceImpl;
import service.RutaService;
import service.RutaServiceImpl;
import utils.EstadisticasResultado;
import service.HistorialRutaService;
import service.HistorialRutaServiceImpl;
public class App extends Application {
	//Aqui inicializamos el constructor con la conexion
	private NodoService nodoService = new NodoServiceImpl();
	private HistorialRutaService hrService = new HistorialRutaServiceImpl();
	private RutaService rutaService = new RutaServiceImpl();
    private GridPane mainGrid;
    private Stage primaryStage;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SORCO - Sistema Optimizador de Rutas y Consultas Operativas");
        
        crearMenuPrincipalGrid();
        primaryStage.show();
    }

    private void crearMenuPrincipalGrid() {
    	//Creamos el panel
        mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(20));
        mainGrid.setHgap(20);
        mainGrid.setVgap(20);
        mainGrid.setStyle("-fx-background-color: #f4f4f4;");

        /* 1: ColumnConstraints permite crear restricciones de columnas
        *2: SetPercentWidth permite definir el ancho de las columnas
        *3: Por ultimo agregamos las 2 columnas definidas al mainGrid
        *
        */
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(colConstraints, colConstraints);

        // Título
        Label titulo = new Label("Bienvenido a SORCO");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(titulo, 2);
        GridPane.setHalignment(titulo, HPos.CENTER);
        mainGrid.add(titulo, 0, 0);

        // Tarjetas de opciones
        VBox cardRutas = crearTarjetaOpcion("Calcular rutas óptimas", "📍");
        VBox cardEstadisticasRuta = crearTarjetaOpcion("Estadísticas", "📊");
        VBox cardHistorialRuta = crearTarjetaOpcion("Historial de Rutas", "📄");
     
        VBox cardSalir = crearTarjetaOpcion("Salir", "🚪");

        // Posicionamiento en grid
        /*
         * El posicionamiento es: 0,0- 1,0 - 2,0 - 3,0
         * 						  0,1 - 1,1 - 2,1 - 3,1
         * 						  0,2 - 1,2 - 2,2 - 3,2
         * */
        mainGrid.add(cardRutas, 0, 1);
        mainGrid.add(cardHistorialRuta, 1, 1);
        mainGrid.add(cardEstadisticasRuta, 0, 2);
        mainGrid.add(cardSalir, 1, 2);

        // Eventos
        cardRutas.setOnMouseClicked(e -> mostrarCalculadorRutas());
        cardHistorialRuta.setOnMouseClicked(e -> mostrarHistorialRuta());
        cardEstadisticasRuta.setOnMouseClicked(e -> mostrarEstadisticas());
        cardSalir.setOnMouseClicked(e -> primaryStage.close());

        mainScene = new Scene(mainGrid, 1000, 600);
        primaryStage.setScene(mainScene);
    }

    	
    private VBox crearTarjetaOpcion(String titulo, String icono) {
        VBox tarjeta = new VBox(10);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);"
                + "-fx-cursor: hand;");
        
        Label lblIcono = new Label(icono);
        lblIcono.setStyle("-fx-font-size: 32px;");
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

        tarjeta.getChildren().addAll(lblIcono, lblTitulo);
        return tarjeta;
    }

    //Modulo de calculo de ruta, tenemos los inputs
    private void mostrarCalculadorRutas() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #f4f4f4;");

        // Título
        Label titulo = new Label("Calculador de Rutas Óptimas");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setColumnSpan(titulo, 2);
        grid.add(titulo, 0, 0);

        // Campos de entrada
        TextField txtOrigen = new TextField();
        TextField txtDestino = new TextField();
        TextField resultado = new TextField();
        txtDestino.setDisable(true);
        
        Button btnBuscarOrigen = new Button("Validar Origen");
        Button btnBuscarDestino = new Button("Validar Destino");
        btnBuscarDestino.setDisable(true);
        
        Button btnCalcular = new Button("Calcular Ruta");
        btnCalcular.setDisable(true);
        
       
        // Configurar grid
        grid.add(new Label("Origen:"), 0, 1);
        grid.add(txtOrigen, 1, 1);
        grid.add(btnBuscarOrigen, 2, 1);
        
        grid.add(new Label("Destino:"), 0, 2);
        grid.add(txtDestino, 1, 2);
        grid.add(btnBuscarDestino, 2, 2);
        
        grid.add(btnCalcular, 1, 3);
       

        // Lógica de validación
        btnBuscarOrigen.setOnAction(e -> {
            if(validarCampo(txtOrigen)) {
            	  // Llamar al servicio para buscar y guardar el origen
                nodoService.consultarCoordenadasYGuardar(txtOrigen.getText(), true);
                
                // Deshabilitar el campo de origen y su botón
                txtOrigen.setDisable(true);
                btnBuscarOrigen.setDisable(true);
                
                // Habilitar el campo de destino y su botón
                txtDestino.setDisable(false);
                btnBuscarDestino.setDisable(false); 
            }
        });

        btnBuscarDestino.setOnAction(e -> {
            if(validarCampo(txtDestino)) {
            	 // Deshabilitar el campo de destino y su botón
                txtDestino.setDisable(true);
                btnBuscarDestino.setDisable(true);
                
                // Activar el botón para calcular la ruta
                btnCalcular.setDisable(false);
            
                nodoService.consultarCoordenadasYGuardar(txtDestino.getText(), false);
            }
        });

        btnCalcular.setOnAction(e -> {
        	 String origen = txtOrigen.getText();
        	    String destino = txtDestino.getText();

        	    // Llamar al método que coordina todo el flujo de cálculo de la ruta
        	    nodoService.calcularRutaDesdeBD(origen, destino);
        	    
        });

        agregarEscenaConVolver(grid);
    }
 
    private void mostrarHistorialRuta() {
        int tamañoPagina = 10;
        SimpleIntegerProperty paginaActual = new SimpleIntegerProperty(1);

        // Contenedor principal
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setVgap(15);

        Label titulo = new Label("Historial de Rutas");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
     // Campo de búsqueda
        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Buscar por ciudad de origen o destino..");
        campoBusqueda.setPrefWidth(400);
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Buscar por fecha");

        campoBusqueda.setStyle("-fx-font-size: 14px;");
        // Tabla
        TableView<HistorialRuta> tabla = new TableView<>();
        tabla.setPrefWidth(800);

        // Columnas
        TableColumn<HistorialRuta, String> colOrigen = new TableColumn<>("Origen");
        colOrigen.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRutaConsultada().getOrigen().getNombre()));

        TableColumn<HistorialRuta, String> colDestino = new TableColumn<>("Destino");
        colDestino.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRutaConsultada().getDestino().getNombre()));

        TableColumn<HistorialRuta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFechaConsulta().toString()));

        tabla.getColumns().addAll(colOrigen, colDestino, colFecha);

        // Etiqueta de paginación
        Label etiquetaPagina = new Label();

        // Botones de paginación
        Button btnAnterior = new Button("Anterior");
        Button btnSiguiente = new Button("Siguiente");
        Button botonBuscar = new Button("Buscar");
        HBox paginador = new HBox(10, btnAnterior, etiquetaPagina, btnSiguiente);
        paginador.setAlignment(Pos.CENTER);

        // Actualiza la tabla según la página actual
        Runnable actualizarTabla = () -> {
            PageRender<HistorialRuta> page = hrService.consultarTodasLasRutas(paginaActual.get(), tamañoPagina);
            
            tabla.getItems().setAll(page.getContenidoPaginaActual());
            etiquetaPagina.setText("Página " + paginaActual.get() + " de " + page.getTotalPaginas());

            btnAnterior.setDisable(paginaActual.get() <= 1);
            btnSiguiente.setDisable(paginaActual.get() >= page.getTotalPaginas());
        };

        btnAnterior.setOnAction(e -> {
            paginaActual.set(paginaActual.get() - 1);
            actualizarTabla.run();
        });

        btnSiguiente.setOnAction(e -> {
            paginaActual.set(paginaActual.get() + 1);	
            actualizarTabla.run();
        });
        botonBuscar.setOnAction(e -> {
            String textoBusqueda = campoBusqueda.getText().trim();
            LocalDate fechaSeleccionada = datePicker.getValue();

            // Usar null si no hay valor
            String nombre = textoBusqueda.isEmpty() ? null : textoBusqueda;
            Date fechaConsulta = (fechaSeleccionada != null) ? Date.valueOf(fechaSeleccionada) : null;

            // Reiniciar paginación
            paginaActual.set(1);
            
            // Usar el servicio de búsqueda
            PageRender<HistorialRuta> page = hrService.consultarPorNombreOFecha(
                nombre, 
                fechaConsulta, 
                paginaActual.get(), 
                tamañoPagina
            );
            
            // Actualizar tabla y paginación
            tabla.getItems().setAll(page.getContenidoPaginaActual());
            etiquetaPagina.setText("Página " + paginaActual.get() + " de " + page.getTotalPaginas());
            
            // Controlar botones de paginación
            btnAnterior.setDisable(paginaActual.get() <= 1);
            btnSiguiente.setDisable(paginaActual.get() >= page.getTotalPaginas());
        });
        actualizarTabla.run(); // Primera carga
        HBox buscador = new HBox(10, campoBusqueda, datePicker, botonBuscar);
        // Agregar componentes al layout
        grid.add(titulo, 0, 0);
        grid.add(buscador, 0, 1);
        grid.add(tabla, 0, 2);
        
        grid.add(paginador, 0, 3);

        agregarEscenaConVolver(grid);
    }




    private void mostrarEstadisticas() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        
        Button exportarPDF = new Button("exportar PDF");
        Label titulo = new Label("TOP CIUDADES MAS CONSULTADAS EN EL DIA");
        grid.add(titulo, 0, 0);
       

        RutaService service = new RutaServiceImpl();
        LocalDate fecha = LocalDate.now(); // Usar fecha actual

        EstadisticasResultado resultado = service.obtenerEstadisticasRuta(fecha);

        // Mostrar gráfico de ciudades
        if (resultado.getCiudades() == null || resultado.getCiudades().isEmpty()) {
            Label noData = new Label("No se encontraron consultas realizadas para mostrar datos el dia de hoy");
            noData.setStyle("-fx-text-fill: red;");
            grid.add(noData, 0, 1);
        } else {
            mostrarGraficoCiudades(resultado.getCiudades(), resultado.getFrecuencias(), grid);
        }

        // Mostrar ciudad más frecuente
        Label resumen = new Label("Ciudad más consultada: " + resultado.getCiudadMasFrecuente());
        
        exportarPDF.setOnAction(e-> {
        		
        	 rutaService.exportarPDF("reporte_rutas.pdf");
        });
        
        grid.add(resumen, 0, 2);
        grid.add(exportarPDF, 0, 3);
        agregarEscenaConVolver(grid);
    }

    private void mostrarGraficoCiudades(List<String> ciudades, List<Integer> frecuencias, GridPane grid) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        yAxis.setTickUnit(1);

        xAxis.setLabel("Ciudades");
        yAxis.setLabel("Frecuencia de consultas");
        xAxis.setTickLabelRotation(30);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top " + ciudades.size() + " Ciudades");
        barChart.setLegendVisible(false);
        barChart.setCategoryGap(20);
        barChart.setBarGap(5);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < ciudades.size(); i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(ciudades.get(i), frecuencias.get(i));
            series.getData().add(data);
            
            // Añadir tooltip
            Tooltip tooltip = new Tooltip(ciudades.get(i) + ": " + frecuencias.get(i) + " consultas");
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip.install(newNode, tooltip);
                    newNode.setOnMouseEntered(event -> newNode.setStyle("-fx-bar-fill: #3498db;"));
                    newNode.setOnMouseExited(event -> newNode.setStyle("-fx-bar-fill: #2c3e50;"));
                }
            });
        }

        barChart.getData().add(series);
        barChart.setStyle("-fx-bar-fill: #2c3e50;"); // Color de barras
        
        // Limitar a las top 10 ciudades si hay muchas
        if (ciudades.size() > 10) {
            barChart.setTitle("Top 10 Ciudades");
        }

        grid.add(barChart, 0, 1);
    }

    private void agregarEscenaConVolver(GridPane grid) {
        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setStyle("-fx-background-color: #666; -fx-text-fill: white;");
        btnVolver.setOnAction(e -> primaryStage.setScene(mainScene));
        
        VBox contenedor = new VBox(20, grid, btnVolver);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(20));
        
        Scene nuevaEscena = new Scene(contenedor, 1000, 600);
        primaryStage.setScene(nuevaEscena);
    }

    private boolean validarCampo(TextField campo) {
        if(campo.getText().isEmpty()) {
            mostrarAlerta("Error", "El campo no puede estar vacío");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
