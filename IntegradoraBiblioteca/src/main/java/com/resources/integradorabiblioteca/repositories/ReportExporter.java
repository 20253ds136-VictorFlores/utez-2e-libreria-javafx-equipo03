package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.*;
import java.io.BufferedWriter;
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
    public void exportarCatalogo(List<Book> listaLibros) throws IOException {
        BufferedWriter escritor = new BufferedWriter(new FileWriter("reporte_catalogo.csv"));

        escritor.write("ISBN,Titulo,Autor,Anio,Genero,Disponible");
        escritor.newLine();

        for (Book libro : listaLibros) {
            escritor.write(libro.toString());
            escritor.newLine();
        }
        escritor.close();
    }
}
