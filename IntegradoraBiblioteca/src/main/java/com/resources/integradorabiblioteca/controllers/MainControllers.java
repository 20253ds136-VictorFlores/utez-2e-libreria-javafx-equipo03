package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de la pantalla principal.
 * Muestra el catálogo en una tabla y conecta con LibraryService.
 */
public class MainControllers {

    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TableColumn<LibroModel, String> colIsbn;
    @FXML private TableColumn<LibroModel, String> colTitulo;
    @FXML private TableColumn<LibroModel, String> colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, String> colGenero;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    private LibraryService service;
    private ObservableList<LibroModel> data;

    /**
     * Inicializa el controlador al cargar la vista.
     * Configura columnas y carga datos desde el servicio.
     */
    @FXML
    public void initialize() {
        // Configurar columnas con atributos del modelo
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // Inicializar servicio con repositorio
        service = new LibraryService(new FileRepository("data/libros.csv"));

        // Cargar datos en la tabla
        data = FXCollections.observableArrayList(service.listar());
        tablaLibros.setItems(data);
    }

    /**
     * Refresca la tabla después de operaciones CRUD.
     */
    public void refrescarTabla() {
        data.setAll(service.listar());
    }
}