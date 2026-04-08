package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador de la interfaz gráfica para el formulario de registro y edición de libros.
 * Gestiona la captura de datos del usuario, la validación básica y la comunicación
 * con el servicio de la biblioteca para almacenar la información.
 */
public class FormControllers {

    /** Campo de texto para ingresar o mostrar el ISBN (Identificador único) del libro. */
    @FXML private TextField txtIsbn;

    /** Campo de texto para ingresar o mostrar el título del libro. */
    @FXML private TextField txtTitulo;

    /** Campo de texto para ingresar o mostrar el autor del libro. */
    @FXML private TextField txtAutor;

    /** Campo de texto para ingresar o mostrar el año de publicación del libro. */
    @FXML private TextField txtAnio;

    /** Campo de texto para ingresar o mostrar el género literario del libro. */
    @FXML private TextField txtGenero;

    /** Casilla de verificación que indica si el libro se encuentra disponible en la biblioteca. */
    @FXML private CheckBox chkDisponible;

    /** Servicio de la biblioteca encargado de la lógica de negocio y persistencia de datos. */
    private LibraryService service;

    /** Referencia al controlador de la ventana principal para poder actualizar sus vistas. */
    private MainControllers mainController;

    /**
     * Establece el servicio de la biblioteca que utilizará este controlador.
     * * @param service Instancia de {@link LibraryService} para gestionar las operaciones de los libros.
     */
    public void setService(LibraryService service) {
        this.service = service;
    }

    /**
     * Establece la referencia al controlador principal de la aplicación.
     * * @param mainController Instancia de {@link MainControllers} para notificar cambios (ej. refrescar tablas).
     */
    public void setMainController(MainControllers mainController) {
        this.mainController = mainController;
    }

    /**
     * Precarga los campos del formulario con los datos de un libro existente.
     * Este método es útil cuando se utiliza el formulario para editar en lugar de crear.
     * * @param libro Instancia de {@link LibroModel} que contiene los datos a mostrar en pantalla.
     */
    public void cargarLibro(LibroModel libro) {
        txtIsbn.setText(libro.getIsbn());
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnio()));
        txtGenero.setText(libro.getGenero());
        chkDisponible.setSelected(libro.isDisponible());
    }




}