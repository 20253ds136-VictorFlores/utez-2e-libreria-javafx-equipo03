package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador logico destinado puramente a proyectar atributos inmutables hacia controles de Label,
 * en representacion de una ficha de solo-lectura sobre un libro especifico.
 */
public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    /**
     * Mapea y disemina los datos inyectados de la entidad a traves de la interfaz de la ventana.
     * * @param libro Contexto base del cual se extraera la informacion a ser plasmada.
     */
    public void cargarDatos(Libro libro) {
        lblIsbn.setText(libro.getIsbn());
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        lblAnio.setText(String.valueOf(libro.getAnio()));
        lblGenero.setText(libro.getGenero());
        lblDisponible.setText(libro.isDisponible() ? "Si" : "No");
    }

    /**
     * Detiene la proyeccion de la interfaz al solicitar el ocultamiento del Stage contenedor.
     */
    @FXML
    private void onBackClick() {
        ((Stage) lblIsbn.getScene().getWindow()).close();
    }
}