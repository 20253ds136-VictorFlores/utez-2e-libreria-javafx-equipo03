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
 * Optimizado para la gestión de reseñas anónimas y visualización de metadatos.
 * Se ha eliminado la columna de usuario tanto de la UI como de la lógica.
 */
public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    @FXML private TableView<ResenaModel> tablaResenas;

    // Solo mantenemos las columnas de Calificación y Comentario
    @FXML private TableColumn<ResenaModel, String> colComentario;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;

    @FXML private TextField txtCalificacion, txtComentario;

    private LibroModel libroActual;
    private ResenaService resenaService;

    /**
     * Vincula el libro seleccionado y el servicio de reseñas a la vista.
     */
    public void cargarDatos(LibroModel libro, ResenaService resenaService) {
        this.libroActual = libro;
        this.resenaService = resenaService;

        inicializarComponentesTexto(libro);
        configurarEstructuraTabla();
        actualizarListaResenas();
    }

    private void inicializarComponentesTexto(LibroModel libro) {
        lblIsbn.setText("ISBN: " + libro.getIsbn());
        lblTitulo.setText("Título: " + libro.getTitulo());
        lblAutor.setText("Autor: " + libro.getAutor());
        lblAnio.setText("Año: " + libro.getAnio());
        lblGenero.setText("Género: " + libro.getGenero());
        lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));
    }

    /**
     * Configura cómo se extraen los datos de los objetos ResenaModel para la tabla.
     * Ya no incluye la vinculación de colUsuario.
     */
    private void configurarEstructuraTabla() {
        // Vinculación de la columna calificación con el atributo entero del modelo
        colCalificacion.setCellValueFactory(cd ->
                new SimpleIntegerProperty(cd.getValue().getCalificacion()).asObject());

        // Vinculación de la columna comentario con el atributo string del modelo
        colComentario.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getComentario()));
    }

    /**
     * Captura la entrada del usuario y genera una reseña sin vinculación a cuenta.
     */
    @FXML
    private void onAgregarResena() {
        try {
            String califText = txtCalificacion.getText().trim();
            String comentarioText = txtComentario.getText().trim();

            if (califText.isEmpty() || comentarioText.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Por favor, rellene todos los campos.");
                return;
            }

            int estrellas = Integer.parseInt(califText);

            if (estrellas < 1 || estrellas > 5) {
                mostrarAlerta("Rango inválido", "La calificación debe ser de 1 a 5.");
                return;
            }

            // CREACIÓN DE OBJETO ANÓNIMO (Constructor de 4 Parámetros)
            // Se eliminó definitivamente el parámetro de "Invitado" o "ID Usuario"
            ResenaModel nueva = new ResenaModel(
                    "R-" + System.currentTimeMillis(), // Generador de ID único temporal
                    libroActual.getIsbn(),             // Llave foránea que une la reseña al libro
                    estrellas,                         // Valor de la calificación
                    comentarioText                     // Cuerpo de la opinión
            );

            resenaService.agregar(nueva);
            limpiarEntradas();
            actualizarListaResenas();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de datos", "Ingrese un número válido para la calificación.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al procesar la reseña: " + e.getMessage());
        }
    }

    private void actualizarListaResenas() {
        if (resenaService != null && libroActual != null) {
            var listaFiltrada = resenaService.listarPorLibro(libroActual.getIsbn());
            tablaResenas.setItems(FXCollections.observableArrayList(listaFiltrada));
        }
    }

    private void limpiarEntradas() {
        txtCalificacion.clear();
        txtComentario.clear();
    }

    @FXML
    private void onRegresar() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}