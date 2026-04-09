package com.resources.integradorabiblioteca.model;

/**
 * Modelo que representa una resena realizada por un usuario.
 * Vincula una valoracion numerica y un comentario con un libro especifico del catalogo.
 */
public class ResenaModel {

    private String idResena;
    private String isbnLibro;
    private int calificacion;
    private String comentario;

    /**
     * Crea una instancia de resena con todos sus datos obligatorios.
     * @param idResena Identificador unico de la valoracion (UUID).
     * @param isbnLibro Codigo del libro al que pertenece la resena.
     * @param calificacion Nota numerica otorgada.
     * @param comentario Texto con la opinion del lector.
     */
    public ResenaModel(String idResena, String isbnLibro, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // --- Metodos de acceso ---
    public String getIdResena() {return idResena;}
    public String getIsbnLibro() {return isbnLibro;}
    public int getCalificacion() {return calificacion;}
    public String getComentario() {
        return comentario;
    }
}