package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.*;

/**
 * Repositorio encargado de guardar y recuperar las resenas de los libros
 * utilizando un archivo de texto plano (.csv) como base de datos local.
 */
public class ResenaRepository {

    private final String ruta;

    /**
     * Crea el repositorio asignando la ruta del archivo fisico.
     * @param ruta Ubicacion del archivo (ej. "data/resenas.csv").
     */
    public ResenaRepository(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Lee el archivo fisico y recupera todas las resenas guardadas.
     * Si el archivo no existe, retorna una lista vacia para evitar interrupciones.
     * @return Lista con los objetos ResenaModel procesados.
     */
    public List<ResenaModel> load() {
        List<ResenaModel> listaResenas = new ArrayList<>();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            return listaResenas;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length >= 4) {
                    listaResenas.add(new ResenaModel(
                            datos[0],
                            datos[1],
                            Integer.parseInt(datos[2]),
                            datos[3]
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaResenas;
    }

    /**
     * Sobrescribe el archivo fisico con la lista actual de resenas para mantenerlas sincronizadas.
     * @param listaResenas Coleccion de resenas a guardar.
     */
    public void save(List<ResenaModel> listaResenas) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(ruta))) {

            for (ResenaModel resena : listaResenas) {
                escritor.println(resena.getIdResena() + ";" +
                        resena.getIsbnLibro() + ";" +
                        resena.getCalificacion() + ";" +
                        resena.getComentario());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}