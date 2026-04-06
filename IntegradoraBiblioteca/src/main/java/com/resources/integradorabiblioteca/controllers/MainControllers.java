package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.Libro;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controlador principal de la interfaz.
 * Gestiona tabla de libros y acciones de usuario.
 */
public class MainControllers {
    @FXML private TableView<Libro> tableBooks;
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colAutor, colGenero;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, Boolean> colDisponible;

    private LibraryService service;

    /**
     * Inicializa el controlador y carga datos.
     */
    public void initialize() {
        service = new LibraryService();
        configurarColumnas();
        actualizarTabla();
    }

    /**
     * Configura las columnas de la tabla.
     */
    private void configurarColumnas() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
    }

    /**
     * Refresca la tabla con el catálogo actual.
     */
    public void actualizarTabla() {
        tableBooks.setItems(FXCollections.observableArrayList(service.getCatalogo()));
    }

    /**
     * Acción para crear un nuevo registro.
     */
    @FXML
    private void onNewClick() {
        abrirFormulario(null);
    }

    /**
     * Acción para editar un registro seleccionado.
     */
    @FXML
    private void onEditClick() {
        Libro seleccionado = tableBooks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Atención", "Seleccione un registro para editar.");
        }
    }

    /**
     * Acción para eliminar un registro seleccionado.
     */
    @FXML
    private void onDeleteClick() {
        Libro seleccionado = tableBooks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                service.eliminarLibro(seleccionado.getIsbn());
                actualizarTabla();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar el registro: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Atención", "Seleccione un registro para eliminar.");
        }
    }

    /**
     * Acción para mostrar detalles de un registro.
     */
    @FXML
    private void onDetailClick() {
        Libro seleccionado = tableBooks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
                Parent root = loader.load();

                DetailControllers controlador = loader.getController();
                controlador.cargarDatos(seleccionado);

                Stage stage = new Stage();
                stage.setTitle("Detalles del Registro");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Acción para exportar el catálogo a reporte.
     */
    @FXML
    private void onExportClick() {
        try {
            service.exportarReporte();
            mostrarAlerta("Exportación Exitosa", "Se generó el archivo reporte_catalogo.csv en tu carpeta de Descargas.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Fallo al exportar el reporte.");
        }
    }

    /**
     * Abre formulario para crear o editar libro.
     * @param libro libro a editar, null si es nuevo
     */
    private void abrirFormulario(Libro libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Parent root = loader.load();

            FormControllers controlador = loader.getController();
            controlador.inicializarDatos(service, libro, this);

            Stage stage = new Stage();
            stage.setTitle(libro == null ? "Nuevo Registro" : "Modificar Registro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta informativa.
     * @param titulo  título de la ventana
     * @param mensaje contenido del mensaje
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
