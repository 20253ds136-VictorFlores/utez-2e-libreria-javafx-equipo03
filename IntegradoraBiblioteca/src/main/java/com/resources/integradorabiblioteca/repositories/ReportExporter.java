package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exporta el catalogo de libros a un archivo CSV
 */
public class ReportExporter {

    /**
     * Convierte una coleccion especifica de libros y fabrica un documento tipo CSV dentro del directorio del usuario actual.
     * * @param listaLibros Coleccion ya pre-filtrada o completa a vaciar en el archivo final.
     * @throws IOException En caso de fallas de escritura de buffer en el directorio objetivo.
     */
    public void exportarCatalogo(List<Libro> listaLibros) throws IOException {
        String rutaDescargas = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "reporte_libros_disponibles.csv";

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaDescargas))) {
            escritor.write("ISBN,Titulo,Autor,Año,Genero,Disponible");
            escritor.newLine();

            for (Libro libro : listaLibros) {
                escritor.write(libro.toString());
                escritor.newLine();
            }
        }
    }
}
