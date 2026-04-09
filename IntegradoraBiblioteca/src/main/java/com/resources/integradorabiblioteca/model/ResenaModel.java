package com.resources.integradorabiblioteca.model;

public class ResenaModel {
    private String idResena;
    private String isbnLibro;
    private int calificacion;
    private String comentario;

    public ResenaModel(String idResena, String isbnLibro, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    public String getIdResena() { return idResena; }
    public String getIsbnLibro() { return isbnLibro; }
    public int getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
}