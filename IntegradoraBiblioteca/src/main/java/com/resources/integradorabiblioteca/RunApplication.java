package com.resources.integradorabiblioteca;

import com.resources.integradorabiblioteca.controllers.MainControllers;
import com.resources.integradorabiblioteca.repositories.FileRepository;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import com.resources.integradorabiblioteca.services.LibraryService;
import com.resources.integradorabiblioteca.services.ResenaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que orquesta el arranque de la aplicación.
 * Se encarga de instanciar los repositorios y servicios, inyectarlos en el
 * controlador principal y configurar el escenario (Stage) inicial de JavaFX.
 */
public class RunApplication extends Application {

    /**
     * Punto de inicio de la interfaz gráfica.
     * Configura la jerarquía de objetos y carga la vista principal.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // 1. Carga de la interfaz FXML
        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 2. Inicialización de la capa de Persistencia y Negocio para Libros
        FileRepository libRepo = new FileRepository("data/libros.csv");
        LibraryService libService = new LibraryService(libRepo);

        // 3. Inicialización de la capa de Persistencia y Negocio para Reseñas
        ResenaRepository resRepo = new ResenaRepository("data/resenas.csv");
        ResenaService resService = new ResenaService(resRepo);

        // 4. Inyección de dependencias en el Controlador Principal
        // Obtenemos la instancia del controlador creada por el fxmlLoader
        MainControllers mainController = fxmlLoader.getController();
        mainController.setServicios(libService, resService);

        // 5. Configuración final de la ventana
        stage.setTitle("Sistema de Gestión de Biblioteca");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método main estándar de Java para lanzar la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }
}