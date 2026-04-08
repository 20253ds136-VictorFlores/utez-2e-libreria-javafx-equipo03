package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de formulario (Crear/Editar Libro).
 */
public class FormControllers {

    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers mainController;
    private LibroModel libroEditando;

    /**
     * Inyecta el servicio de biblioteca necesario para persistir los datos.
     */
    public void setService(LibraryService service) {
        this.service = service;
    }

    /**
     * Inyecta el controlador principal para poder refrescar la tabla al terminar.
     */
    public void setMainController(MainControllers mainController) {
        this.mainController = mainController;
    }

    /**
     * Carga los datos de un libro existente en los campos del formulario.
     * Si se llama a este método, el formulario entra en "modo edición".
     */
    public void cargarLibro(LibroModel libro) {
        this.libroEditando = libro;
        txtIsbn.setText(libro.getIsbn());
        txtIsbn.setEditable(false); // El ISBN no debe editarse
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio()));
        txtGenero.setText(libro.getGenero());
        chkDisponible.setSelected(libro.isDisponible());
    }

    @FXML
    private void onGuardar() {
        // --- VALIDACIÓN DE INYECCIÓN ---
        if (service == null || mainController == null) {
            mostrarError("Error interno: Los servicios no fueron inyectados correctamente al formulario.");
            return;
        }

        try {
            boolean disponible = chkDisponible.isSelected();

            if (libroEditando == null) {
                // MODO NUEVO: Validar campos vacíos antes de crear
                if (txtIsbn.getText().isEmpty() || txtTitulo.getText().isEmpty()) {
                    mostrarError("El ISBN y el Título son campos obligatorios.");
                    return;
                }

                LibroModel nuevo = new LibroModel(
                        txtIsbn.getText(),
                        txtTitulo.getText(),
                        txtAutor.getText(),
                        Integer.parseInt(txtAnio.getText()),
                        txtGenero.getText(),
                        disponible
                );
                service.agregar(nuevo);
            } else {
                // MODO EDICIÓN: Solo actualizamos los valores permitidos
                libroEditando.setTitulo(txtTitulo.getText());
                libroEditando.setAutor(txtAutor.getText());
                libroEditando.setAnio(Integer.parseInt(txtAnio.getText()));
                libroEditando.setGenero(txtGenero.getText());
                libroEditando.setDisponible(disponible);

                service.actualizar(libroEditando);
            }

            // Notificar al MainController que los datos cambiaron
            mainController.refrescarTabla();
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("El año debe ser un número válido.");
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}