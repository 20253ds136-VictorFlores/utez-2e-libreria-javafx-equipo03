package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exporta el catálogo de libros a un archivo CSV.
 */
public class ReportExporter {

    /**
     * Genera un archivo CSV con los datos del catálogo.
     * @param listaLibros colección de libros a exportar
     * @throws IOException si ocurre un error de escritura
     */
    public void exportarCatalogo(List<Libro> listaLibros) throws IOException {
        String carpetaUsuario = System.getProperty("user.home");
        String rutaDescargas = carpetaUsuario + File.separator + "Downloads" + File.separator + "reporte_catalogo.csv";

        BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaDescargas));

        escritor.write("ISBN,Titulo,Autor,Anio,Genero,Disponible");
        escritor.newLine();

        for (Libro libro : listaLibros) {
            escritor.write(libro.toString());
            escritor.newLine();
        }
        escritor.close();
    }
}
