package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador de vista encargado de la captura de datos para la creacion
 * o modificacion de las entidades Libro dentro del sistema.
 */
public class FormControllers {

    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers parentController;
    private boolean modoEdicion = false;

    /**
     * Vincula las dependencias operativas y, de ser necesario, pobla los campos con los datos del libro a editar.
     * @param service Servicio que implementa la logica de negocio.
     * @param libro Entidad Libro seleccionada; si es null, la vista operara en modo de creacion.
     * @param parentController Referencia al controlador principal para forzar actualizaciones visuales.
     */
    public void inicializarDatos(LibraryService service, Libro libro, MainControllers parentController) {
        this.service = service;
        this.parentController = parentController;

        if (libro != null) {
            this.modoEdicion = true;
            this.txtIsbn.setText(libro.getIsbn());
            this.txtIsbn.setDisable(true);
            this.txtTitulo.setText(libro.getTitulo());
            this.txtAutor.setText(libro.getAutor());
            this.txtAnio.setText(String.valueOf(libro.getAnio()));
            this.txtGenero.setText(libro.getGenero());
            this.chkDisponible.setSelected(libro.isDisponible());
        }
    }

    /**
     * Captura la informacion del formulario, la ensambla en un objeto Libro y la envia al servicio.
     * Administra el flujo dependiendo de si es un alta nueva o una actualizacion.
     */
    @FXML
    private void onSaveClick() {
        try {
            Libro libro = new Libro(
                    txtIsbn.getText(),
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    Integer.parseInt(txtAnio.getText()),
                    txtGenero.getText(),
                    chkDisponible.isSelected()
            );

            if (modoEdicion) {
                service.actualizarLibro(libro);
            } else {
                service.agregarLibro(libro);
            }

            parentController.actualizarTabla();
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Formato de año incorrecto. Ingrese un valor numerico entero.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Descarta los cambios actuales y finaliza el ciclo de vida de la ventana.
     */
    @FXML
    private void onCancelClick() {
        cerrarVentana();
    }

    /**
     * Libera los recursos visuales y oculta el escenario de la pantalla.
     */
    private void cerrarVentana() {
        ((Stage) txtIsbn.getScene().getWindow()).close();
    }

    /**
     * Construye y expone una ventana emergente de tipo error al usuario en caso de excepciones.
     * @param mensaje Descripcion tecnica o logica del problema ocurrido.
     */
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de Validacion");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
