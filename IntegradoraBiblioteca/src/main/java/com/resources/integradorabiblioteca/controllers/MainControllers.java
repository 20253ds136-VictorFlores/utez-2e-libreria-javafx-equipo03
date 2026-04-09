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
import java.util.function.Predicate;

/**
 * Controlador principal que utiliza estructuras tradicionales y clases anónimas.
 */
public class MainControllers {

    // 1. DECLARACIÓN DE ATRIBUTOS (Esto es lo que faltaba)
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TextField txtBusqueda;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;

    // Estas son las variables que el compilador no encontraba:
    private LibraryService libService;
    private ResenaService resService;
    private ObservableList<LibroModel> masterData = FXCollections.observableArrayList();

    /**
     * Configura los servicios y la lógica de búsqueda sin lambdas.
     */
    public void setServicios(LibraryService ls, ResenaService rs) {
        this.libService = ls;
        this.resService = rs;

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        FilteredList<LibroModel> filteredData = new FilteredList<>(masterData, null);

        txtBusqueda.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String old, String val) {
                filteredData.setPredicate(new Predicate<LibroModel>() {
                    @Override
                    public boolean test(LibroModel libro) {
                        if (val == null || val.isEmpty()) return true;
                        String f = val.toLowerCase();
                        return libro.getTitulo().toLowerCase().contains(f) || libro.getIsbn().contains(f);
                    }
                });
            }
        });

        tablaLibros.setItems(filteredData);
        refrescarTabla();
    }

    public void refrescarTabla() {
        if (libService != null) {
            masterData.setAll(libService.listar());
        }
    }

    @FXML
    private void onExportarReporte() {
        try {
            libService.generarReporte();
            new Alert(Alert.AlertType.INFORMATION, "Reporte generado en Descargas.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    // --- Otros métodos de navegación ---
    @FXML private void onNuevo() { abrirVentana("/com/resources/integradorabiblioteca/form-view.fxml", "Nuevo Libro", null); }

    @FXML private void onVerDetalle() {
        LibroModel sel = tablaLibros.getSelectionModel().getSelectedItem();
        if (sel != null) {
            try {
                FXMLLoader l = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
                Stage st = new Stage();
                st.setTitle("Detalles");
                st.setScene(new Scene(l.load()));
                ((DetailControllers)l.getController()).cargarDatos(sel, resService);
                st.show();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void abrirVentana(String fxml, String titulo, LibroModel libro) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Stage st = new Stage();
            st.setTitle(titulo);
            st.setScene(new Scene(l.load()));
            FormControllers c = l.getController();
            c.setService(libService);
            c.setMainController(this);
            if (libro != null) c.cargarLibro(libro);
            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void onEditar() { /* Lógica similar a onNuevo pasándole el objeto seleccionado */ }
    @FXML private void onEliminar() { /* Lógica para abrir DeleteConfirmControllers */ }
}