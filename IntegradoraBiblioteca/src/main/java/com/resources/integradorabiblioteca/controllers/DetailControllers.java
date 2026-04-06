package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador de la vista de detalles.
 * Muestra información de un libro seleccionado.
 */
public class DetailControllers {
    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    /**
     * Carga los datos del libro en la vista.
     * @param libro libro a mostrar
     */
    public void cargarDatos(Libro libro) {
        lblIsbn.setText(libro.getIsbn());
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        lblAnio.setText(String.valueOf(libro.getAnio()));
        lblGenero.setText(libro.getGenero());
        lblDisponible.setText(libro.isDisponible() ? "Sí" : "No");
    }

    /**
     * Acción para cerrar la ventana de detalles.
     */
    @FXML
    private void onBackClick() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }
}