package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.*;

/**
 * Repositorio encargado de leer y escribir la informacion de los libros
 * en un archivo de texto, simulando una base de datos local.
 */
public class FileRepository {

    private final String ruta;

    /**
     * Crea el repositorio asignando la ruta del archivo fisico a utilizar.
     * @param ruta Direccion del archivo de datos ("data/books.csv").
     */
    public FileRepository(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Lee el archivo fisico y convierte cada linea en un objeto LibroModel.
     * Si el archivo no existe, retorna una lista vacia para evitar interrupciones.
     * @return Lista con todos los libros procesados.
     */
    public List<LibroModel> load() {
        List<LibroModel> listaLibros = new ArrayList<>();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            return listaLibros;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length >= 6) {
                    listaLibros.add(new LibroModel(
                            datos[0],
                            datos[1],
                            datos[2],
                            Integer.parseInt(datos[3]),
                            datos[4],
                            Boolean.parseBoolean(datos[5])
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaLibros;
    }

    /**
     * Toma la lista actual de libros y la guarda en el archivo de texto,
     * sobrescribiendo la informacion anterior para mantenerla sincronizada.
     * @param listaLibros Coleccion de libros que se desea guardar.
     */
    public void save(List<LibroModel> listaLibros) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(ruta))) {

            for (LibroModel libro : listaLibros) {
                escritor.println(String.format("%s;%s;%s;%d;%s;%b",
                        libro.getIsbn(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getAnio(),
                        libro.getGenero(),
                        libro.isDisponible()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}