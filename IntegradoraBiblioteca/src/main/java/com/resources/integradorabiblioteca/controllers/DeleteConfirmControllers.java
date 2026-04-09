package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador para la ventana modal de confirmación de eliminación.
 * Implementa un mecanismo de "borrado seguro" que requiere que el usuario
 * escriba manualmente el ISBN del libro para validar la intención de borrado.
 */
public class DeleteConfirmControllers {

    /** Campo que muestra el ISBN del libro seleccionado (Solo lectura). */
    @FXML private TextField txtIsbnTarget;

    /** Campo donde el usuario debe ingresar el ISBN para confirmar. */
    @FXML private TextField txtIsbnConfirm;

    /** Almacena el ISBN correcto que se espera para validar la operación. */
    private String correcto;

    /** Estado final de la confirmación: true si el usuario validó correctamente. */
    private boolean confirmado = false;

    /**
     * Prepara la ventana de confirmación con los datos del libro a eliminar.
     * Muestra el ISBN objetivo en un campo de solo lectura para referencia del usuario.
     *
     * @param isbn Identificador único del libro que se pretende borrar.
     */
    public void inicializarDatos(String isbn) {
        this.correcto = isbn;
        this.txtIsbnTarget.setText(isbn);
    }

    /**
     * Informa al controlador solicitante si la eliminación fue autorizada.
     *
     * @return true si el ISBN ingresado coincidió con el objetivo; false en caso contrario.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Ejecuta la lógica de validación al presionar el botón de confirmación.
     * Compara el texto ingresado con el ISBN original. Si coinciden, marca
     * el estado como confirmado y cierra la ventana.
     */
    @FXML
    private void onConfirmarClick() {
        if (txtIsbnConfirm.getText().equals(correcto)) {
            confirmado = true;
            cerrarVentana();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El ISBN ingresado no coincide con el registro original.");
            alerta.setTitle("Error de Validación");
            alerta.setHeaderText(null);
            alerta.show();
        }
    }

    /**
     * Cancela la operación de borrado y cierra la ventana sin cambiar
     * el estado de confirmación.
     */
    @FXML
    private void onCancelarClick() {
        cerrarVentana();
    }

    /**
     * Recupera la ventana actual (Stage) a través de uno de sus componentes
     * y solicita su cierre.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbnConfirm.getScene().getWindow();
        stage.close();
    }
}