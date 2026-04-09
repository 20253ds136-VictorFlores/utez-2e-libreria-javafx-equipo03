package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.*;

/**
 * Repositorio encargado de la persistencia física de los libros.
 * Utiliza el separador ';' para evitar conflictos con caracteres en los títulos
 * al momento de almacenar la información en formato de texto.
 */
public class FileRepository {

    /** Ubicación del archivo de texto o CSV en el sistema de archivos. */
    private final String ruta;

    /**
     * Constructor que inicializa la ruta del archivo de datos.
     *
     * @param ruta Ubicación relativa o absoluta del archivo .csv.
     */
    public FileRepository(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Recupera la lista de libros desde el archivo físico a la memoria.
     * Si el archivo no existe, retorna una lista vacía para evitar excepciones
     * durante la lectura.
     *
     * @return Lista de objetos LibroModel recuperados del archivo.
     */
    public List<LibroModel> load() {
        List<LibroModel> lista = new ArrayList<>();
        File archivo = new File(ruta);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length >= 6) {
                    lista.add(new LibroModel(d[0], d[1], d[2], Integer.parseInt(d[3]), d[4], Boolean.parseBoolean(d[5])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Sobrescribe el archivo físico con la lista de libros actual.
     * Formatea los atributos de cada objeto separados por punto y coma (;)
     * para su correcta serialización y posterior lectura.
     *
     * @param lista Lista de libros a persistir en el almacenamiento.
     */
    public void save(List<LibroModel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            for (LibroModel l : lista) {
                pw.println(String.format("%s;%s;%s;%d;%s;%b",
                        l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnio(), l.getGenero(), l.isDisponible()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}