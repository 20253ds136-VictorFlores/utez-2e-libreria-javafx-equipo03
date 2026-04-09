package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador especializado en la gestión de la ventana de captura de datos.
 * Funciona bajo un esquema dual: puede operar en modo "Alta" (crear nuevo objeto)
 * o en modo "Edición" (modificar objeto existente), adaptando su comportamiento
 * según el estado del atributo {@code editando}.
 */
public class FormControllers {

    // --- Vinculación con componentes visuales FXML ---
    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    /** Referencia al servicio de lógica de negocio inyectada desde el exterior. */
    private LibraryService service;

    /** Referencia al controlador de la vista principal para permitir el refresco de datos. */
    private MainControllers mainController;

    /** Almacena el objeto que se está modificando; si es nulo, el controlador asume modo "Nuevo". */
    private LibroModel editando;

    /**
     * Inyecta el servicio necesario para realizar las operaciones de persistencia.
     * @param s Instancia activa de LibraryService.
     */
    public void setService(LibraryService s) {
        this.service = s;
    }

    /**
     * Establece la conexión con el controlador de la ventana padre.
     * @param m Instancia de MainControllers.
     */
    public void setMainController(MainControllers m) {
        this.mainController = m;
    }

    /**
     * Configura el formulario para la actualización de un libro existente.
     * Carga los valores actuales del modelo en los nodos de la interfaz y deshabilita
     * la edición del ISBN para evitar inconsistencias en la base de datos.
     * @param l El libro seleccionado para editar.
     */
    public void cargarLibro(LibroModel l) {
        this.editando = l;
        txtIsbn.setText(l.getIsbn());
        txtIsbn.setEditable(false); // El identificador único no debe alterarse tras su creación.
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtAnio.setText(String.valueOf(l.getAnio()));
        txtGenero.setText(l.getGenero());
        chkDisponible.setSelected(l.isDisponible());
    }

    /**
     * Orquestador del evento de confirmación.
     * Recolecta la información de la interfaz, realiza el parseo de tipos y
     * delega la validación de reglas de negocio a la capa de servicio.
     */
    @FXML
    private void onGuardar() {
        try {
            // Recolección y saneamiento de datos (eliminación de espacios laterales).
            String isbn = txtIsbn.getText().trim();
            String tit = txtTitulo.getText().trim();
            String aut = txtAutor.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText());
            String gen = txtGenero.getText().trim();
            boolean disp = chkDisponible.isSelected();

            // Lógica de decisión según el modo de operación.
            if (editando == null) {
                // Modo Alta: Se instancia un nuevo objeto y se envía al servicio.
                service.agregar(new LibroModel(isbn, tit, aut, anio, gen, disp));
            } else {
                // Modo Edición: Se actualiza el objeto referenciado en memoria.
                editando.setTitulo(tit);
                editando.setAutor(aut);
                editando.setAnio(anio);
                editando.setGenero(gen);
                editando.setDisponible(disp);
                service.actualizar(editando);
            }

            // Notificación al controlador principal para actualizar la tabla visual.
            if (mainController != null) mainController.refrescarTabla();

            // Salida exitosa.
            cerrar();

        } catch (NumberFormatException e) {
            // Manejo específico para errores de conversión de texto a número en el año.
            new Alert(Alert.AlertType.ERROR, "El año debe ser un número entero válido.").show();
        } catch (Exception e) {
            // Captura de validaciones lanzadas por el servicio (ej: duplicados).
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Aborta la operación y cierra el escenario sin realizar cambios.
     */
    @FXML
    private void onCancelar() {
        cerrar();
    }

    /**
     * Utilidad privada para cerrar la ventana modal de forma segura.
     * Obtiene el Stage a través de la propiedad Scene de uno de los componentes.
     */
    private void cerrar() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }
}