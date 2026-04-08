package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Clase utilitaria encargada de la generación de reportes externos del catálogo.
 * A diferencia del repositorio de persistencia, esta clase está optimizada para
 * la legibilidad humana y la compatibilidad con software de hojas de cálculo.
 */
public class ReportExporter {

    private final String filePath;
    private static final String SEPARADOR = ";";

    /**
     * Inicializa el exportador con la ruta de destino definida por el usuario.
     * @param filePath Ruta y nombre del archivo a generar (ej. "reporte_biblioteca.csv").
     */
    public ReportExporter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Exporta la colección de libros a un archivo de texto plano.
     * Utiliza un formato delimitado por punto y coma e incluye encabezados descriptivos.
     * * @param libros Lista de {@link LibroModel} a procesar.
     * @throws IOException Si existe un fallo en la creación o escritura del archivo,
     * permitiendo que el controlador gestione la alerta visual.
     */
    public void export(List<LibroModel> libros) throws IOException {
        // El uso de try-with-resources garantiza el cierre del flujo incluso ante errores
        try (FileWriter writer = new FileWriter(filePath)) {

            // Definición de cabecera profesional
            writer.write("ISBN" + SEPARADOR + "Título" + SEPARADOR + "Autor" + SEPARADOR +
                    "Año" + SEPARADOR + "Género" + SEPARADOR + "Disponible" + System.lineSeparator());

            // Iteración y escritura de registros
            for (LibroModel libro : libros) {
                StringBuilder sb = new StringBuilder();
                sb.append(libro.getIsbn()).append(SEPARADOR)
                        .append(libro.getTitulo()).append(SEPARADOR)
                        .append(libro.getAutor()).append(SEPARADOR)
                        .append(libro.getAnio()).append(SEPARADOR)
                        .append(libro.getGenero()).append(SEPARADOR)
                        .append(libro.isDisponible() ? "Sí" : "No") // Formateo amigable para el reporte
                        .append(System.lineSeparator());

                writer.write(sb.toString());
            }
        }
    }
}