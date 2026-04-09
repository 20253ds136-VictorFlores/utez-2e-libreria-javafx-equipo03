package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.*;

/**
 * Clase de la capa de acceso a datos responsable de la persistencia de libros.
 * Gestiona la lectura y escritura de archivos en formato de texto plano,
 * utilizando una estructura de datos delimitada para simular una base de datos local.
 */
public class FileRepository {

    /** Ruta o nombre del archivo donde se centraliza el almacenamiento de los libros. */
    private final String ruta;

    /**
     * Constructor del repositorio. Establece la conexión lógica con el archivo físico.
     * * @param ruta Trayectoria del archivo de datos (ej. "data/books.csv").
     */
    public FileRepository(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Transforma el contenido del archivo físico en una colección de objetos en memoria.
     * Realiza un proceso de "deserialización" manual, interpretando cada línea del
     * archivo como una instancia única de LibroModel.
     * * @return Una {@code List<LibroModel>} con todos los registros procesados.
     * Si el archivo no se encuentra, devuelve una lista vacía para asegurar
     * la continuidad de la aplicación.
     */
    public List<LibroModel> load() {
        List<LibroModel> lista = new ArrayList<>();
        File archivo = new File(ruta);

        // Verificación de existencia para prevenir errores de flujo de entrada.
        if (!archivo.exists()) return lista;

        // Apertura del flujo de lectura optimizado con BufferedReader.
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            // Lectura iterativa hasta alcanzar el final del documento.
            while ((linea = br.readLine()) != null) {
                // Fragmentación de la línea basada en el delimitador de punto y coma.
                String[] d = linea.split(";");

                // Validación de integridad: el registro debe cumplir con la estructura de 6 campos.
                if (d.length >= 6) {
                    // Reconstrucción del objeto con conversión explícita de tipos de datos.
                    lista.add(new LibroModel(
                            d[0],                   // ISBN
                            d[1],                   // Título
                            d[2],                   // Autor
                            Integer.parseInt(d[3]), // Conversión de texto a Año (int)
                            d[4],                   // Género
                            Boolean.parseBoolean(d[5]) // Conversión de texto a Disponibilidad (boolean)
                    ));
                }
            }
        } catch (Exception e) {
            // Reporte de excepciones en consola para rastreo técnico.
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Convierte la colección de objetos en memoria a un formato de texto persistente.
     * Este método sobrescribe el archivo completo para garantizar la sincronización
     * absoluta entre la interfaz y el almacenamiento.
     * * @param lista La fuente de datos actual (colección de libros) que se desea guardar.
     */
    public void save(List<LibroModel> lista) {
        // Inicialización del flujo de salida (escritura).
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            // Iteración sobre la lista para procesar cada objeto individualmente.
            for (LibroModel l : lista) {
                // Serialización: se genera una cadena de texto con formato estructurado.
                pw.println(String.format("%s;%s;%s;%d;%s;%b",
                        l.getIsbn(),
                        l.getTitulo(),
                        l.getAutor(),
                        l.getAnio(),
                        l.getGenero(),
                        l.isDisponible()));
            }
        } catch (IOException e) {
            // Captura de errores críticos del sistema de archivos.
            e.printStackTrace();
        }
    }
}