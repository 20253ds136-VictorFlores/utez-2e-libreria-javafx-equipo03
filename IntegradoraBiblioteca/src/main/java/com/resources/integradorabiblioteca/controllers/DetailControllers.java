package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.UUID;

/**
 * Controlador de la vista de detalles del libro.
 * Gestiona la visualización de la ficha técnica completa y la sección interactiva
 * de reseñas, permitiendo a los usuarios consultar y añadir valoraciones.
 */
public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;
    @FXML private TableColumn<ResenaModel, String> colComentario;

    @FXML private TextField txtCalificacion, txtComentario;

    /** Servicio encargado de la lógica y persistencia de las reseñas. */
    private ResenaService resService;

    /** Modelo del libro que se está visualizando actualmente. */
    private LibroModel libro;

    /**
     * Inicializa la vista con la información del libro y configura el servicio de reseñas.
     * Establece el mapeo de las columnas de la tabla y carga las reseñas existentes.
     *
     * @param b  Modelo del libro seleccionado desde la pantalla principal.
     * @param rs Instancia del servicio de reseñas inyectada para la gestión de datos.
     */
    public void cargarDatos(LibroModel b, ResenaService rs) {
        this.libro = b;
        this.resService = rs;

        lblIsbn.setText(b.getIsbn());
        lblTitulo.setText(b.getTitulo());
        lblAutor.setText(b.getAutor());
        lblAnio.setText(String.valueOf(b.getAnio()));
        lblGenero.setText(b.getGenero());
        lblDisponible.setText(b.isDisponible() ? "Disponible" : "No disponible");

        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        refrescarResenas();
    }

    /**
     * Gestiona el evento de añadir una nueva valoración.
     * Genera un identificador único (UUID) para la reseña y la vincula al ISBN
     * del libro actual antes de solicitar su persistencia al servicio.
     */
    @FXML
    private void onAgregarResena() {
        try {
            ResenaModel nuevaResena = new ResenaModel(
                    UUID.randomUUID().toString(),
                    libro.getIsbn(),
                    Integer.parseInt(txtCalificacion.getText()),
                    txtComentario.getText()
            );

            resService.agregar(nuevaResena);
            refrescarResenas();

            txtCalificacion.clear();
            txtComentario.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "La calificación debe ser un valor numérico.").show();
        }
    }

    /**
     * Actualiza el contenido de la tabla de reseñas.
     * Recupera del servicio únicamente los comentarios que pertenecen al ISBN del libro actual.
     */
    private void refrescarResenas() {
        if (resService != null && libro != null) {
            tablaResenas.setItems(FXCollections.observableArrayList(
                    resService.listarPorLibro(libro.getIsbn())
            ));
        }
    }

    /**
     * Finaliza la sesión de visualización de detalles cerrando la ventana actual.
     */
    @FXML
    private void onRegresar() {
        ((javafx.stage.Stage) lblIsbn.getScene().getWindow()).close();
    }
}