package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador de la ventana de confirmacion para eliminar un registro.
 * Obliga al usuario a escribir el ISBN del libro para evitar borrados accidentales.
 */
public class DeleteConfirmControllers {

    @FXML private TextField txtIsbnTarget;
    @FXML private TextField txtIsbnConfirm;

    private String isbnCorrecto;
    private boolean confirmado = false;

    /**
     * Recibe el ISBN del libro seleccionado y lo muestra en la interfaz para referencia.
     * @param isbn Identificador unico del libro a eliminar.
     */
    public void inicializarDatos(String isbn) {
        this.isbnCorrecto = isbn;
        this.txtIsbnTarget.setText(isbn);
    }

    /**
     * Indica si el usuario confirmo la accion correctamente.
     * @return true si el ISBN ingresado coincide, false en caso contrario.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Verifica que el texto ingresado sea exactamente igual al ISBN objetivo.
     * Si es correcto, autoriza la eliminacion y cierra la ventana.
     */
    @FXML
    private void onConfirmarClick() {
        String inputUsuario = txtIsbnConfirm.getText();

        if (inputUsuario != null && inputUsuario.trim().equals(isbnCorrecto)) {
            confirmado = true;
            cerrarVentana();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El ISBN ingresado no coincide con el registro original.");
            alerta.setTitle("Error de Validacion");
            alerta.setHeaderText(null);
            alerta.show();
        }
    }

    /**
     * Cancela la operacion de eliminacion y cierra la ventana.
     */
    @FXML
    private void onCancelarClick() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual (Stage) de JavaFX.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbnConfirm.getScene().getWindow();
        stage.close();
    }
}