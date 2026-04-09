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

public class MainControllers {
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TextField txtBusqueda;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor, colGenero;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    private LibraryService libService;
    private ResenaService resService;
    private ObservableList<LibroModel> masterData = FXCollections.observableArrayList();

    public void setServicios(LibraryService ls, ResenaService rs) {
        this.libService = ls;
        this.resService = rs;

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        FilteredList<LibroModel> filteredData = new FilteredList<>(masterData, p -> true);
        txtBusqueda.textProperty().addListener((obs, old, val) -> {
            filteredData.setPredicate(l -> val == null || val.isEmpty() ||
                    l.getTitulo().toLowerCase().contains(val.toLowerCase()) ||
                    l.getIsbn().contains(val));
        });
        tablaLibros.setItems(filteredData);
        refrescarTabla();
    }

    public void refrescarTabla() {
        if (libService != null) masterData.setAll(libService.listar());
    }

    @FXML private void onNuevo() { abrirForm(null); }

    @FXML private void onEditar() {
        LibroModel s = tablaLibros.getSelectionModel().getSelectedItem();
        if(s != null) abrirForm(s);
    }

    @FXML
    private void onVerDetalle() {
        LibroModel s = tablaLibros.getSelectionModel().getSelectedItem();
        if(s == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            DetailControllers c = loader.getController();
            c.cargarDatos(s, resService);
            stage.show();
        } catch(Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void onEliminar() {
        LibroModel s = tablaLibros.getSelectionModel().getSelectedItem();
        if(s == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/delete-confirm-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            DeleteConfirmControllers c = loader.getController();
            c.inicializarDatos(s.getIsbn());
            stage.showAndWait();
            if(c.isConfirmado()) {
                libService.eliminar(s.getIsbn());
                refrescarTabla();
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    // --- EL NOMBRE DEBE SER EXACTAMENTE ESTE ---
    @FXML
    private void onExportarReporte() {
        try {
            libService.exportar();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reporte generado con éxito en tu carpeta de Descargas.");
            alert.show();
        } catch(Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo exportar: " + e.getMessage()).show();
        }
    }

    private void abrirForm(LibroModel b) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            FormControllers c = loader.getController();
            c.setService(libService);
            c.setMainController(this);
            if(b != null) c.cargarLibro(b);
            stage.show();
        } catch(Exception e) { e.printStackTrace(); }
    }
}