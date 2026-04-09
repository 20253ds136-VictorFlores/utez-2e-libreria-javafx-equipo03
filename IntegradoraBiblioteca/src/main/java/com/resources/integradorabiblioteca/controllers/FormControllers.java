package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador del formulario para registrar y modificar libros.
 * Reutiliza la misma ventana para crear un libro nuevo o editar uno existente.
 */
public class FormControllers {

    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers mainController;
    private LibroModel libroEditando;

    /**
     * Asigna el servicio de libros al controlador.
     * @param libraryService Instancia del servicio principal.
     */
    public void setService(LibraryService libraryService) {
        this.service = libraryService;
    }

    /**
     * Conecta este formulario con el controlador principal para poder actualizar la tabla.
     * @param controladorPrincipal Instancia de la ventana principal.
     */
    public void setMainController(MainControllers controladorPrincipal) {
        this.mainController = controladorPrincipal;
    }

    /**
     * Llena los campos del formulario con los datos del libro que se va a modificar.
     * Bloquea la edicion del ISBN para evitar problemas en la base de datos.
     * @param libroAEditar Libro seleccionado en la tabla.
     */
    public void cargarLibro(LibroModel libroAEditar) {
        this.libroEditando = libroAEditar;

        txtIsbn.setText(libroAEditar.getIsbn());
        txtIsbn.setEditable(false);
        txtTitulo.setText(libroAEditar.getTitulo());
        txtAutor.setText(libroAEditar.getAutor());
        txtAnio.setText(String.valueOf(libroAEditar.getAnio()));
        txtGenero.setText(libroAEditar.getGenero());
        chkDisponible.setSelected(libroAEditar.isDisponible());
    }

    /**
     * Lee los campos de texto y guarda el libro.
     * Decide automaticamente si debe crear uno nuevo o actualizar el existente.
     */
    @FXML
    private void onGuardar() {
        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText());
            String genero = txtGenero.getText().trim();
            boolean disponible = chkDisponible.isSelected();

            if (libroEditando == null) {
                service.agregar(new LibroModel(isbn, titulo, autor, anio, genero, disponible));
            } else {
                libroEditando.setTitulo(titulo);
                libroEditando.setAutor(autor);
                libroEditando.setAnio(anio);
                libroEditando.setGenero(genero);
                libroEditando.setDisponible(disponible);
                service.actualizar(libroEditando);
            }

            if (mainController != null) {
                mainController.refrescarTabla();
            }

            cerrar();

        } catch (NumberFormatException e) {
            Alert alertaNumero = new Alert(Alert.AlertType.ERROR, "El valor ingresado en el anio debe ser un numero entero valido.");
            alertaNumero.setTitle("Error de Formato");
            alertaNumero.setHeaderText(null);
            alertaNumero.show();
        } catch (Exception e) {
            Alert alertaServicio = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alertaServicio.setTitle("Error de Validacion");
            alertaServicio.setHeaderText(null);
            alertaServicio.show();
        }
    }

    /**
     * Cierra la ventana sin guardar los datos.
     */
    @FXML
    private void onCancelar() {
        cerrar();
    }

    /**
     * Metodo auxiliar para cerrar la ventana modal actual.
     */
    private void cerrar() {
        Stage stage = (Stage) txtIsbn.getScene().getWindow();
        stage.close();
    }
}