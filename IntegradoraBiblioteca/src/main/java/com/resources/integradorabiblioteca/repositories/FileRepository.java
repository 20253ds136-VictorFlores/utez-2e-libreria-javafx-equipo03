package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de datos que gestiona la persistencia de los libros en un archivo de texto plano.
 * Funciona como la capa de acceso a datos, serializando y deserializando
 * los objetos {@link LibroModel} utilizando un formato separado por punto y coma (;).
 */
public class FileRepository {

    /** Ruta del archivo en el sistema donde se almacenarán los datos. */
    private final String filePath;

    /**
     * Constructor del repositorio de archivos.
     * * @param filePath La ruta relativa o absoluta del archivo de texto que
     * se utilizará como base de datos (por ejemplo, "data/libros.csv").
     */
    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Carga todos los libros almacenados en el archivo.
     * Lee el archivo línea por línea, separando los atributos por el delimitador (;)
     * y reconstruyendo las instancias de {@link LibroModel}.
     * Si el archivo no existe, no arroja error, sino que retorna una lista vacía.
     * * @return Una lista ({@link List}) que contiene todos los libros recuperados del archivo.
     */
    public List<LibroModel> load() {
        List<LibroModel> libros = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return libros;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                // Verifica que la línea tenga exactamente los 6 atributos requeridos
                if (data.length == 6) {
                    libros.add(new LibroModel(
                            data[0], data[1], data[2],
                            Integer.parseInt(data[3]),
                            data[4], Boolean.parseBoolean(data[5])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libros;
    }

    /**
     * Guarda la lista completa de libros en el archivo, sobrescribiendo
     * cualquier contenido anterior.
     * Transforma cada objeto {@link LibroModel} en una cadena de texto plana,
     * uniendo sus atributos con un punto y coma (;) como delimitador.
     * * @param libros La lista actual de libros en memoria que se desea persistir.
     */
    public void save(List<LibroModel> libros) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (LibroModel libro : libros) {
                pw.println(libro.getIsbn() + ";" + libro.getTitulo() + ";" + libro.getAutor() + ";" +
                        libro.getAnio() + ";" + libro.getGenero() + ";" + libro.isDisponible());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}