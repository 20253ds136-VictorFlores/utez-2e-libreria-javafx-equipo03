package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormControllers {
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers mainController;

    public void setService(LibraryService service) { this.service = service; }
    public void setMainController(MainControllers mainController) { this.mainController = mainController; }

    // Método público para precargar datos
    public void cargarLibro(LibroModel libro) {
        txtIsbn.setText(libro.getIsbn());
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio()));
        txtGenero.setText(libro.getGenero());
        chkDisponible.setSelected(libro.isDisponible());
    }

    @FXML
    private void onGuardar() {
        try {
            LibroModel libro = new LibroModel(
                    txtIsbn.getText(),
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    Integer.parseInt(txtAnio.getText()),
                    txtGenero.getText(),
                    chkDisponible.isSelected()
            );

            if (!service.agregar(libro)) {
                mostrarError("El ISBN ya existe.");
                return;
            }

            mainController.refrescarTabla();
            cerrarVentana();

        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
}