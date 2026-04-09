package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.UUID;

/**
 * Controlador de la vista de detalles de un libro.
 * Muestra la informacion del ejemplar y gestiona la visualizacion y creacion de resenas.
 */
public class DetailControllers {

    @FXML private Label lblIsbn, lblTitulo, lblAutor, lblAnio, lblGenero, lblDisponible;

    @FXML private TableView<ResenaModel> tablaResenas;
    @FXML private TableColumn<ResenaModel, Integer> colCalificacion;
    @FXML private TableColumn<ResenaModel, String> colComentario;

    @FXML private TextField txtCalificacion, txtComentario;

    private ResenaService resenaService;
    private LibroModel libroActual;

    /**
     * Inicializa la interfaz con los datos del libro y carga sus resenas correspondientes.
     * @param libro Objeto con la informacion del libro a mostrar.
     * @param resenaService Servicio para gestionar las consultas y registros de resenas.
     */
    public void cargarDatos(LibroModel libro, ResenaService resenaService) {
        this.libroActual = libro;
        this.resenaService = resenaService;

        lblIsbn.setText(libro.getIsbn());
        lblTitulo.setText(libro.getTitulo());
        lblAutor.setText(libro.getAutor());
        lblAnio.setText(String.valueOf(libro.getAnio()));
        lblGenero.setText(libro.getGenero());
        lblDisponible.setText(libro.isDisponible() ? "Disponible" : "No disponible");

        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        refrescarResenas();
    }

    /**
     * Captura los datos del formulario, los valida, crea una nueva resena con un ID unico
     * y la guarda a traves del servicio.
     */
    @FXML
    private void onAgregarResena() {
        try {
            String calificacionTexto = txtCalificacion.getText().trim();
            String comentario = txtComentario.getText().trim();

            if (calificacionTexto.isEmpty() || comentario.isEmpty()) {
                mostrarAlerta("Campos Incompletos", "Por favor, ingresa una calificacion y un comentario.");
                return;
            }

            int calificacion = Integer.parseInt(calificacionTexto);
            if (calificacion < 1 || calificacion > 5) {
                mostrarAlerta("Rango Invalido", "La calificacion debe ser un numero entre el 1 y el 5.");
                return;
            }

            ResenaModel nuevaResena = new ResenaModel(
                    UUID.randomUUID().toString(),
                    libroActual.getIsbn(),
                    calificacion,
                    comentario
            );

            resenaService.agregar(nuevaResena);
            refrescarResenas();

            txtCalificacion.clear();
            txtComentario.clear();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "La calificacion debe ser un numero entero (ejemplo: 4).");
        }
    }

    /**
     * Sincroniza la tabla visual de resenas obteniendo la lista mas reciente desde el servicio.
     */
    private void refrescarResenas() {
        if (resenaService != null && libroActual != null) {
            tablaResenas.setItems(FXCollections.observableArrayList(
                    resenaService.listarPorLibro(libroActual.getIsbn())
            ));
        }
    }

    /**
     * Cierra la ventana de detalles actual.
     */
    @FXML
    private void onRegresar() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }

    /**
     * Utilidad privada para generar ventanas emergentes de advertencia sin repetir codigo.
     * @param titulo Encabezado de la alerta.
     * @param mensaje Cuerpo del texto a mostrar al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING, mensaje);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.show();
    }
}