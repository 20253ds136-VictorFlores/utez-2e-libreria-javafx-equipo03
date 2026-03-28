package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Maneja la persistencia de libros en un archivo CSV.
 * Permite cargar y guardar el catálogo completo.
 */
public class FileRepository {
    private final String filePath;

    /**
     * Constructor que recibe la ruta del archivo.
     * @param filePath ubicación del archivo CSV
     */
    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Carga todos los libros desde el archivo CSV.
     * @return lista de libros
     */
    public List<LibroModel> load() {
        List<LibroModel> libros = new ArrayList<>();
        File file = new File(filePath);

        // Si el archivo no existe, se devuelve lista vacía
        if (!file.exists()) return libros;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Saltar encabezado
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String isbn = data[0];
                    String titulo = data[1];
                    String autor = data[2];
                    int anio = Integer.parseInt(data[3]);
                    String genero = data[4];
                    boolean disponible = Boolean.parseBoolean(data[5]);

                    libros.add(new LibroModel(isbn, titulo, autor, anio, genero, disponible));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Guarda todos los libros en el archivo CSV.
     * @param libros lista de libros a persistir
     */
    public void save(List<LibroModel> libros) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Encabezado
            bw.write("ISBN,Titulo,Autor,Año,Genero,Disponible");
            bw.newLine();

            // Escribir cada libro
            for (LibroModel libro : libros) {
                bw.write(libro.getIsbn() + "," +
                        libro.getTitulo() + "," +
                        libro.getAutor() + "," +
                        libro.getAnio() + "," +
                        libro.getGenero() + "," +
                        libro.isDisponible());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
        }
    }
}