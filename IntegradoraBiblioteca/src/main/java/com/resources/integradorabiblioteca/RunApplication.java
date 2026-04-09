package com.resources.integradorabiblioteca;

import com.resources.integradorabiblioteca.controllers.MainControllers;
import com.resources.integradorabiblioteca.repositories.*;
import com.resources.integradorabiblioteca.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que inicializa y arranca la aplicación JavaFX.
 * Se encarga de instanciar los repositorios, los servicios y configurar la vista principal.
 */
public class RunApplication extends Application {

    /**
     * Método principal del ciclo de vida de JavaFX.
     * Configura el escenario (ventana), carga el archivo FXML e inyecta las dependencias al controlador.
     *
     * @param stage El escenario principal (ventana) proporcionado por JavaFX.
     * @throws Exception Si ocurre un error al cargar el archivo FXML u otros recursos.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FileRepository libRepo = new FileRepository("data/libros.csv");
        ResenaRepository resRepo = new ResenaRepository("data/resenas.csv");

        LibraryService libService = new LibraryService(libRepo);
        ResenaService resService = new ResenaService(resRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        stage.setScene(new Scene(loader.load()));

        MainControllers mainCtrl = loader.getController();
        mainCtrl.setServicios(libService, resService);

        stage.setTitle("Sistema de Gestión de Biblioteca v1.0");
        stage.show();
    }

    /**
     * Punto de entrada estándar para la ejecución de la aplicación.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}