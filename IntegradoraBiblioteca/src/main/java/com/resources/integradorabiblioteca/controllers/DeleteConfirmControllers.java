package com.resources.integradorabiblioteca.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador especializado en la gestión de seguridad para operaciones críticas.
 * Implementa el patrón de "Borrado Seguro", obligando al usuario a realizar
 * una validación manual mediante la re-escritura del ISBN antes de permitir
 * la eliminación de un registro del inventario.
 */
public class DeleteConfirmControllers {

    /** Campo visual de solo lectura que presenta el identificador del libro a eliminar. */
    @FXML private TextField txtIsbnTarget;

    /** Campo de entrada donde el usuario debe replicar el identificador para validar la acción. */
    @FXML private TextField txtIsbnConfirm;

    /** Variable interna que almacena el ISBN original para realizar la comparación lógica. */
    private String correcto;

    /** Bandera de estado que indica si la validación fue exitosa tras cerrar la ventana. */
    private boolean confirmado = false;

    /**
     * Prepara el entorno de validación con la información del libro seleccionado.
     * Establece la referencia de comparación y muestra el objetivo en la interfaz.
     * * @param isbn El identificador único del libro que se encuentra en proceso de baja.
     */
    public void inicializarDatos(String isbn) {
        this.correcto = isbn;
        // Se presenta el ISBN en el campo superior para que el usuario tenga la referencia visual.
        this.txtIsbnTarget.setText(isbn);
    }

    /**
     * Método de consulta utilizado por el controlador principal para verificar el veredicto.
     * * @return true si los textos coincidieron y se pulsó confirmar; false en cualquier otro caso.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Orquestador de la validación de identidad.
     * Compara el contenido de la entrada del usuario con la referencia almacenada.
     * Si la verificación es positiva, autoriza la operación; de lo contrario,
     * interrumpe el flujo con una alerta de advertencia.
     */
    @FXML
    private void onConfirmarClick() {
        // Validación de coincidencia exacta de cadenas de texto.
        if (txtIsbnConfirm.getText().equals(correcto)) {
            // Se autoriza la confirmación.
            confirmado = true;
            cerrarVentana();
        } else {
            // Se informa al usuario sobre la discrepancia en los datos ingresados.
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El ISBN ingresado no coincide con el registro original.");
            alerta.setTitle("Error de Validación");
            alerta.setHeaderText(null);
            alerta.show();
        }
    }

    /**
     * Gestiona la cancelación voluntaria de la operación.
     * Cierra la interfaz manteniendo el estado 'confirmado' en falso.
     */
    @FXML
    private void onCancelarClick() {
        cerrarVentana();
    }

    /**
     * Procedimiento técnico para finalizar el ciclo de vida de la ventana actual.
     * Localiza el escenario (Stage) a través del grafo de escena del componente de confirmación.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbnConfirm.getScene().getWindow();
        stage.close();
    }
}