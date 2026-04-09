package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado en la persistencia de reseñas anónimas.
 * Gestiona el almacenamiento físico en formato CSV. Se ha eliminado la
 * referencia a usuarios para simplificar la arquitectura del sistema.
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
     * Procesa únicamente 4 columnas de datos: ID, ISBN, Calificación y Comentario.
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

                // Cambio crítico: Ahora validamos 4 columnas (antes eran 5 con el usuario)
                if (data.length >= 4) {
                    try {
                        resenas.add(new ResenaModel(
                                data[0].trim(),                     // ID Reseña
                                data[1].trim(),                     // ISBN Libro
                                Integer.parseInt(data[2].trim()),   // Calificación
                                data[3].trim()                      // Comentario
                        ));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en línea: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error crítico al cargar las reseñas: " + e.getMessage());
        }
        return resenas;
    }

    /**
     * Persiste la colección de reseñas en el almacenamiento local.
     * El formato de salida es: ID;ISBN;Calificación;Comentario
     * @param resenas Lista de reseñas anónimas a guardar.
     */
    public void save(List<ResenaModel> resenas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (ResenaModel resena : resenas) {
                // Se eliminó el parámetro del ID de usuario en la cadena de formato
                pw.printf("%s%s%s%s%d%s%s%n",
                        resena.getIdResena(), SEPARADOR,
                        resena.getIsbnLibro(), SEPARADOR,
                        resena.getCalificacion(), SEPARADOR,
                        resena.getComentario());
            }
        } catch (IOException e) {
            System.err.println("Fallo al persistir las reseñas: " + e.getMessage());
        }
    }
}