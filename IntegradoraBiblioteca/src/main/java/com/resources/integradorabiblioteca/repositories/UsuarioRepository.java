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

    /** Ruta del archivo donde se almacenan los usuarios (ej. "data/usuarios.csv"). */
    private final String filePath;

    /**
     * Constructor del repositorio de usuarios.
     * @param filePath La ruta del archivo de texto.
     */
    public UsuarioRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Carga todos los usuarios almacenados en el archivo.
     * @return Una lista con los objetos {@link UsuarioModel} recuperados.
     */
    public List<UsuarioModel> load() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return usuarios;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                // Verifica que la línea tenga exactamente los 3 atributos del usuario
                if (data.length == 3) {
                    usuarios.add(new UsuarioModel(
                            data[0], // idUsuario
                            data[1], // nombreCompleto
                            data[2]  // contrasena
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los usuarios: " + e.getMessage());
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
                pw.println(usuario.getIdUsuario() + ";" +
                        usuario.getNombreCompleto() + ";" +
                        usuario.getContrasena());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los usuarios: " + e.getMessage());
        }
    }
}