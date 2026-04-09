package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador de la interfaz grafica para la validacion de eliminacion de registros.
 * Exige la confirmacion manual del ISBN por parte del usuario para proceder.
 */
public class DeleteConfirmControllers {

    @FXML private TextField txtIsbnTarget;
    @FXML private TextField txtIsbnConfirm;

    private String isbnCorrecto;
    private boolean confirmado = false;

    /**
     * Inicializa la vista inyectando el ISBN del libro que se pretende eliminar.
     * @param isbn Identificador unico del libro seleccionado a eliminar.
     */
    public void inicializarDatos(String isbn) {
        this.isbnCorrecto = isbn;
        this.txtIsbnTarget.setText(isbn);
    }

    /**
     * Verifica el estado de la confirmacion de seguridad por parte del usuario.
     * @return true si el usuario ingreso el ISBN correctamente, false en caso contrario.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Compara el valor ingresado por el usuario con el ISBN objetivo.
     * Si la validacion es exitosa, actualiza la bandera de confirmacion y cierra la vista.
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
     * Interrumpe la operacion de eliminacion estableciendo un estado negativo y cerrando la vista.
     */
    @FXML
    private void onCancelarClick() {
        confirmado = false;
        cerrarVentana();
    }

    /**
     * Recupera el escenario actual a partir del contexto del componente y solicita su cierre.
     */
    private void cerrarVentana() {
        ((Stage) txtIsbnConfirm.getScene().getWindow()).close();
    }

    /**
     * Construye y expone una ventana emergente de tipo advertencia para notificar al usuario.
     * @param titulo Titulo que se mostrara en la barra superior de la alerta.
     * @param mensaje Detalle del error o advertencia dirigido al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}