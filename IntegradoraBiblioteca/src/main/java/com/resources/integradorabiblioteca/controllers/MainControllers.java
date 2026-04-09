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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.function.Predicate;

/**
 * Controlador que gestiona la cuadricula de datos de la interfaz grafica,
 * procesando flujos de creacion, busqueda, edicion, y eliminacion de la coleccion principal.
 */
public class MainControllers {

    @FXML private TableView<Libro> tableBooks;
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colAutor, colGenero;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, Boolean> colDisponible;
    @FXML private TextField txtBusqueda;

    private LibraryService service;
    private ObservableList<Libro> listaObservable;
    private FilteredList<Libro> listaFiltrada;

    /**
     * Rutina principal de inicializacion de componentes inyectados por JavaFX.
     * Enlaza el repositorio logico con los observadores de la interfaz.
     */
    @FXML
    public void initialize() {
        service = new LibraryService();
        configurarColumnas();
        listaObservable = FXCollections.observableArrayList(service.getCatalogo());

        listaFiltrada = new FilteredList<>(listaObservable, new Predicate<Libro>() {
            @Override
            public boolean test(Libro b) {
                return true;
            }
        });

        tableBooks.setItems(listaFiltrada);
        configurarBuscador();
    }

    /**
     * Mapea las propiedades de la clase Libro contra las celdas respectivas de TableColumn.
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
     * Sincroniza la lista observable en memoria con el ultimo estado capturado desde el servicio logico.
     */
    public void actualizarTabla() {
        listaObservable.setAll(service.getCatalogo());
    }

    /**
     * Invoca el despliegue de la ventana de formulario en modo "Alta".
     */
    @FXML
    private void onNewClick() {
        abrirFormulario(null);
    }

    /**
     * Invoca el despliegue de la ventana de formulario inyectando la informacion del renglon actualmente remarcado en la tabla.
     */
    @FXML
    private void onEditClick() {
        Libro seleccionado = tableBooks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Atencion", "Seleccione un registro para editar.");
        }
    }

    /**
     * Inicia un proceso de doble capa de seguridad para la supresion de registros,
     * cargando un controlador externo para la primer capa.
     */
    @FXML
    private void onDeleteClick() {
        Libro seleccionado = tableBooks.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/resources/integradorabiblioteca/delete-confirm-view.fxml"));
                Parent root = loader.load();

                DeleteConfirmControllers controller = loader.getController();
                controller.inicializarDatos(seleccionado.getIsbn());

                Stage stage = new Stage();
                stage.setTitle("Verificacion de Seguridad");
                stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.showAndWait();

                if (controller.isConfirmado()) {
                    Alert confirmacionFinal = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacionFinal.setTitle("Confirmacion Final");
                    confirmacionFinal.setHeaderText("Accion irreversible");
                    confirmacionFinal.setContentText("¿Esta seguro de eliminar '" + seleccionado.getTitulo() + "'?");

                    if (confirmacionFinal.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                        service.eliminarLibro(seleccionado.getIsbn());
                        actualizarTabla();
                        mostrarAlerta("Exito", "El registro se elimino correctamente.");
                    }
                }
            } catch (Exception e) {
                mostrarAlerta("Error Critico", "No se pudo cargar la vista de confirmacion: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Atencion", "Seleccione un registro para eliminar.");
        }
    }

    /**
     * Genera un escenario detallado para el registro capturado,
     * el cual opera estrictamente de forma visual (solo-lectura).
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
                mostrarAlerta("Error Critico", "Error al abrir detalles: " + e.getMessage());
            }
        }
    }

    /**
     * Llama al delegado de exportacion del servicio base.
     */
    @FXML
    private void onExportClick() {
        try {
            service.exportarReporte();
            mostrarAlerta("Exportacion Exitosa", "Se genero el archivo reporte_libros_disponibles.csv en tu carpeta de Descargas.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Fallo al exportar el reporte.");
        }
    }

    /**
     * Centraliza la instanciacion de la vista de formulario, compartiendo el contexto necesario de estado a traves de los modulos.
     * @param libro Entidad a incrustar en el modo de edicion. De ser null el formulario abrira vacio.
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
            mostrarAlerta("Error", "Fallo cargando vista de formulario: " + e.getMessage());
        }
    }

    /**
     * Construye y expone una ventana emergente de tipo informacion al usuario en el hilo visual.
     * @param titulo Encabezado descriptivo de la ventana informativa.
     * @param mensaje Cadena de texto correspondiente al cuerpo de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Vincula un Listener para recalcular el predicado de la lista filtrada de
     * manera reactiva contra el campo de busqueda de texto
     */
    private void configurarBuscador() {
        txtBusqueda.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                listaFiltrada.setPredicate(new Predicate<Libro>() {
                    @Override
                    public boolean test(Libro libro) {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String filtroLowerCase = newValue.toLowerCase();
                        if (libro.getTitulo().toLowerCase().contains(filtroLowerCase)) {
                            return true;
                        } else if (libro.getIsbn().toLowerCase().contains(filtroLowerCase)) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }
}
