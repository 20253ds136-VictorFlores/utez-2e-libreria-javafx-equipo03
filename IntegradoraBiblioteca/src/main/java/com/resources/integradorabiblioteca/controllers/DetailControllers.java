package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailControllers {
    @FXML private Label lblIsbn;
    @FXML private Label lblTitulo;
    @FXML private Label lblAutor;
    @FXML private Label lblAnio;
    @FXML private Label lblGenero;
    @FXML private Label lblDisponible;

    public void setLibro(LibroModel libro) {
        lblIsbn.setText("ISBN: " + libro.getIsbn());
        lblTitulo.setText("Título: " + libro.getTitulo());
        lblAutor.setText("Autor: " + libro.getAutor());
        lblAnio.setText("Año: " + libro.getAnio());
        lblGenero.setText("Género: " + libro.getGenero());
        lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));
    }

    @FXML
    private void onRegresar() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }
}