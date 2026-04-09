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
 * Controlador principal de la aplicación.
 * Gestiona la pantalla de inicio, el catálogo visual de libros y la
 * coordinación de eventos para la navegación hacia módulos secundarios.
 */
public class MainControllers {

    // --- Componentes vinculados al archivo FXML ---
    @FXML private TableView<LibroModel> tablaLibros;
    @FXML private TextField txtBusqueda;
    @FXML private TableColumn<LibroModel, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<LibroModel, Integer> colAnio;

    // --- Dependencias de la lógica de negocio ---
    private LibraryService libService;
    private ResenaService resService;

    /** Lista observable que actúa como puente directo entre la memoria y la tabla visual. */
    private ObservableList<LibroModel> masterData = FXCollections.observableArrayList();

    /**
     * Inicializa la configuración de la tabla y establece los servicios.
     * Configura el filtrado reactivo de búsqueda utilizando clases anónimas.
     * * @param ls Servicio para la gestión de libros.
     * @param rs Servicio para la gestión de reseñas.
     */
    public void setServicios(LibraryService ls, ResenaService rs) {
        this.libService = ls;
        this.resService = rs;

        // Vinculación de columnas: conecta los atributos de LibroModel con la TableView.
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        /**
         * Implementación de Búsqueda Reactiva:
         * Se crea una FilteredList que envuelve a la lista maestra.
         */
        FilteredList<LibroModel> filteredData = new FilteredList<>(masterData, null);

        // Agregamos un escucha al campo de texto mediante una Clase Anónima.
        txtBusqueda.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String old, String val) {
                // Definimos la regla de filtrado mediante otra Clase Anónima (Predicate).
                filteredData.setPredicate(new Predicate<LibroModel>() {
                    @Override
                    public boolean test(LibroModel libro) {
                        // Si el buscador está vacío, se muestran todos los libros.
                        if (val == null || val.isEmpty()) return true;

                        String f = val.toLowerCase();
                        // El libro se muestra si el título o el ISBN coinciden con la búsqueda.
                        return libro.getTitulo().toLowerCase().contains(f) ||
                                libro.getIsbn().contains(f);
                    }
                });
            }
        });

        // Se asigna la lista filtrada a la tabla para que responda a las búsquedas.
        tablaLibros.setItems(filteredData);
        refrescarTabla();
    }

    /**
     * Sincroniza el contenido de la tabla con los datos actuales del servicio.
     * Limpia la lista observable y la vuelve a poblar con el inventario actualizado.
     */
    public void refrescarTabla() {
        if (libService != null) {
            masterData.setAll(libService.listar());
        }
    }

    /**
     * Dispara la lógica de generación de reportes externos.
     * Muestra alertas visuales de éxito o error según el resultado del proceso.
     */
    @FXML
    private void onExportarReporte() {
        try {
            libService.generarReporte();
            new Alert(Alert.AlertType.INFORMATION, "Reporte generado en Descargas.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    /**
     * Prepara la apertura del formulario en modo "Creación".
     */
    @FXML
    private void onNuevo() {
        abrirVentana("/com/resources/integradorabiblioteca/form-view.fxml", "Nuevo Libro", null);
    }

    /**
     * Obtiene el libro seleccionado en la tabla y abre la vista de detalles.
     * Envía la referencia del libro y el servicio de reseñas a la nueva ventana.
     */
    @FXML
    private void onVerDetalle() {
        LibroModel sel = tablaLibros.getSelectionModel().getSelectedItem();
        if (sel != null) {
            try {
                FXMLLoader l = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/detail-view.fxml"));
                Stage st = new Stage();
                st.setTitle("Detalles");
                st.setScene(new Scene(l.load()));

                // Inyección de datos y servicios en el controlador de destino.
                ((DetailControllers)l.getController()).cargarDatos(sel, resService);
                st.show();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Método genérico para la apertura de ventanas secundarias.
     * Centraliza la lógica de carga de FXML y configuración de controladores.
     * * @param fxml   Ruta del recurso de vista.
     * @param titulo Texto para la barra superior de la ventana.
     * @param libro  Instancia de libro (opcional) en caso de edición.
     */
    private void abrirVentana(String fxml, String titulo, LibroModel libro) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Stage st = new Stage();
            st.setTitle(titulo);
            st.setScene(new Scene(l.load()));

            // Configuración inicial del controlador de la nueva ventana.
            FormControllers c = l.getController();
            c.setService(libService);
            c.setMainController(this);

            if (libro != null) c.cargarLibro(libro);

            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}