package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private final String filePath;
    private final String SEPARADOR = ";"; // Configurado con punto y coma

    public FileRepository(String filePath) {
        this.filePath = filePath;
        verificarArchivo();
    }

    /**
     * Carga los libros desde el archivo CSV.
     * El orden esperado es: ISBN;Título;Autor;Año;Género;Disponible
     */
    public List<LibroModel> load() {
        List<LibroModel> libros = new ArrayList<>();
        File archivo = new File(filePath);

        if (!archivo.exists()) return libros;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(SEPARADOR);

                // Verificamos que la línea tenga todas las columnas necesarias (6)
                if (datos.length >= 6) {
                    try {
                        LibroModel libro = new LibroModel(
                                datos[0].trim(),                     // ISBN
                                datos[1].trim(),                     // Título (Índice 1)
                                datos[2].trim(),                     // Autor (Índice 2)
                                Integer.parseInt(datos[3].trim()),   // Año (Índice 3)
                                datos[4].trim(),                     // Género
                                Boolean.parseBoolean(datos[5].trim())// Disponible
                        );
                        libros.add(libro);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en línea: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libros;
    }

    /**
     * Guarda la lista completa de libros en el archivo CSV.
     */
    public void save(List<LibroModel> libros) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (LibroModel libro : libros) {
                writer.println(
                        libro.getIsbn() + SEPARADOR +
                                libro.getTitulo() + SEPARADOR +
                                libro.getAutor() + SEPARADOR +
                                libro.getAnio() + SEPARADOR +
                                libro.getGenero() + SEPARADOR +
                                libro.isDisponible()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea la carpeta 'data' y el archivo si no existen para evitar errores.
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
            e.printStackTrace();
        }
    }
}