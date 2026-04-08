package com.resources.integradorabiblioteca.model;

import java.util.Objects;

/**
 * Representa a un usuario registrado en el sistema de la biblioteca.
 * La identidad del usuario se define de manera única por su ID (o nombre de usuario).
 */
public class UsuarioModel {
    private String idUsuario;
    private String nombreCompleto;
    private String contrasena; // En un sistema real, esto iría encriptado

    public UsuarioModel(String idUsuario, String nombreCompleto, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getContrasena() { return contrasena; }

    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioModel)) return false;
        UsuarioModel usuario = (UsuarioModel) o;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
}