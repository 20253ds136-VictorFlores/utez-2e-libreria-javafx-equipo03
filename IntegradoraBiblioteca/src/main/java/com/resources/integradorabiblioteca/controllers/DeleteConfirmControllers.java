package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de confirmacion de eliminacion
 * Requiere que el usuario valide la accion reescribiendo o pegando el ISBN
 */
public class DeleteConfirmControllers {

    @FXML private TextField txtIsbnTarget;
    @FXML private TextField txtIsbnConfirm;

    private String isbnCorrecto;
    private boolean confirmado = false;

    /**
     * Prepara la vista con el ISBN del libro seleccionado
     * @param isbn Identificador unico que el usuario debe confirmar
     */
    public void inicializarDatos(String isbn) {
        this.isbnCorrecto = isbn;
        txtIsbnTarget.setText(isbn);
    }

    /**
     * Indica si el proceso de validacion fue exitoso
     * @return true si el usuario confirmo correctamente, false en caso contrario
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Valida que el texto ingresado coincida con el ISBN objetivo
     * Si es correcto, marca la confirmacion como exitosa y cierra la ventana
     */
    @FXML
    private void onConfirmarClick() {
        String input = txtIsbnConfirm.getText();

        if (input == null || input.trim().isEmpty()) {
            mostrarAlerta("Error", "El campo no puede estar vacio.");
            return;
        }

        if (input.trim().equals(isbnCorrecto)) {
            confirmado = true;
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "El ISBN no coincide.");
        }
    }

    /**
     * Cancela la operacion de eliminacion y cierra la ventana
     */
    @FXML
    private void onCancelarClick() {
        confirmado = false;
        cerrarVentana();
    }

    /**
     * Obtiene el escenario actual y lo cierra
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbnConfirm.getScene().getWindow();
        stage.close();
    }

    /**
     * Despliega una alerta de advertencia al usuario.
     * @param titulo Encabezado de la ventana.
     * @param mensaje Cuerpo del mensaje de error.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
