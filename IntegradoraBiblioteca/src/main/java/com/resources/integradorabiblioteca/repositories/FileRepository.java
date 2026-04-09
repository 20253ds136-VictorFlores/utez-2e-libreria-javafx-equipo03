package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.*;

/**
 * Repositorio encargado de la persistencia de los libros en el almacenamiento local.
 * Implementa la lectura y escritura de datos utilizando archivos de texto plano (.csv).
 */
public class FileRepository {

    private final String ruta;

    /**
     * Inicializa el repositorio con la ruta del archivo que funcionara como base de datos.
     * @param ruta Ubicacion fisica del archivo ("data/libros.csv").
     */
    public FileRepository(String ruta) {
        this.ruta = ruta ;
    }

    /**
     * Recupera la coleccion completa de libros desde el archivo en disco.
     * Si el archivo no existe, devuelve una lista vacia para no interrumpir el flujo del sistema.
     * @return Lista de objetos LibroModel reconstruidos desde el archivo.
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
     * Escribe la lista completa de libros en el archivo fisico.
     * Este metodo sobrescribe el contenido previo para asegurar la integridad del inventario.
     * @param listaLibros Coleccion de libros que se desea persistir.
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