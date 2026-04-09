package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador encargado de gestionar el formulario de entrada de datos.
 * Permite realizar operaciones de creación y edición de libros, validando
 * la entrada del usuario y sincronizando los cambios con la vista principal.
 */
public class FormControllers {

    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    /** Instancia del servicio para procesar la persistencia y reglas de negocio. */
    private LibraryService service;

    /** Referencia al controlador de la pantalla principal para actualizar la UI tras guardar. */
    private MainControllers mainController;

    /** Objeto temporal que almacena el libro en caso de estar en modo "Edición". */
    private LibroModel editando;

    /**
     * Inyecta el servicio de lógica de negocio en el controlador.
     *
     * @param s Instancia de LibraryService.
     */
    public void setService(LibraryService s) {
        this.service = s;
    }

    /**
     * Establece el vínculo con el controlador principal.
     * Esencial para invocar el refresco de la tabla una vez se confirmen los cambios.
     *
     * @param m Instancia del MainControllers operativo.
     */
    public void setMainController(MainControllers m) {
        this.mainController = m;
    }

    /**
     * Prepara el formulario para el modo "Edición".
     * Carga los datos del objeto seleccionado en los campos de texto y bloquea
     * el ISBN para mantener la integridad de la llave primaria.
     *
     * @param l El modelo del libro que se desea modificar.
     */
    public void cargarLibro(LibroModel l) {
        this.editando = l;
        txtIsbn.setText(l.getIsbn());
        txtIsbn.setEditable(false);
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtAnio.setText(String.valueOf(l.getAnio()));
        txtGenero.setText(l.getGenero());
        chkDisponible.setSelected(l.isDisponible());
    }

    /**
     * Procesa la recolección de datos y decide entre crear o actualizar un registro.
     * Limpia los espacios en blanco, realiza el parseo numérico y maneja las excepciones
     * delegadas por la capa de servicio (como validaciones de duplicados).
     */
    @FXML
    private void onGuardar() {
        try {
            String isbn = txtIsbn.getText().trim();
            String tit = txtTitulo.getText().trim();
            String aut = txtAutor.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText());
            String gen = txtGenero.getText().trim();
            boolean disp = chkDisponible.isSelected();

            if (editando == null) {
                service.agregar(new LibroModel(isbn, tit, aut, anio, gen, disp));
            } else {
                editando.setTitulo(tit);
                editando.setAutor(aut);
                editando.setAnio(anio);
                editando.setGenero(gen);
                editando.setDisponible(disp);
                service.actualizar(editando);
            }

            if (mainController != null) mainController.refrescarTabla();

            cerrar();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El año debe ser un número entero.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    /**
     * Cancela la operación actual y cierra el formulario sin guardar los cambios.
     */
    @FXML
    private void onCancelar() {
        cerrar();
    }

    /**
     * Recupera el escenario (Stage) actual a partir de la jerarquía de nodos de la vista
     * y solicita su cierre para salir del modo modal.
     */
    private void cerrar() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }
}