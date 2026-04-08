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

public class MainControllers {
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TableColumn<LibroModel, String> colIsbn;
    @FXML private TableColumn<LibroModel, String> colTitulo;
    @FXML private TableColumn<LibroModel, String> colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, String> colGenero;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    private final LibraryService service = new LibraryService(new FileRepository("data/libros.csv"));

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

    public void refrescarTabla() {
        tablaLibros.getItems().setAll(service.listar());
    }

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

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
}