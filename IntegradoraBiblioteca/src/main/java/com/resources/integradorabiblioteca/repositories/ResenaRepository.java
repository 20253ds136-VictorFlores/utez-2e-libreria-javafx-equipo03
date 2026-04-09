package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.*;

/**
 * Clase de persistencia encargada de la gestión física de las reseñas.
 * Implementa operaciones de lectura y escritura en archivos de texto plano
 * utilizando el formato CSV (valores separados por punto y coma).
 */
public class ResenaRepository {

    /** Ruta del sistema de archivos donde se almacena el archivo .csv. */
    private final String RUTA;

    /**
     * Constructor que define la ubicación del archivo de persistencia.
     *
     * @param ruta Dirección relativa o absoluta del archivo de reseñas.
     */
    public ResenaRepository(String ruta) {
        this.RUTA = ruta;
    }

    /**
     * Carga todas las reseñas almacenadas en el archivo físico a la memoria.
     * Si el archivo no existe, retorna una lista vacía para evitar errores de ejecución.
     * Realiza el parseo de datos asumiendo el orden: ID; ISBN_LIBRO; CALIFICACION; COMENTARIO.
     *
     * @return List de objetos ResenaModel recuperados.
     */
    public List<ResenaModel> load() {
        List<ResenaModel> lista = new ArrayList<>();
        File f = new File(RUTA);

        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] d = l.split(";");
                if (d.length >= 4) {
                    lista.add(new ResenaModel(
                            d[0],
                            d[1],
                            Integer.parseInt(d[2]),
                            d[3]
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Sobrescribe el archivo físico con el estado actual de la lista de reseñas.
     * Transforma cada objeto ResenaModel en una línea de texto formateada con ';'.
     *
     * @param lista Conjunto de reseñas a persistir en el almacenamiento.
     */
    public void save(List<ResenaModel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (ResenaModel r : lista) {
                pw.println(r.getIdResena() + ";" +
                        r.getIsbnLibro() + ";" +
                        r.getCalificacion() + ";" +
                        r.getComentario());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}