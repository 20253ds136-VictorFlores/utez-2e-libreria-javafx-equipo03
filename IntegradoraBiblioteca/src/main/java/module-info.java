module com.resources.integradorabiblioteca {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.resources.integradorabiblioteca to javafx.fxml;
    exports com.resources.integradorabiblioteca;
}