package com.resources.integradorabiblioteca.model;

public class ResenaModel {
    private String idResena;
    private String isbnLibro;
    private String idUsuario;
    private int calificacion;
    private String comentario;

    public ResenaModel(String idResena, String isbnLibro, String idUsuario, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.idUsuario = idUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // Getters
    public String getIdResena() { return idResena; }
    public String getIsbnLibro() { return isbnLibro; }
    public String getIdUsuario() { return idUsuario; }
    public int getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
}