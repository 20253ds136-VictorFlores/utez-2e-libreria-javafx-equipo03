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
 * Controlador principal de la aplicación.
 * Gestiona la vista del catálogo (tabla) y enruta las acciones del usuario
 * (nuevo, editar, eliminar, ver detalles) hacia las ventanas correspondientes.
 */
public class MainControllers {

    // --- Componentes de la Interfaz (Inyectados desde main-view.fxml) ---
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TableColumn<LibroModel, String> colIsbn;
    @FXML private TableColumn<LibroModel, String> colTitulo;
    @FXML private TableColumn<LibroModel, String> colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;
    @FXML private TableColumn<LibroModel, String> colGenero;
    @FXML private TableColumn<LibroModel, Boolean> colDisponible;

    // --- Servicios de Lógica de Negocio ---
    private LibraryService libraryService;
    private ResenaService resenaService;

    /**
     * Método que JavaFX ejecuta automáticamente al cargar la vista.
     * Sirve para vincular las columnas de la tabla con los atributos del LibroModel.
     */
    @FXML
    public void initialize() {
        colIsbn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));
        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        colAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        colAnio.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAnio()).asObject());
        colGenero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenero()));
        colDisponible.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDisponible()).asObject());
    }

    /**
     * Inyecta los servicios necesarios para el funcionamiento del controlador principal.
     * Al recibir los servicios, automáticamente llena la tabla con los datos.
     * @param libraryService Servicio de libros.
     * @param resenaService Servicio de reseñas.
     */
    public void setServicios(LibraryService libraryService, ResenaService resenaService) {
        this.libraryService = libraryService;
        this.resenaService = resenaService;

        // ¡IMPORTANTE! Llamamos a refrescarTabla aquí para que
        // los libros que ya existían en el CSV aparezcan al abrir el programa.
        refrescarTabla();
    }

    /**
     * Acción del botón "Nuevo". Abre el formulario vacío para registrar un libro.
     */
    @FXML
    private void onNuevo() {
        abrirFormulario(null);
    }

    /**
     * Acción del botón "Editar". Abre el formulario cargando los datos del libro seleccionado.
     */
    @FXML
    private void onEditar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Editar Disponibilidad");

            // AQUÍ USAMOS LOS MÉTODOS DEL MODELO
            dialog.setHeaderText("Libro: " + seleccionado.getTitulo() + "\nISBN: " + seleccionado.getIsbn());
            dialog.setContentText("Selecciona el nuevo estado:");

            ButtonType btnDisponible = new ButtonType("Disponible");
            ButtonType btnPrestado = new ButtonType("Prestado");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getButtonTypes().setAll(btnDisponible, btnPrestado, btnCancelar);

            Optional<ButtonType> resultado = dialog.showAndWait();
            if (resultado.isPresent()) {
                if (resultado.get() == btnDisponible) {
                    seleccionado.setDisponible(true);
                } else if (resultado.get() == btnPrestado) {
                    seleccionado.setDisponible(false);
                } else {
                    return;
                }

                libraryService.actualizar(seleccionado);
                refrescarTabla();
            }
        }
    }

    /**
     * Acción del botón "Eliminar". Elimina el libro seleccionado de la tabla y del archivo.
     */
    @FXML
    private void onEliminar() {
        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            // 1. Pedir confirmación al usuario (Buena práctica)
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de eliminar este libro?", ButtonType.YES, ButtonType.NO);
            confirmacion.showAndWait();

            if (confirmacion.getResult() == ButtonType.YES) {
                // 2. Borrar del servicio (esto lo borra del CSV)
                libraryService.eliminar(seleccionado.getIsbn());

                refrescarTabla();
                mostrarMensaje("Libro eliminado con éxito.");
            }
        } else {
            mostrarMensaje("Por favor, selecciona un libro de la tabla.");
        }
    }

    /**
     * Abre la ventana modal de detalles para el libro seleccionado,
     * inyectándole el servicio de reseñas para que busque sus comentarios.
     */
    // Dentro de MainControllers.java
    @FXML
    private void onVerDetalle() {
        System.out.println("DEBUG: Se presionó el botón Ver Detalle"); // Mira si esto sale en consola

        LibroModel seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un libro de la tabla.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
            Parent root = loader.load();

            DetailControllers controller = loader.getController();

            // Verificamos que los servicios no sean nulos
            if (resenaService != null) {
                controller.cargarDatos(seleccionado, resenaService);

                Stage stage = new Stage();
                stage.setTitle("Detalles de: " + seleccionado.getTitulo());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
                System.err.println("ERROR: resenaService es NULL. Revisa RunApplication.");
            }

        } catch (Exception e) {
            System.err.println("ERROR AL CARGAR LA VISTA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Acción del botón "Exportar reporte".
     */
    @FXML
    private void onExportarReporte() {
        // 1. Configurar el selector de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Biblioteca");
        fileChooser.setInitialFileName("reporte_biblioteca.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Texto", "*.txt"));

        // 2. Mostrar la ventana para elegir ruta
        File file = fileChooser.showSaveDialog(tablaLibros.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Cabecera del reporte
                writer.println("==========================================");
                writer.println("      REPORTE GENERAL DE BIBLIOTECA       ");
                writer.println("==========================================");
                writer.println("Fecha: " + java.time.LocalDate.now());
                writer.println();

                List<LibroModel> lista = libraryService.listar();
                int disponibles = 0;

                for (LibroModel libro : lista) {
                    String estado = libro.isDisponible() ? "[DISPONIBLE]" : "[PRESTADO]";
                    if (libro.isDisponible()) disponibles++;

                    writer.printf("ISBN: %s | %s - %s (%d) | %s%n",
                            libro.getIsbn(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getAnio(),
                            estado);
                }

                // Resumen final
                writer.println();
                writer.println("------------------------------------------");
                writer.println("Total de libros: " + lista.size());
                writer.println("Libros disponibles: " + disponibles);
                writer.println("Libros prestados: " + (lista.size() - disponibles));
                writer.println("==========================================");

                // Alerta de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reporte exportado con éxito en: " + file.getAbsolutePath());
                alert.setHeaderText(null);
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al crear el reporte: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Método auxiliar para cargar y mostrar la ventana del formulario (form-view.fxml).
     * @param libro Si es null, el formulario se abre vacío. Si tiene un libro, se abre para editar.
     */
    private void abrirFormulario(LibroModel libro) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/form-view.fxml"));
            Parent root = loader.load();

            FormControllers controller = loader.getController();

            controller.setService(this.libraryService);
            controller.setMainController(this);

            if (libro != null) {
                controller.cargarLibro(libro);
            }

            Stage stage = new Stage();
            stage.setTitle(libro == null ? "Nuevo Libro" : "Editar Libro");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos de la tabla (usado al iniciar y después de agregar/editar/eliminar).
     */
    public void refrescarTabla() {
        // Verificamos que el servicio no sea null para evitar errores
        if (libraryService != null) {
            // 1. Obtenemos la lista de libros desde el servicio
            // NOTA: Asegúrate de que en LibraryService tu método se llame 'listar'
            List<LibroModel> libros = libraryService.listar();

            // 2. Le pasamos esa lista a la tabla de JavaFX
            tablaLibros.getItems().setAll(libros);

            // 3. (Opcional) Forzamos el refresco visual
            tablaLibros.refresh();
        }
    }

    /**
     * Método auxiliar para mostrar cuadros de diálogo de error.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}