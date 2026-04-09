package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.List;

public class ReportExporter {
    public void exportarCSV(List<LibroModel> libros) throws IOException {
        String rutaDescargas = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "reporte_biblioteca.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaDescargas))) {
            pw.println("ISBN;Titulo;Autor;Año;Genero;Disponible");
            for (LibroModel l : libros) {
                pw.println(l.getIsbn() + ";" + l.getTitulo() + ";" + l.getAutor() + ";" + l.getAnio() + ";" + l.getGenero() + ";" + (l.isDisponible()?"Si":"No"));
            }
        }
    }
}