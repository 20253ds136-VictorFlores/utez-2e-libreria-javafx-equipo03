package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de datos en el sistema de archivos local mediante formato CSV.
 * Esta clase se encarga de la serialización y deserialización de objetos {@link LibroModel},
 * asegurando que la estructura del archivo se mantenga íntegra.
 */
public class FileRepository {
    private final String filePath;
    private final String SEPARADOR = ";"; // Delimitador estándar para evitar conflictos con comas en títulos

    /**
     * Inicializa el repositorio y garantiza la existencia del archivo de destino.
     * @param filePath Ruta relativa o absoluta del archivo .csv
     */
    public FileRepository(String filePath) {
        this.filePath = filePath;
        verificarArchivo();
    }

    /**
     * Recupera la colección completa de libros almacenados en el disco.
     * Realiza validaciones de formato por cada línea para prevenir errores de carga.
     * @return Una lista de {@link LibroModel} con los datos recuperados.
     */
    public List<LibroModel> load() {
        List<LibroModel> libros = new ArrayList<>();
        File archivo = new File(filePath);

        if (!archivo.exists()) return libros;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue; // Salta líneas vacías

                String[] datos = linea.split(SEPARADOR);

                // Validación de integridad de columnas (Esperadas: 6)
                if (datos.length >= 6) {
                    try {
                        libros.add(mapearLibro(datos));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de conversión numérica en el archivo: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error crítico al leer el repositorio: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Mapea un arreglo de cadenas a una instancia de LibroModel.
     */
    private LibroModel mapearLibro(String[] datos) {
        return new LibroModel(
                datos[0].trim(),                     // ISBN
                datos[1].trim(),                     // Título
                datos[2].trim(),                     // Autor
                Integer.parseInt(datos[3].trim()),   // Año
                datos[4].trim(),                     // Género
                Boolean.parseBoolean(datos[5].trim())// Disponible
        );
    }

    /**
     * Sobrescribe el archivo CSV con el estado actual de la lista de libros.
     * @param libros Lista de libros a persistir.
     */
    public void save(List<LibroModel> libros) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (LibroModel libro : libros) {
                writer.printf("%s%s%s%s%s%s%d%s%s%s%b%n",
                        libro.getIsbn(), SEPARADOR,
                        libro.getTitulo(), SEPARADOR,
                        libro.getAutor(), SEPARADOR,
                        libro.getAnio(), SEPARADOR,
                        libro.getGenero(), SEPARADOR,
                        libro.isDisponible());
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar la información en el disco: " + e.getMessage());
        }
    }

    /**
     * Verifica la existencia del archivo y crea las carpetas necesarias si faltan.
     */
    private void verificarArchivo() {
        try {
            File archivo = new File(filePath);
            if (archivo.getParentFile() != null) {
                archivo.getParentFile().mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el almacenamiento: " + e.getMessage());
        }
    }
}