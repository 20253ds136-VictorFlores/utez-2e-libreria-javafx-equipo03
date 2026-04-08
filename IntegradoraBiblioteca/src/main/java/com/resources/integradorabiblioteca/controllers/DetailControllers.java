package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador de la interfaz gráfica para la vista de detalle de un libro.
 * Esta clase gestiona una ventana de solo lectura donde el usuario puede
 * visualizar toda la información de un libro específico sin posibilidad de editarla.
 */
public class DetailControllers {

    /** Etiqueta (Label) de la interfaz para mostrar el ISBN. */
    @FXML private Label lblIsbn;

    /** Etiqueta (Label) de la interfaz para mostrar el título. */
    @FXML private Label lblTitulo;

    /** Etiqueta (Label) de la interfaz para mostrar el autor. */
    @FXML private Label lblAutor;

    /** Etiqueta (Label) de la interfaz para mostrar el año de publicación. */
    @FXML private Label lblAnio;

    /** Etiqueta (Label) de la interfaz para mostrar el género literario. */
    @FXML private Label lblGenero;

    /** Etiqueta (Label) de la interfaz para mostrar si el libro está disponible o no. */
    @FXML private Label lblDisponible;

    /**
     * Recibe un objeto LibroModel y puebla las etiquetas de la interfaz con sus datos.
     * Añade prefijos descriptivos (ej. "Título: ") para mejorar la presentación
     * en pantalla y formatea el valor booleano de disponibilidad a "Sí" o "No".
     * * @param libro Instancia de {@link LibroModel} cuyos detalles se van a mostrar.
     */
    public void setLibro(LibroModel libro) {
        lblIsbn.setText("ISBN: " + libro.getIsbn());
        lblTitulo.setText("Título: " + libro.getTitulo());
        lblAutor.setText("Autor: " + libro.getAutor());
        lblAnio.setText("Año: " + libro.getAnio());
        lblGenero.setText("Género: " + libro.getGenero());
        lblDisponible.setText("Disponible: " + (libro.isDisponible() ? "Sí" : "No"));
    }

    /**
     * Método manejador de eventos asociado al botón de "Regresar" o "Cerrar".
     * Obtiene la ventana (Stage) actual a partir de la escena de uno de los
     * componentes gráficos (lblIsbn) y procede a cerrarla.
     */
    @FXML
    private void onRegresar() {
        Stage stage = (Stage) lblIsbn.getScene().getWindow();
        stage.close();
    }
}