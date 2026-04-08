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

public class RunApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Dentro de tu método start() en RunApplication.java

        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        FileRepository libRepo = new FileRepository("data/libros.csv");
        LibraryService libService = new LibraryService(libRepo);

        ResenaRepository resRepo = new ResenaRepository("data/resenas.csv");
        ResenaService resService = new ResenaService(resRepo);

        MainControllers mainController = fxmlLoader.getController();
        mainController.setServicios(libService, resService);

        stage.setTitle("Biblioteca");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}