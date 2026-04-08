package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador del formulario de libros
 * Permite crear o editar registros
 */
public class FormControllers {
    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers parentController;
    private boolean modoEdicion = false;

    /**
     * Inicializa datos del formulario
     * @param service servicio de biblioteca
     * @param libro libro a editar, null si es nuevo
     * @param parentController controlador principal
     */
    public void inicializarDatos(LibraryService service, Libro libro, MainControllers parentController) {
        this.service = service;
        this.parentController = parentController;

        if (libro != null) {
            modoEdicion = true;
            txtIsbn.setText(libro.getIsbn());
            txtIsbn.setDisable(true);
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor());
            txtAnio.setText(String.valueOf(libro.getAnio()));
            txtGenero.setText(libro.getGenero());
            chkDisponible.setSelected(libro.isDisponible());
        }
    }

    /**
     * Accion para guardar libro nuevo o editado
     */
    @FXML
    private void onSaveClick() {
        try {
            String isbn = txtIsbn.getText();
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            String genero = txtGenero.getText();
            boolean disponible = chkDisponible.isSelected();
            int anio = Integer.parseInt(txtAnio.getText());

            Libro libro = new Libro(isbn, titulo, autor, anio, genero, disponible);

            if (modoEdicion) {
                service.actualizarLibro(libro);
            } else {
                service.agregarLibro(libro);
            }

            parentController.actualizarTabla();
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Formato de año incorrecto. Ingrese un valor numerico.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Accion para cancelar y cerrar formulario
     */
    @FXML
    private void onCancelClick() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra un mensaje de error
     * @param mensaje texto del error
     */
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de Validacion");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
