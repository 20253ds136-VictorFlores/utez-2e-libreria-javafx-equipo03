package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import java.util.function.Predicate;

/**
 * Controlador principal de la interfaz.
 * Gestiona la tabla de libros, las busquedas y la coordinacion entre ventanas.
 */
public class MainControllers {

    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TextField txtBusqueda;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    private LibraryService libService;
    private ResenaService resService;

    private ObservableList<LibroModel> masterData = FXCollections.observableArrayList();

    /**
     * Configura los servicios y establece la logica de las columnas y el filtro de busqueda.
     * @param libraryService Servicio para la gestion de libros.
     * @param resenaService Servicio para la gestion de resenas.
     */
    public void setServicios(LibraryService libraryService, ResenaService resenaService) {
        this.libService = libraryService;
        this.resService = resenaService;

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        FilteredList<LibroModel> filteredData = new FilteredList<>(masterData, null);

        txtBusqueda.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String valorAnterior, String valorNuevo) {
                filteredData.setPredicate(new Predicate<LibroModel>() {
                    @Override
                    public boolean test(LibroModel libro) {
                        if (valorNuevo == null || valorNuevo.isEmpty()) {
                            return true;
                        }

                        String filtroLowerCase = valorNuevo.toLowerCase();
                        return libro.getTitulo().toLowerCase().contains(filtroLowerCase) ||
                                libro.getIsbn().toLowerCase().contains(filtroLowerCase);
                    }
                });
            }
        });

        tablaLibros.setItems(filteredData);
        refrescarTabla();
    }

    /**
     * Actualiza la lista de la tabla con los datos mas recientes del servicio.
     */
    public void refrescarTabla() {
        if (libService != null) {
            masterData.setAll(libService.listar());
        }
    }

    /**
     * Genera el reporte de inventario en formato de texto.
     */
    @FXML
    private void onExportarReporte() {
        try {
            libService.generarReporte();
            Alert alertaExito = new Alert(Alert.AlertType.INFORMATION, "Reporte generado en Descargas.");
            alertaExito.show();
        } catch (Exception e) {
            Alert alertaError = new Alert(Alert.AlertType.ERROR, "Error al exportar: " + e.getMessage());
            alertaError.show();
        }
    }

    /**
     * Abre el formulario para registrar un nuevo libro.
     */
    @FXML
    private void onNuevo() {
        abrirVentana("/com/resources/integradorabiblioteca/form-view.fxml", "Nuevo Libro", null);
    }

    /**
     * Muestra la informacion detallada y las resenas del libro seleccionado.
     */
    @FXML
    private void onVerDetalle() {
        LibroModel libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Detalles del Registro");
                stage.setScene(new Scene(root));

                DetailControllers controladorDetalle = fxmlLoader.getController();
                controladorDetalle.cargarDatos(libroSeleccionado, resService);

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga el formulario con los datos del libro para su modificacion.
     */
    @FXML
    private void onEditar() {
        LibroModel libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            abrirVentana("/com/resources/integradorabiblioteca/form-view.fxml", "Modificar Registro", libroSeleccionado);
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Seleccione un registro para editar.");
            alerta.show();
        }
    }

    /**
     * Lanza el proceso de confirmacion de seguridad antes de eliminar un libro.
     */
    @FXML
    private void onEliminar() {
        LibroModel libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/delete-confirm-view.fxml"));
                Parent root = fxmlLoader.load();

                DeleteConfirmControllers controladorConfirmacion = fxmlLoader.getController();
                controladorConfirmacion.inicializarDatos(libroSeleccionado.getIsbn());

                Stage stage = new Stage();
                stage.setTitle("Verificacion");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controladorConfirmacion.isConfirmado()) {
                    libService.eliminar(libroSeleccionado.getIsbn());
                    refrescarTabla();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo generico para la apertura de modales de formulario.
     */
    private void abrirVentana(String rutaFxml, String titulo, LibroModel libro) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));

            FormControllers controladorFormulario = fxmlLoader.getController();
            controladorFormulario.setService(libService);
            controladorFormulario.setMainController(this);

            if (libro != null) {
                controladorFormulario.cargarLibro(libro);
            }

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}