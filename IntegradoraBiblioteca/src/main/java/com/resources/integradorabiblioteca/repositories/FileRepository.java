package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de libros en archivo CSV.
 */
public class FileRepository {
    private final String RUTA_ARCHIVO = "data/books.csv";

    /**
     * Carga los libros desde el archivo CSV.
     * @return lista de libros cargados
     * @throws IOException si ocurre un error de lectura o creación del archivo
     */
    public List<Book> loadBooks() throws IOException {
        List<Book> listaLibros = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs();
            archivo.createNewFile();
            return listaLibros;
        }

        BufferedReader lector = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = lector.readLine()) != null) {
            String[] datos = linea.split(",");

            if (datos.length == 6) {
                Book libro = new Book(
                        datos[0],
                        datos[1],
                        datos[2],
                        Integer.parseInt(datos[3]),
                        datos[4],
                        Boolean.parseBoolean(datos[5])
                );
                listaLibros.add(libro);
            }
        }
        lector.close();
        return listaLibros;
    }

}
