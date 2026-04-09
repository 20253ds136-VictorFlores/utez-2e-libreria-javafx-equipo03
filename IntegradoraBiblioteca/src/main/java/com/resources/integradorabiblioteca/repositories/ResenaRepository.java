package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.*;

/**
 * Clase especializada en la gestión de persistencia para las reseñas de los libros.
 * Implementa la lógica necesaria para transformar objetos en memoria a registros
 * en un archivo de texto plano (.csv) y viceversa, permitiendo el almacenamiento
 * permanente de la información.
 */
public class ResenaRepository {

    /** Ruta específica del sistema de archivos donde se lee y escribe el archivo de datos. */
    private final String RUTA;

    /**
     * Constructor del repositorio.
     * Define la ubicación física del archivo que servirá como base de datos.
     * * @param ruta Ubicación del archivo (ej. "data/resenas.csv").
     */
    public ResenaRepository(String ruta) {
        this.RUTA = ruta;
    }

    /**
     * Recupera el listado completo de reseñas desde el archivo físico.
     * Procesa el archivo línea por línea y reconstruye los objetos ResenaModel.
     * * @return Una lista {@code ArrayList} con las reseñas recuperadas.
     * Si el archivo no existe, devuelve una lista vacía para no romper el flujo.
     */
    public List<ResenaModel> load() {
        List<ResenaModel> lista = new ArrayList<>();
        File f = new File(RUTA);

        // Verificación preventiva para evitar errores de "Archivo no encontrado".
        if (!f.exists()) return lista;

        // Se utiliza try-with-resources para asegurar que el archivo se cierre automáticamente.
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            // Lectura secuencial hasta llegar al final del documento.
            while ((l = br.readLine()) != null) {
                // Se divide la línea de texto en un arreglo usando el punto y coma como separador.
                String[] d = l.split(";");

                // Validación de integridad: se asegura de que la línea tenga las 4 columnas requeridas.
                if (d.length >= 4) {
                    // Reconstrucción del objeto a partir de los fragmentos de texto.
                    lista.add(new ResenaModel(
                            d[0],                   // ID de la reseña
                            d[1],                   // ISBN del libro asociado
                            Integer.parseInt(d[2]), // Conversión de texto a valor numérico
                            d[3]                    // Texto del comentario
                    ));
                }
            }
        } catch (Exception e) {
            // Registro de incidentes en la consola para depuración técnica.
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Serializa y guarda la lista completa de reseñas en el almacenamiento físico.
     * Este método sobrescribe el archivo existente para reflejar el estado más actual.
     * * @param lista La colección de objetos ResenaModel que se desea persistir.
     */
    public void save(List<ResenaModel> lista) {
        // Se abre el flujo de escritura. PrintWriter facilita la creación de líneas de texto.
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            // Recorrido de la lista para transformar cada objeto en una cadena formateada.
            for (ResenaModel r : lista) {
                // Construcción de la línea CSV concatenando atributos con el delimitador ';'.
                pw.println(r.getIdResena() + ";" +
                        r.getIsbnLibro() + ";" +
                        r.getCalificacion() + ";" +
                        r.getComentario());
            }
        } catch (Exception e) {
            // Captura de errores de Entrada/Salida (I/O) durante la escritura.
            e.printStackTrace();
        }
    }
}