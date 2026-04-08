package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Clase utilitaria encargada de exportar el catálogo de libros a un archivo externo.
 * Permite generar reportes en formato de texto plano (similar a un CSV),
 * incluyendo una cabecera con los nombres de las columnas para facilitar
 * su lectura en programas de hojas de cálculo como Excel o Google Sheets.
 */
public class ReportExporter {

    /** Ruta del archivo donde se guardará el reporte exportado. */
    private final String filePath;

    /**
     * Constructor para inicializar el exportador de reportes.
     * * @param filePath La ruta y nombre del archivo que se va a generar (ej. "reporte.csv").
     */
    public ReportExporter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Exporta la lista de libros proporcionada al archivo especificado.
     * Escribe primero una fila de cabecera y luego itera sobre la lista de libros,
     * escribiendo los atributos de cada uno separados por punto y coma (;).
     * * A diferencia del FileRepository, este método delega el manejo de excepciones
     * mediante la cláusula `throws`, permitiendo que la interfaz de usuario
     * sea la encargada de mostrar una alerta si ocurre un error.
     *
     * * @param libros La lista de instancias de {@link LibroModel} que se desea exportar.
     * @throws IOException Si ocurre un error de entrada/salida al intentar crear o escribir en el archivo.
     */
    public void export(List<LibroModel> libros) throws IOException {
        // Se utiliza try-with-resources para asegurar que el FileWriter se cierre automáticamente
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir la cabecera del reporte
            writer.write("ISBN;Título;Autor;Año;Género;Disponible\n");

            // Escribir los datos de cada libro
            for (LibroModel libro : libros) {
                writer.write(libro.getIsbn() + ";" + libro.getTitulo() + ";" + libro.getAutor() + ";" +
                        libro.getAnio() + ";" + libro.getGenero() + ";" + libro.isDisponible() + "\n");
            }
        }
    }
}