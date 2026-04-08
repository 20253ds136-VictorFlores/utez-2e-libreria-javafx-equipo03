package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;
import com.resources.integradorabiblioteca.repositories.ReportExporter;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador principal de la interfaz gráfica de la biblioteca.
 * Se encarga de gestionar la vista principal (tabla del catálogo),
 * coordinar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * abriendo las ventanas modales correspondientes, y gestionar la exportación de reportes.
 */
public class MainControllers {

    /** Tabla principal que muestra el catálogo de libros. */
    @FXML private TableView<LibroModel> tablaLibros;

    /** Columna de la tabla que muestra el ISBN del libro. */
    @FXML private TableColumn<LibroModel, String> colIsbn;

    /** Columna de la tabla que muestra el título del libro. */
    @FXML private TableColumn<LibroModel, String> colTitulo;

    /** Columna de la tabla que muestra el autor del libro. */
    @FXML private TableColumn<LibroModel, String> colAutor;

    /** Columna de la tabla que muestra el año de publicación del libro. */
    @FXML private TableColumn<LibroModel, Integer> colAnio;

    /** Columna de la tabla que muestra el género literario del libro. */
    @FXML private TableColumn<LibroModel, String> colGenero;

    /** Columna de la tabla que muestra la disponibilidad del libro. */
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    /** * Servicio de la biblioteca. Se inicializa directamente inyectando
     * un repositorio basado en archivos CSV para la persistencia de datos.
     */
    private final LibraryService service = new LibraryService(new FileRepository("data/libros.csv"));

    /**
     * Método inicializador de JavaFX. Se ejecuta automáticamente después de
     * que el archivo FXML ha sido cargado.
     * Configura el mapeo de las propiedades del modelo ({@link LibroModel})
     * a sus respectivas columnas en la tabla y carga los datos iniciales.
     */
    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colAutor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAutor()));
        colAnio.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAnio()).asObject());
        colGenero.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGenero()));
        colDisponible.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isDisponible()).asObject());

        refrescarTabla();
    }

    /**
     * Actualiza el contenido de la tabla solicitando la lista completa
     * de libros al servicio y recargando los elementos en el TableView.
     */
    public void refrescarTabla() {
        tablaLibros.getItems().setAll(service.listar());
    }

    /**
     * Manejador del evento para agregar un nuevo libro.
     * Abre la ventana de formulario (`form-view.fxml`) en modo modal,
     * inyectando el servicio y el controlador principal al controlador del formulario.
     */
    @FXML
    private void onNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Parent root = loader.load();

            FormControllers formController = loader.getController(); // plural
            formController.setService(service);
            formController.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Registrar nuevo libro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace(); // para ver la causa real en consola
            mostrarError("Error al abrir formulario: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento para editar un libro existente.
     * Verifica que haya un libro seleccionado en la tabla y, de ser así,
     * abre el formulario de edición precargando los datos del libro.
     */
    @FXML
    private void onEditar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un libro para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Parent root = loader.load();

            FormControllers formController = loader.getController();
            formController.setService(service);
            formController.setMainController(this);

            // Aquí usamos el método público en lugar de acceder a los campos privados
            formController.cargarLibro(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar libro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refrescarTabla();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir formulario: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento para eliminar un libro.
     * Pide confirmación al usuario mediante un cuadro de diálogo antes de
     * proceder a eliminar el registro a través del servicio.
     */
    @FXML
    private void onEliminar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un libro para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar el libro con ISBN " + seleccionado.getIsbn() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            service.eliminar(seleccionado.getIsbn());
            refrescarTabla();
        }
    }

    /**
     * Manejador del evento para visualizar los detalles de un libro.
     * Abre una ventana de sólo lectura (`detail-view.fxml`) mostrando
     * la información extendida del libro seleccionado.
     */
    @FXML
    private void onVerDetalle() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un libro para ver detalle.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
            Parent root = loader.load();

            DetailControllers detailController = loader.getController();
            detailController.setLibro(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Detalle del libro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir detalle: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento para exportar el catálogo actual a un archivo CSV.
     * Utiliza la clase {@link ReportExporter} y muestra un mensaje de éxito
     * o error dependiendo del resultado de la operación.
     */
    @FXML
    private void onExportarReporte() {
        try {
            ReportExporter exporter = new ReportExporter("data/reporte_catalogo.csv");
            exporter.export(service.listar());
            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Reporte exportado correctamente.", ButtonType.OK);
            ok.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al exportar reporte: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar privado para mostrar mensajes de error al usuario.
     * * @param mensaje El texto del error a mostrar en la alerta.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
}