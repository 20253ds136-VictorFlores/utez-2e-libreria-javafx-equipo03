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
 * Controlador para la ventana de formulario de libros.
 * Gestiona tanto la creación de nuevos ejemplares como la edición de los existentes,
 * validando la integridad de los datos antes de su persistencia.
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
     * Establece el servicio de lógica de negocio para la biblioteca.
     * @param service Instancia de {@link LibraryService}.
     */
    public void setService(LibraryService service) {
        this.service = service;
    }

    /**
     * Establece la referencia al controlador principal para permitir la actualización de la UI.
     * @param mainController Instancia del controlador de la vista principal.
     */
    public void setMainController(MainControllers mainController) {
        this.mainController = mainController;
    }

    /**
     * Prepara el formulario para la edición de un libro existente.
     * Bloquea el campo ISBN para mantener la integridad referencial.
     * @param libro El {@link LibroModel} cuyos datos se cargarán en los campos.
     */
    public void cargarLibro(LibroModel libro) {
        this.libroEditando = libro;

        txtIsbn.setText(libro.getIsbn());
        txtIsbn.setEditable(false); // Regla de negocio: El ISBN no es editable

        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio()));
        txtGenero.setText(libro.getGenero());
        chkDisponible.setSelected(libro.isDisponible());
    }

    /**
     * Procesa la acción de guardado.
     * Determina si se trata de una inserción nueva o una actualización basada en el contexto.
     */
    @FXML
    private void onGuardar() {
        if (!validarInyeccion() || !validarCampos()) {
            return;
        }

        try {
            // Extracción y limpieza de datos
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            String genero = txtGenero.getText().trim();
            boolean disponible = chkDisponible.isSelected();

            if (libroEditando == null) {
                // Operación de creación
                LibroModel nuevo = new LibroModel(isbn, titulo, autor, anio, genero, disponible);
                service.agregar(nuevo);
            } else {
                // Operación de actualización sobre objeto existente
                libroEditando.setTitulo(titulo);
                libroEditando.setAutor(autor);
                libroEditando.setAnio(anio);
                libroEditando.setGenero(genero);
                libroEditando.setDisponible(disponible);
                service.actualizar(libroEditando);
            }

            // Sincronización con la vista principal y cierre
            mainController.refrescarTabla();
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Formato de fecha inválido", "El año debe ser un número entero válido.");
        } catch (Exception e) {
            mostrarError("Error de persistencia", "No se pudo guardar la información: " + e.getMessage());
        }
    }

    /**
     * Valida que los campos obligatorios no estén vacíos.
     * @return true si los campos son válidos.
     */
    private boolean validarCampos() {
        if (txtIsbn.getText().trim().isEmpty() || txtTitulo.getText().trim().isEmpty()) {
            mostrarError("Campos obligatorios", "El ISBN y el Título no pueden estar vacíos.");
            return false;
        }
        return true;
    }

    /**
     * Verifica que las dependencias necesarias hayan sido inyectadas.
     */
    private boolean validarInyeccion() {
        if (service == null || mainController == null) {
            mostrarError("Error de sistema", "Dependencias del controlador no inicializadas.");
            return false;
        }
        return true;
    }

    /**
     * Cierra el formulario sin realizar cambios.
     */
    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual obteniendo el Stage desde cualquier componente.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }

    /**
     * Centraliza el manejo de mensajes de error para la interfaz.
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}