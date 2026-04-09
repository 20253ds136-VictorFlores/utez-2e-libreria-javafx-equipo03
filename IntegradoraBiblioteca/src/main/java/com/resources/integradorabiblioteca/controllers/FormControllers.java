package com.resources.integradorabiblioteca.controllers;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.services.LibraryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormControllers {
    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtGenero;
    @FXML private CheckBox chkDisponible;

    private LibraryService service;
    private MainControllers mainController; // Referencia a la pantalla principal
    private LibroModel editando;

    public void setService(LibraryService s) { this.service = s; }

    // ESTE ES EL MÉTODO QUE CAUSABA EL ERROR:
    public void setMainController(MainControllers m) { this.mainController = m; }

    public void cargarLibro(LibroModel l) {
        this.editando = l;
        txtIsbn.setText(l.getIsbn());
        txtIsbn.setEditable(false);
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtAnio.setText(String.valueOf(l.getAnio()));
        txtGenero.setText(l.getGenero());
        chkDisponible.setSelected(l.isDisponible());
    }

    @FXML
    private void onGuardar() {
        try {
            String isbn = txtIsbn.getText();
            String tit = txtTitulo.getText();
            String aut = txtAutor.getText();
            int anio = Integer.parseInt(txtAnio.getText());
            String gen = txtGenero.getText();
            boolean disp = chkDisponible.isSelected();

            if (editando == null) {
                service.agregar(new LibroModel(isbn, tit, aut, anio, gen, disp));
            } else {
                editando.setTitulo(tit);
                editando.setAutor(aut);
                editando.setAnio(anio);
                editando.setGenero(gen);
                editando.setDisponible(disp);
                service.actualizar(editando);
            }

            // Refrescamos la tabla usando la referencia
            if (mainController != null) mainController.refrescarTabla();

            ((Stage) txtIsbn.getScene().getWindow()).close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML private void onCancelar() { ((Stage) txtIsbn.getScene().getWindow()).close(); }
}