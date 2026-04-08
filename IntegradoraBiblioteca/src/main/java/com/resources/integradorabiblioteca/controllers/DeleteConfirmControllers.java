package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteConfirmControllers {

    @FXML private TextField txtIsbnTarget;
    @FXML private TextField txtIsbnConfirm;

    private String isbnCorrecto;
    private boolean confirmado = false;

    public void inicializarDatos(String isbn) {
        this.isbnCorrecto = isbn;
        txtIsbnTarget.setText(isbn);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void onConfirmarClick() {
        String input = txtIsbnConfirm.getText();

        if (input == null || input.trim().isEmpty()) {
            mostrarAlerta("Error", "El campo no puede estar vacío. Pegue el ISBN.");
            return;
        }

        if (input.trim().equals(isbnCorrecto)) {
            confirmado = true;
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "El ISBN no coincide con el del libro seleccionado.");
        }
    }

    @FXML
    private void onCancelarClick() {
        confirmado = false;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbnConfirm.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
