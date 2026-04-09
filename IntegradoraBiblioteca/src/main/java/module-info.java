module com.resources.integradorabiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires org.kordamp.bootstrapfx.core;

    opens com.resources.integradorabiblioteca.controllers to javafx.fxml;
    opens com.resources.integradorabiblioteca.model to javafx.base;

    exports com.resources.integradorabiblioteca;
}