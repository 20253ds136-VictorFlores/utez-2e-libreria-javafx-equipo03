package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de la persistencia de los datos de las reseñas.
 * Almacena la información en un archivo de texto separado por punto y coma (;),
 * manteniendo las referencias cruzadas (llaves foráneas) hacia los Libros y los Usuarios.
 */
public class ResenaRepository {

    /** Ruta del archivo donde se guardarán las reseñas (ej. "data/resenas.csv"). */
    private final String filePath;

    /**
     * Constructor del repositorio de reseñas.
     * @param filePath La ruta del archivo de texto.
     */
    public ResenaRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Carga todas las reseñas desde el archivo.
     * @return Una lista con los objetos {@link ResenaModel} recuperados.
     */
    public List<ResenaModel> load() {
        List<ResenaModel> resenas = new ArrayList<>();
        File file = new File(filePath);

        // Si el archivo no existe (ej. primera vez que se ejecuta), retorna lista vacía
        if (!file.exists()) return resenas;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                // Verifica que la línea tenga exactamente los 5 atributos de la reseña
                if (data.length == 5) {
                    resenas.add(new ResenaModel(
                            data[0],                     // idResena
                            data[1],                     // isbnLibro
                            data[2],                     // idUsuario
                            Integer.parseInt(data[3]),   // calificacion (convertido a int)
                            data[4]                      // comentario
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar las reseñas: " + e.getMessage());
        }
        return resenas;
    }

    /**
     * Guarda la lista completa de reseñas en el archivo, sobrescribiendo el contenido.
     * @param resenas La lista de reseñas en memoria que se desea persistir.
     */
    public void save(List<ResenaModel> resenas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (ResenaModel resena : resenas) {
                pw.println(resena.getIdResena() + ";" +
                        resena.getIsbnLibro() + ";" +
                        resena.getIdUsuario() + ";" +
                        resena.getCalificacion() + ";" +
                        resena.getComentario());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar las reseñas: " + e.getMessage());
        }
    }
}