package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.UUID;

/**
 * Controlador de la vista expandida del sistema.
 * Esta clase se encarga de presentar la ficha técnica detallada de un libro
 * seleccionado y de gestionar el subsistema de valoraciones, actuando como
 * punto de unión entre la información estática del ejemplar y la dinámica
 * de los comentarios de los usuarios.
 */
public class DetailControllers {

    // --- Componentes de la Ficha Técnica (Labels) ---
    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    // --- Componentes del Módulo de Reseñas (UI) ---
    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;
    @FXML private TableColumn<ResenaModel, String> colComentario;

    // --- Campos de entrada para nuevas reseñas ---
    @FXML private TextField txtCalificacion, txtComentario;

    /** Instancia del servicio encargado de procesar la lógica de negocio de las opiniones. */
    private ResenaService resService;

    /** Referencia al modelo del libro en foco para establecer vínculos de datos. */
    private LibroModel libro;

    /**
     * Inicializa la vista con la información del libro y configura el entorno de reseñas.
     * Este método inyecta las dependencias necesarias y puebla los campos visuales.
     * * @param b  Objeto LibroModel que contiene la información a desplegar.
     * @param rs Instancia de ResenaService para gestionar las consultas y registros.
     */
    public void cargarDatos(LibroModel b, ResenaService rs) {
        // Asignación de referencias locales.
        this.libro = b;
        this.resService = rs;

        // Poblamiento de etiquetas de texto a partir del modelo.
        lblIsbn.setText(b.getIsbn());
        lblTitulo.setText(b.getTitulo());
        lblAutor.setText(b.getAutor());
        lblAnio.setText(String.valueOf(b.getAnio()));
        lblGenero.setText(b.getGenero());
        lblDisponible.setText(b.isDisponible() ? "Disponible" : "No disponible");

        // Vinculación de las columnas de la tabla con los atributos de ResenaModel.
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        // Carga inicial de comentarios existentes.
        refrescarResenas();
    }

    /**
     * Orquestador del proceso de creación de una nueva valoración.
     * Realiza la recolección de datos, genera una identidad única para el registro
     * y solicita al servicio su incorporación al sistema.
     */
    @FXML
    private void onAgregarResena() {
        try {
            /**
             * Creación de un objeto de transferencia de datos (ResenaModel).
             * Se genera un UUID aleatorio para asegurar la unicidad del ID de reseña.
             * Se utiliza el ISBN del libro actual para establecer la relación lógica (FK).
             */
            ResenaModel nuevaResena = new ResenaModel(
                    UUID.randomUUID().toString(), // Generación de ID Universal
                    libro.getIsbn(),             // Vinculación por llave foránea
                    Integer.parseInt(txtCalificacion.getText()), // Parseo de calificación
                    txtComentario.getText()      // Captura del cuerpo del mensaje
            );

            // Delegación de la persistencia a la capa de servicio.
            resService.agregar(nuevaResena);

            // Actualización inmediata de la tabla para reflejar el nuevo comentario.
            refrescarResenas();

            // Limpieza de campos de entrada para facilitar un nuevo registro.
            txtCalificacion.clear();
            txtComentario.clear();

        } catch (NumberFormatException e) {
            // Manejo de errores en caso de que la calificación no sea un número entero.
            new Alert(Alert.AlertType.WARNING, "La calificación debe ser un valor numérico.").show();
        }
    }

    /**
     * Sincroniza la tabla visual con el estado de la base de datos en memoria.
     * Solicita al servicio solo aquellas reseñas asociadas al ISBN del libro actual.
     */
    private void refrescarResenas() {
        if (resService != null && libro != null) {
            /**
             * El servicio devuelve una List filtrada manualmente.
             * Se envuelve en una ObservableArrayList para que la tabla de JavaFX
             * pueda procesar y mostrar los datos correctamente.
             */
            tablaResenas.setItems(FXCollections.observableArrayList(
                    resService.listarPorLibro(libro.getIsbn())
            ));
        }
    }

    /**
     * Ejecuta el cierre controlado de la ventana de detalles.
     * Recupera la escena y el escenario actual a través de la jerarquía de componentes.
     */
    @FXML
    private void onRegresar() {
        ((javafx.stage.Stage) lblIsbn.getScene().getWindow()).close();
    }
}