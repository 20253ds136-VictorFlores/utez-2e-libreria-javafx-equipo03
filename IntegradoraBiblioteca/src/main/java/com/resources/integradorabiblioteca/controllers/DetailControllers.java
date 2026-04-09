package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.UUID;

public class DetailControllers {
    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;
    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;
    @FXML private TableColumn<ResenaModel, String> colComentario;
    @FXML private TextField txtCalificacion, txtComentario;

    private ResenaService resService;
    private LibroModel libro;

    public void cargarDatos(LibroModel b, ResenaService rs) {
        this.libro = b; this.resService = rs;
        lblIsbn.setText(b.getIsbn()); lblTitulo.setText(b.getTitulo()); lblAutor.setText(b.getAutor());
        lblAnio.setText(String.valueOf(b.getAnio())); lblGenero.setText(b.getGenero());
        lblDisponible.setText(b.isDisponible()?"Si":"No");

        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        refrescarResenas();
    }

    @FXML private void onAgregarResena() {
        resService.agregar(new ResenaModel(UUID.randomUUID().toString(), libro.getIsbn(), Integer.parseInt(txtCalificacion.getText()), txtComentario.getText()));
        refrescarResenas();
        txtCalificacion.clear(); txtComentario.clear();
    }

    private void refrescarResenas() { tablaResenas.setItems(FXCollections.observableArrayList(resService.listarPorLibro(libro.getIsbn()))); }
    @FXML private void onRegresar() { ((javafx.stage.Stage)lblIsbn.getScene().getWindow()).close(); }
}