package com.resources.integradorabiblioteca;

import com.resources.integradorabiblioteca.controllers.MainControllers;
import com.resources.integradorabiblioteca.repositories.*;
import com.resources.integradorabiblioteca.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RunApplication extends Application {
    @Override
    public void start(Stage st) throws Exception {
        LibraryService ls = new LibraryService(new FileRepository("data/libros.csv"));
        ResenaService rs = new ResenaService(new ResenaRepository("data/resenas.csv"));

        FXMLLoader l = new FXMLLoader(getClass().getResource("main-view.fxml"));
        st.setScene(new Scene(l.load()));
        ((MainControllers)l.getController()).setServicios(ls, rs);
        st.setTitle("Biblioteca Integrada");
        st.show();
    }
    public static void main(String[] args) { launch(); }
}