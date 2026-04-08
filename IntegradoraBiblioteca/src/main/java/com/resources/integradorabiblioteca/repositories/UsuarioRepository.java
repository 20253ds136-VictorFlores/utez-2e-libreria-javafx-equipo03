package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.UsuarioModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de la persistencia de los datos de los usuarios.
 * Gestiona la lectura y escritura de instancias de {@link UsuarioModel} en un
 * archivo de texto, utilizando el punto y coma (;) como delimitador de campos.
 */
public class UsuarioRepository {

    private final String filePath;
    private static final String SEPARADOR = ";";

    /**
     * Constructor del repositorio de usuarios.
     * @param filePath La ruta del archivo de texto (ej. "data/usuarios.csv").
     */
    public UsuarioRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Carga todos los usuarios almacenados en el archivo.
     * Implementa limpieza de datos para asegurar una comparación de credenciales precisa.
     * @return Una lista con los objetos {@link UsuarioModel} recuperados.
     */
    public List<UsuarioModel> load() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return usuarios;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] data = line.split(SEPARADOR);
                // Verifica que la línea tenga la estructura correcta (3 columnas)
                if (data.length == 3) {
                    usuarios.add(new UsuarioModel(
                            data[0].trim(), // idUsuario
                            data[1].trim(), // nombreCompleto
                            data[2].trim()  // contrasena
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error crítico al cargar el registro de usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Guarda la lista completa de usuarios en el archivo, sobrescribiendo el contenido.
     * @param usuarios La lista de usuarios en memoria que se desea persistir.
     */
    public void save(List<UsuarioModel> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (UsuarioModel usuario : usuarios) {
                pw.printf("%s%s%s%s%s%n",
                        usuario.getIdUsuario(), SEPARADOR,
                        usuario.getNombreCompleto(), SEPARADOR,
                        usuario.getContrasena());
            }
        } catch (IOException e) {
            System.err.println("Fallo al guardar el registro de usuarios: " + e.getMessage());
        }
    }
}