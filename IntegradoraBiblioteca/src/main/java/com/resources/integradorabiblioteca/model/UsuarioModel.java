package com.resources.integradorabiblioteca.model;

import java.util.Objects;

/**
 * Representa la entidad de Usuario dentro del ecosistema de la biblioteca.
 * Esta clase almacena las credenciales y la información de perfil necesaria
 * para gestionar sesiones y autorizar acciones dentro de la aplicación.
 */
public class UsuarioModel {
    private String idUsuario;
    private String nombreCompleto;
    private String contrasena; // Nota de seguridad: En entornos productivos, manejar mediante hashes.

    /**
     * Construye un nuevo usuario con sus credenciales básicas.
     * @param idUsuario Identificador único del usuario (username).
     * @param nombreCompleto Nombre real del titular de la cuenta.
     * @param contrasena Clave de acceso al sistema.
     */
    public UsuarioModel(String idUsuario, String nombreCompleto, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
    }

    // --- Métodos de Acceso y Modificación ---

    public String getIdUsuario() { return idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /**
     * Compara este usuario con otro objeto basándose en la igualdad del identificador.
     * Este método es vital para operaciones de búsqueda en colecciones y validación de sesiones.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioModel)) return false;
        UsuarioModel usuario = (UsuarioModel) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    /**
     * Genera un valor hash consistente con el identificador único del usuario.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
}