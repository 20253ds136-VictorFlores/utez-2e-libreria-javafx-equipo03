package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DeleteConfirmControllers {
    @FXML private TextField txtIsbnTarget, txtIsbnConfirm;
    private String correcto;
    private boolean confirmado = false;

    public void inicializarDatos(String isbn) {
        this.correcto = isbn;
        this.txtIsbnTarget.setText(isbn);
    }

    public boolean isConfirmado() { return confirmado; }

    @FXML
    private void onConfirmarClick() {
        if (txtIsbnConfirm.getText().equals(correcto)) {
            confirmado = true;
            ((Stage) txtIsbnConfirm.getScene().getWindow()).close();
        } else {
            new Alert(Alert.AlertType.WARNING, "El ISBN no coincide.").show();
        }
    }

    @FXML private void onCancelarClick() { ((Stage) txtIsbnConfirm.getScene().getWindow()).close(); }
}