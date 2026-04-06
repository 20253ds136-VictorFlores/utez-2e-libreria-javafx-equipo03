module com.resources.integradorabiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.kordamp.bootstrapfx.core;

    opens com.resources.integradorabiblioteca to javafx.fxml;
    exports com.resources.integradorabiblioteca;

    opens com.resources.integradorabiblioteca.controllers to javafx.fxml;
    exports com.resources.integradorabiblioteca.controllers;

    opens com.resources.integradorabiblioteca.model to javafx.base;
    exports com.resources.integradorabiblioteca.model;
}