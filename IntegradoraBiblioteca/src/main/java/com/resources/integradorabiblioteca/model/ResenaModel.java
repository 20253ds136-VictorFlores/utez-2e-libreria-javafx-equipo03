package com.resources.integradorabiblioteca.model;

/**
 * Clase de modelo (POJO) que representa la entidad "Reseña" dentro del sistema.
 * Esta clase funciona como un contenedor de datos (Data Transfer Object) diseñado
 * para estandarizar la información de las valoraciones que los usuarios realizan
 * sobre los libros del catálogo.
 */
public class ResenaModel {

    /** Identificador único asignado a la reseña (generalmente un UUID). */
    private String idResena;

    /** Código ISBN que vincula de manera lógica la reseña con un libro específico del inventario. */
    private String isbnLibro;

    /** Valor numérico que representa la satisfacción del usuario (escala de calificación). */
    private int calificacion;

    /** Contenido textual que expresa la opinión o crítica del lector. */
    private String comentario;

    /**
     * Constructor para la creación de una instancia completa de la reseña.
     * Permite inicializar todos los atributos del objeto en el momento de su creación.
     * * @param idResena     Clave única de la reseña.
     * @param isbnLibro    Referencia al libro calificado.
     * @param calificacion Valoración numérica.
     * @param comentario   Texto de la opinión.
     */
    public ResenaModel(String idResena, String isbnLibro, int calificacion, String comentario) {
        this.idResena = idResena;
        this.isbnLibro = isbnLibro;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    /** * Recupera el identificador único de la reseña.
     * @return Cadena de texto con el ID.
     */
    public String getIdResena() {
        return idResena;
    }

    /** * Recupera el ISBN del libro al que hace referencia esta valoración.
     * @return El código ISBN asociado.
     */
    public String getIsbnLibro() {
        return isbnLibro;
    }

    /** * Obtiene la calificación otorgada por el usuario.
     * @return Valor entero de la calificación.
     */
    public int getCalificacion() {
        return calificacion;
    }

    /** * Recupera el texto descriptivo del comentario.
     * @return Cadena con la opinión del usuario.
     */
    public String getComentario() {
        return comentario;
    }
}