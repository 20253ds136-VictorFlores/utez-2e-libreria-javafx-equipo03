package com.resources.integradorabiblioteca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase fundacional que inicializa el sub-sistema de JavaFX y despliega la matriz visual inicial.
 */
public class RunApplication extends Application {


    /**
     * Inyecta la resolucion fundamental y el recurso FXML raiz en la primera etapa visual mostrada.
     * @param stage Contenedor primario instanciado nativamente por JavaFX.
     * @throws IOException Si ocurre un error resolviendo el recurso "main-view.fxml".
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);
        stage.setTitle("Sistema de Gestión - Biblioteca Escolar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
