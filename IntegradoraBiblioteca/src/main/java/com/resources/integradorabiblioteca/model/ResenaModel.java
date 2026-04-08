package com.resources.integradorabiblioteca.model;

/**
 * Representa la opinión y valoración de un usuario sobre un libro específico.
 * Esta clase funciona como un contenedor de datos inmutable para las reseñas
 * almacenadas en el sistema.
 */
public class ResenaModel {

    private final String idResena;
    private final String isbnLibro;
    private final String idUsuario;
    private final int calificacion;
    private final String comentario;

    /**
     * Constructor para crear una nueva instancia de reseña.
     * * @param idResena     Identificador único de la reseña (ej. R-171256...).
     * @param isbnLibro    Referencia al ISBN del libro reseñado.
     * @param idUsuario    Identificador o nombre del usuario que escribe la reseña.
     * @param calificacion Valoración numérica (típicamente de 1 a 5).
     * @param comentario   Texto descriptivo con la opinión del usuario.
     */
    public ResenaModel(String idResena, String isbnLibro, String idUsuario, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.idUsuario = idUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // --- MÉTODOS DE ACCESO (GETTERS) ---
    // Nota: No se incluyen Setters para mantener la integridad de la reseña una vez creada.

    public String getIdResena() {
        return idResena;
    }

    public String getIsbnLibro() {
        return isbnLibro;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    /**
     * Genera una representación textual de la reseña.
     * Útil para logs y procesos de depuración en consola.
     * * @return Cadena con la información relevante de la reseña.
     */
    @Override
    public String toString() {
        return "ResenaModel{" +
                "id='" + idResena + '\'' +
                ", libro='" + isbnLibro + '\'' +
                ", estrellas=" + calificacion +
                '}';
    }
}