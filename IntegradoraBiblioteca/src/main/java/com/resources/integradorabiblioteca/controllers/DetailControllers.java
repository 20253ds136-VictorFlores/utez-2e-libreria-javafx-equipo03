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

/**
 * Controlador para la vista detallada de un libro.
 * Gestiona la visualización de metadatos del libro y la administración de reseñas
 * vinculadas al ISBN del ejemplar seleccionado.
 */
public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;
    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, String> colUsuario, colComentario;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;
    @FXML private TextField txtCalificacion, txtComentario;

    private LibroModel libroActual;
    private ResenaService resenaService;

    /**
     * Carga la información del libro en la interfaz y vincula el servicio de reseñas.
     * * @param libro El objeto {@link LibroModel} con los datos a mostrar.
     * @param resenaService El servicio para obtener y guardar reseñas.
     */
    public void cargarDatos(LibroModel libro, ResenaService resenaService) {
        this.libroActual = libro;
        this.resenaService = resenaService;

        inicializarComponentesTexto(libro);
        configurarEstructuraTabla();
        actualizarListaResenas();
    }

    /**
     * Mapea los atributos del libro a las etiquetas de la interfaz de usuario.
     */
    private void inicializarComponentesTexto(LibroModel libro) {
        lblIsbn.setText("ISBN: " + libro.getIsbn());
        lblTitulo.setText("Título: " + libro.getTitulo());
        lblAutor.setText("Autor: " + libro.getAutor());
        lblAnio.setText("Año: " + libro.getAnio());
        lblGenero.setText("Género: " + libro.getGenero());
        lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));
    }

    /**
     * Define las fábricas de celdas para las columnas de la TableView.
     */
    private void configurarEstructuraTabla() {
        colUsuario.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIdUsuario()));
        colCalificacion.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getCalificacion()).asObject());
        colComentario.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getComentario()));
    }

    /**
     * Procesa la creación de una nueva reseña.
     * Incluye validación de rango (1-5 estrellas) y manejo de campos vacíos.
     */
    @FXML
    private void onAgregarResena() {
        try {
            // Aplicamos trim() para ignorar espacios en blanco accidentales
            String califText = txtCalificacion.getText().trim();
            String comentarioText = txtComentario.getText().trim();

            if (califText.isEmpty() || comentarioText.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Debe ingresar una calificación y un comentario.");
                return;
            }

            int estrellas = Integer.parseInt(califText);

            if (estrellas < 1 || estrellas > 5) {
                mostrarAlerta("Calificación inválida", "Las estrellas deben ser un número entre 1 y 5.");
                return;
            }

            // Generación de ID único basado en timestamp
            ResenaModel nueva = new ResenaModel(
                    "R-" + System.currentTimeMillis(),
                    libroActual.getIsbn(),
                    "Invitado",
                    estrellas,
                    comentarioText
            );

            resenaService.agregar(nueva);
            limpiarEntradas();
            actualizarListaResenas();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La calificación debe ser un número entero (1-5).");
        } catch (Exception e) {
            mostrarAlerta("Error de sistema", "No se pudo guardar la reseña: " + e.getMessage());
        }
    }

    /**
     * Refresca los datos de la tabla filtrando por el ISBN del libro actual.
     */
    private void actualizarListaResenas() {
        if (resenaService != null && libroActual != null) {
            var listaFiltrada = resenaService.listarPorLibro(libroActual.getIsbn());
            tablaResenas.setItems(FXCollections.observableArrayList(listaFiltrada));
        }
    }

    /**
     * Limpia los campos de texto después de agregar una reseña exitosamente.
     */
    private void limpiarEntradas() {
        txtCalificacion.clear();
        txtComentario.clear();
    }

    /**
     * Cierra la ventana de detalles y regresa a la vista principal.
     */
    @FXML
    private void onRegresar() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una ventana de diálogo informativa al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}