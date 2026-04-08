package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportExporter {
    private final String filePath;

    public ReportExporter(String filePath) {
        this.filePath = filePath;
    }

    public void export(List<LibroModel> libros) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ISBN;Título;Autor;Año;Género;Disponible\n");
            for (LibroModel libro : libros) {
                writer.write(libro.getIsbn() + ";" + libro.getTitulo() + ";" + libro.getAutor() + ";" +
                        libro.getAnio() + ";" + libro.getGenero() + ";" + libro.isDisponible() + "\n");
            }
        }
    }
}