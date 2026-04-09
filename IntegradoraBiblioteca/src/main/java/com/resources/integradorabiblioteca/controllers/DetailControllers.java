package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;

public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;
    @FXML private TextField txtNuevaEstrella, txtNuevaResena;
    @FXML private TableView<ResenaRow> tableResenas;
    @FXML private TableColumn<ResenaRow, String> colEstrellas, colComentario;

    private Libro libroSeleccionado;
    private ResenaService service;

    @FXML
    public void initialize() {
        // Configura las columnas de la tabla
        colEstrellas.setCellValueFactory(new PropertyValueFactory<>("estrellas"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
    }

    public void cargarDatos(Libro libro, ResenaService resenaService) {
        this.libroSeleccionado = libro;
        this.service = resenaService;

        if (libro != null) {
            lblIsbn.setText("ISBN: " + libro.getIsbn());
            lblTitulo.setText("Título: " + libro.getTitulo());
            lblAutor.setText("Autor: " + libro.getAutor());
            lblAnio.setText("Año: " + libro.getAnio());
            lblGenero.setText("Género: " + libro.getGenero());
            lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));
            actualizarTabla();
        }
    }

    @FXML
    private void onPublicarClick() {
        String estrellas = txtNuevaEstrella.getText().trim();
        String comentario = txtNuevaResena.getText().trim();

        if (!estrellas.isEmpty() && !comentario.isEmpty()) {
            // Guardamos con formato "Estrellas|Comentario"
            service.agregarResena(libroSeleccionado.getIsbn(), estrellas + "|" + comentario);
            txtNuevaEstrella.clear();
            txtNuevaResena.clear();
            actualizarTabla();
        }
    }

    private void actualizarTabla() {
        List<String> raw = service.obtenerResenasPorIsbn(libroSeleccionado.getIsbn());
        ObservableList<ResenaRow> data = FXCollections.observableArrayList();
        for (String s : raw) {
            String[] p = s.split("\\|");
            data.add(new ResenaRow(p[0], p.length > 1 ? p[1] : ""));
        }
        tableResenas.setItems(data);
    }

    @FXML
    private void onRegresarClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Clase para representar una fila en la tabla
    public static class ResenaRow {
        private String estrellas, comentario;
        public ResenaRow(String e, String c) { this.estrellas = e; this.comentario = c; }
        public String getEstrellas() { return estrellas; }
        public String getComentario() { return comentario; }
    }
}