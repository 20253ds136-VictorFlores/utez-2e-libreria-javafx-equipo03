package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.Libro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de acceso a datos que gestiona la persistencia de los objetos Libro
 * a traves de un archivo de texto estructurado por comas (CSV).
 */
public class FileRepository {

    private final String RUTA_ARCHIVO = "data/books.csv";

    /**
     * Recupera la coleccion de libros almacenada en el sistema de archivos local.
     * Si el directorio o el archivo no existen, se encargara de generarlos.
     * @return Lista estructurada con los objetos Libro leidos desde el origen de datos.
     * @throws IOException Si existe una interrupcion, problema de permisos o fallo de lectura del CSV.
     */
    public List<Libro> loadBooks() throws IOException {
        List<Libro> listaLibros = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs();
            archivo.createNewFile();
            return listaLibros;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 6) {
                    listaLibros.add(new Libro(
                            datos[0],
                            datos[1],
                            datos[2],
                            Integer.parseInt(datos[3]),
                            datos[4],
                            Boolean.parseBoolean(datos[5])
                    ));
                }
            }
        }
        return listaLibros;
    }

    /**
     * Sobrescribe el origen de datos actual persistiendo todos los elementos de la coleccion en el CSV.
     * @param listaLibros Coleccion completa y actualizada de libros a persistir.
     * @throws IOException Si ocurre un problema de bloqueo de archivo o error de escritura.
     */
    public void saveBooks(List<Libro> listaLibros) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Libro libro : listaLibros) {
                escritor.write(libro.toString());
                escritor.newLine();
            }
        }
    }
}
