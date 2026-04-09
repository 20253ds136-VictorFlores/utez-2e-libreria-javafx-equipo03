package com.resources.integradorabiblioteca.model;

/**
 * Clase de modelo que representa la entidad "Reseña" en el sistema.
 * Actúa como un contenedor de datos (Data Transfer Object) para transportar
 * la información de las valoraciones de los usuarios entre las capas de
 * persistencia, servicio e interfaz.
 */
public class ResenaModel {

    /** Identificador único universal (UUID) para distinguir cada reseña. */
    private String idResena;

    /** Referencia cruzada (Foreign Key) al ISBN del libro calificado. */
    private String isbnLibro;

    /** Valoración numérica otorgada por el usuario (usualmente en un rango de 1 a 5). */
    private int calificacion;

    /** Texto descriptivo que contiene la opinión o crítica del lector. */
    private String comentario;

    /**
     * Constructor completo para la instanciación de una reseña.
     *
     * @param idResena     Identificador único de la transacción de reseña.
     * @param isbnLibro    Código ISBN del libro al que pertenece el comentario.
     * @param calificacion Puntaje numérico asignado.
     * @param comentario   Contenido textual de la opinión.
     */
    public ResenaModel(String idResena, String isbnLibro, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    /** * @return El identificador único de la reseña.
     */
    public String getIdResena() {
        return idResena;
    }

    /** * @return El ISBN del libro asociado a este comentario.
     */
    public String getIsbnLibro() {
        return isbnLibro;
    }

    /** * @return La calificación numérica (puntos/estrellas).
     */
    public int getCalificacion() {
        return calificacion;
    }

    /** * @return El texto íntegro del comentario.
     */
    public String getComentario() {
        return comentario;
    }
}