package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado en la persistencia de reseñas.
 * Gestiona el almacenamiento físico de las opiniones de los usuarios, manteniendo
 * la integridad de las relaciones entre libros y autores en formato CSV.
 */
public class ResenaRepository {

    private final String filePath;
    private static final String SEPARADOR = ";";

    /**
     * Inicializa el repositorio de reseñas definiendo la ruta de almacenamiento.
     * @param filePath Ruta del archivo persistente (ej. "data/resenas.csv").
     */
    public ResenaRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Recupera el histórico de reseñas desde el archivo local.
     * Implementa un filtrado de líneas mal formadas para evitar excepciones en tiempo de ejecución.
     * @return Lista de objetos {@link ResenaModel}.
     */
    public List<ResenaModel> load() {
        List<ResenaModel> resenas = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return resenas;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] data = line.split(SEPARADOR);
                if (data.length >= 5) {
                    try {
                        resenas.add(new ResenaModel(
                                data[0].trim(),                     // ID Reseña
                                data[1].trim(),                     // ISBN Libro
                                data[2].trim(),                     // ID Usuario
                                Integer.parseInt(data[3].trim()),   // Calificación
                                data[4].trim()                      // Comentario
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("Dato numérico inválido en reseña: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error crítico al cargar las reseñas: " + e.getMessage());
        }
        return resenas;
    }

    /**
     * Persiste la colección completa de reseñas en el almacenamiento local.
     * @param resenas Lista de reseñas a guardar.
     */
    public void save(List<ResenaModel> resenas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (ResenaModel resena : resenas) {
                pw.printf("%s%s%s%s%s%s%d%s%s%n",
                        resena.getIdResena(), SEPARADOR,
                        resena.getIsbnLibro(), SEPARADOR,
                        resena.getIdUsuario(), SEPARADOR,
                        resena.getCalificacion(), SEPARADOR,
                        resena.getComentario());
            }
        } catch (IOException e) {
            System.err.println("Fallo al persistir las reseñas: " + e.getMessage());
        }
    }
}