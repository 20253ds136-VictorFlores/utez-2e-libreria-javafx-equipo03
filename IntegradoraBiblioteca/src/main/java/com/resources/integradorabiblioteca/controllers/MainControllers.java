package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal de la interfaz de usuario.
 * Actúa como orquestador entre la vista del catálogo y los servicios de negocio,
 * gestionando el ciclo de vida de las ventanas secundarias y la persistencia visual.
 */
public class MainControllers {

    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor, colGenero;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    private LibraryService libraryService;
    private ResenaService resenaService;

    /**
     * Inicializa la configuración de las columnas de la tabla.
     * Vincula cada columna con su propiedad correspondiente en {@link LibroModel}.
     */
    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIsbn()));
        colTitulo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitulo()));
        colAutor.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getAutor()));
        colAnio.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getAnio()).asObject());
        colGenero.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getGenero()));

        // Formateo visual: Convierte true/false en "Disponible/Prestado" o similar si se desea
        colDisponible.setCellValueFactory(cd -> new SimpleBooleanProperty(cd.getValue().isDisponible()).asObject());
    }

    /**
     * Inyecta las dependencias de servicio y sincroniza la tabla con los datos persistidos.
     * * @param libraryService Servicio de gestión de libros.
     * @param resenaService Servicio de gestión de reseñas.
     */
    public void setServicios(LibraryService libraryService, ResenaService resenaService) {
        this.libraryService = libraryService;
        this.resenaService = resenaService;
        refrescarTabla();
    }

    /**
     * Abre el formulario para registrar un nuevo ejemplar.
     */
    @FXML
    private void onNuevo() {
        abrirFormulario(null);
    }

    /**
     * Gestiona la edición rápida de disponibilidad del libro seleccionado mediante un diálogo.
     */
    @FXML
    private void onEditar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Selección requerida", "Por favor, selecciona un libro para editar su estado.");
            return;
        }

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Cambiar Disponibilidad");
        dialog.setHeaderText("Libro: " + seleccionado.getTitulo() + "\nISBN: " + seleccionado.getIsbn());
        dialog.setContentText("Seleccione el nuevo estado del ejemplar:");

        ButtonType btnDisp = new ButtonType("Disponible");
        ButtonType btnPrest = new ButtonType("Prestado");
        ButtonType btnCanc = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(btnDisp, btnPrest, btnCanc);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() != btnCanc) {
            seleccionado.setDisponible(resultado.get() == btnDisp);
            libraryService.actualizar(seleccionado);
            refrescarTabla();
        }
    }

    /**
     * Elimina permanentemente un libro tras la confirmación del usuario.
     */
    @FXML
    private void onEliminar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro de eliminar el libro: " + seleccionado.getTitulo() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                libraryService.eliminar(seleccionado.getIsbn());
                refrescarTabla();
                mostrarMensaje("Éxito", "Libro eliminado correctamente.");
            }
        } else {
            mostrarError("Selección requerida", "Debe seleccionar un libro de la lista.");
        }
    }

    /**
     * Despliega la vista detallada del libro seleccionado inyectando el servicio de reseñas.
     */
    @FXML
    private void onVerDetalle() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Selección requerida", "Seleccione un libro para ver sus detalles y reseñas.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
            Parent root = loader.load();

            DetailControllers controller = loader.getController();
            if (resenaService != null) {
                controller.cargarDatos(seleccionado, resenaService);
                mostrarVentanaModal("Detalles del Libro", root);
            }
        } catch (IOException e) {
            mostrarError("Error de Vista", "No se pudo cargar la pantalla de detalles.");
        }
    }

    /**
     * Genera un archivo de texto con el estado actual del inventario.
     */
    @FXML
    private void onExportarReporte() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Inventario");
        fileChooser.setInitialFileName("reporte_biblioteca.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto Plano", "*.txt"));

        File file = fileChooser.showSaveDialog(tablaLibros.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                List<LibroModel> lista = libraryService.listar();
                long disponibles = lista.stream().filter(LibroModel::isDisponible).count();

                writer.println("==========================================");
                writer.println("      INVENTARIO DE BIBLIOTECA           ");
                writer.println("==========================================");
                writer.println("Fecha de generación: " + java.time.LocalDate.now());
                writer.println();

                lista.forEach(l -> writer.printf("[%s] %-25s | ISBN: %s%n",
                        l.isDisponible() ? "DISP" : "PRES", l.getTitulo(), l.getIsbn()));

                writer.println("\n------------------------------------------");
                writer.println("Resumen: " + lista.size() + " libros totales.");
                writer.println("Disponibles: " + disponibles + " | Prestados: " + (lista.size() - disponibles));

                mostrarMensaje("Reporte Generado", "El archivo se guardó en: " + file.getName());
            } catch (IOException e) {
                mostrarError("Error de Exportación", "No se pudo escribir el archivo.");
            }
        }
    }

    /**
     * Centraliza la lógica para abrir el formulario de gestión de libros.
     */
    private void abrirFormulario(LibroModel libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Parent root = loader.load();

            FormControllers controller = loader.getController();
            controller.setService(this.libraryService);
            controller.setMainController(this);

            if (libro != null) controller.cargarLibro(libro);

            mostrarVentanaModal(libro == null ? "Nuevo Registro" : "Editar Registro", root);
        } catch (IOException e) {
            mostrarError("Error de Sistema", "Error al cargar el formulario.");
        }
    }

    /**
     * Sincroniza la tabla visual con la fuente de datos persistente.
     */
    public void refrescarTabla() {
        if (libraryService != null) {
            tablaLibros.getItems().setAll(libraryService.listar());
            tablaLibros.refresh();
        }
    }

    // --- Utilidades de Interfaz de Usuario ---

    private void mostrarVentanaModal(String titulo, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}