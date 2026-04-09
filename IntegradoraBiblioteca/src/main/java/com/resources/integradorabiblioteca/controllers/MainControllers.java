package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;

/**
 * Controlador principal de la interfaz de usuario.
 * Gestiona la tabla de libros, la búsqueda reactiva y la navegación entre ventanas.
 */
public class MainControllers {
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TextField txtBusqueda;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;

    private LibraryService libService;
    private ResenaService resService;
    private ObservableList<LibroModel> masterData = FXCollections.observableArrayList();

    /**
     * Inyecta los servicios necesarios, configura el mapeo de las columnas con los
     * atributos del modelo y establece el filtro de búsqueda reactivo en la tabla.
     *
     * @param ls Instancia del servicio de gestión de libros.
     * @param rs Instancia del servicio de gestión de reseñas.
     */
    public void setServicios(LibraryService ls, ResenaService rs) {
        this.libService = ls;
        this.resService = rs;

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        FilteredList<LibroModel> filteredData = new FilteredList<>(masterData, p -> true);
        txtBusqueda.textProperty().addListener((obs, old, val) -> {
            filteredData.setPredicate(l -> val == null || val.isEmpty() ||
                    l.getTitulo().toLowerCase().contains(val.toLowerCase()) ||
                    l.getIsbn().contains(val));
        });
        tablaLibros.setItems(filteredData);
        refrescarTabla();
    }

    /**
     * Sincroniza los datos de la tabla visual con la lista actualizada
     * proveniente del servicio de inventario.
     */
    public void refrescarTabla() {
        if (libService != null) masterData.setAll(libService.listar());
    }

    /**
     * Maneja el evento del botón para registrar un nuevo libro.
     * Abre el formulario de registro en blanco.
     */
    @FXML
    private void onNuevo() {
        abrirVentanaFormulario(null, "Registrar Nuevo Libro");
    }

    /**
     * Maneja el evento del botón para editar.
     * Carga los datos del libro seleccionado en la tabla dentro del formulario.
     */
    @FXML
    private void onEditar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirVentanaFormulario(seleccionado, "Editar Libro");
        } else {
            mostrarAlerta("Atención", "Por favor, selecciona un libro de la tabla.");
        }
    }

    /**
     * Maneja el evento del botón para eliminar.
     * Despliega una ventana modal de confirmación antes de proceder con la eliminación.
     */
    @FXML
    private void onEliminar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/delete-confirm-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Confirmar Eliminación");
            stage.setScene(new Scene(loader.load()));

            DeleteConfirmControllers controller = loader.getController();
            controller.inicializarDatos(seleccionado.getIsbn());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isConfirmado()) {
                libService.eliminar(seleccionado.getIsbn());
                refrescarTabla();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Maneja el evento del botón de detalles.
     * Abre una ventana con la información completa del libro y sus reseñas correspondientes.
     */
    @FXML
    private void onVerDetalle() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Detalles: " + seleccionado.getTitulo());
            stage.setScene(new Scene(loader.load()));

            DetailControllers controller = loader.getController();
            controller.cargarDatos(seleccionado, resService);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Solicita al servicio la generación de un reporte físico en TXT y
     * notifica al usuario el resultado de la operación.
     */
    @FXML
    private void onExportarReporte() {
        try {
            libService.generarReporte();
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Reporte Generado");
            alerta.setHeaderText(null);
            alerta.setContentText("Se ha generado 'inventario_biblioteca.txt' en tu carpeta de Descargas.");
            alerta.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al generar el TXT: " + e.getMessage()).show();
        }
    }

    /**
     * Método auxiliar para cargar y mostrar la ventana del formulario.
     *
     * @param libro         Objeto LibroModel a cargar (null si es un nuevo registro).
     * @param tituloVentana Título a mostrar en la barra superior de la ventana.
     */
    private void abrirVentanaFormulario(LibroModel libro, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(loader.load()));

            FormControllers controller = loader.getController();
            controller.setService(libService);
            controller.setMainController(this);
            if (libro != null) controller.cargarLibro(libro);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Método auxiliar para desplegar cuadros de diálogo informativos en pantalla.
     *
     * @param titulo  Título de la alerta.
     * @param mensaje Contenido o cuerpo del mensaje a mostrar.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}