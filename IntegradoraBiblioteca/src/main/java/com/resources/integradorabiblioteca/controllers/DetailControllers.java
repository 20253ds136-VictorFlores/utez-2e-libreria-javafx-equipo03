package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DetailControllers {
    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;
    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, String> colUsuario, colComentario;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;

    @FXML private TextField txtCalificacion, txtComentario;

    private LibroModel libroActual;
    private ResenaService resenaService;

    public void cargarDatos(LibroModel libro, ResenaService resenaService) {
        this.libroActual = libro;
        this.resenaService = resenaService;

        // Llenar etiquetas
        lblIsbn.setText("ISBN: " + libro.getIsbn());
        lblTitulo.setText("Título: " + libro.getTitulo());
        lblAutor.setText("Autor: " + libro.getAutor());
        lblAnio.setText("Año: " + libro.getAnio());
        lblGenero.setText("Género: " + libro.getGenero());
        lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));

        // Configurar tabla
        colUsuario.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIdUsuario()));
        colCalificacion.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getCalificacion()).asObject());
        colComentario.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getComentario()));

        actualizarTabla();
    }

    @FXML
    private void onAgregarResena() {
        try {
            int estrellas = Integer.parseInt(txtCalificacion.getText());
            String texto = txtComentario.getText();

            if (estrellas < 1 || estrellas > 5 || texto.isEmpty()) {
                mostrarAlerta("Datos inválidos. Calificación 1-5 y comentario no vacío.");
                return;
            }

            ResenaModel nueva = new ResenaModel(
                    "R-" + System.currentTimeMillis(),
                    libroActual.getIsbn(),
                    "Invitado",
                    estrellas,
                    texto
            );

            resenaService.agregar(nueva);
            txtCalificacion.clear();
            txtComentario.clear();
            actualizarTabla();

        } catch (Exception e) {
            mostrarAlerta("Error: La calificación debe ser un número.");
        }
    }

    private void actualizarTabla() {
        tablaResenas.setItems(FXCollections.observableArrayList(resenaService.listarPorLibro(libroActual.getIsbn())));
    }

    @FXML private void onRegresar() { ((Stage) lblIsbn.getScene().getWindow()).close(); }

    private void mostrarAlerta(String m) { new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}