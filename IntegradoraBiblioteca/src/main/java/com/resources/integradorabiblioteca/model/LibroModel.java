package com.resources.integradorabiblioteca.model;

/**
 * Entidad que representa un libro dentro del sistema.
 * Contiene la información técnica y el estado de circulación (disponibilidad)
 * para su gestión en el inventario.
 */
public class LibroModel {

    /** Identificador único del libro (International Standard Book Number). */
    private String isbn;

    /** Título oficial de la obra literaria. */
    private String titulo;

    /** Nombre del autor o creador del libro. */
    private String autor;

    /** Año de publicación de la edición registrada. */
    private int anio;

    /** Categoría o género literario al que pertenece la obra. */
    private String genero;

    /** Indicador del estado actual: verdadero si está en biblioteca, falso si está prestado. */
    private boolean disponible;

    /**
     * Constructor completo para inicializar todos los atributos de un libro.
     *
     * @param isbn       Código único de identificación.
     * @param titulo     Nombre de la obra.
     * @param autor      Escritor del libro.
     * @param anio       Año en que fue publicado.
     * @param genero     Clasificación literaria.
     * @param disponible Estado físico en el inventario.
     */
    public LibroModel(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    /** @return El código ISBN del libro. */
    public String getIsbn() { return isbn; }

    /** @return El título de la obra. */
    public String getTitulo() { return titulo; }

    /** @return El nombre del autor. */
    public String getAutor() { return autor; }

    /** @return El año de publicación. */
    public int getAnio() { return anio; }

    /** @return El género literario. */
    public String getGenero() { return genero; }

    /** @return true si el libro está disponible para préstamo, false en caso contrario. */
    public boolean isDisponible() { return disponible; }

    /** @param t El nuevo título a asignar. */
    public void setTitulo(String t) { this.titulo = t; }

    /** @param a El nuevo autor a asignar. */
    public void setAutor(String a) { this.autor = a; }

    /** @param a El nuevo año de publicación a asignar. */
    public void setAnio(int a) { this.anio = a; }

    /** @param g El nuevo género a asignar. */
    public void setGenero(String g) { this.genero = g; }

    /** @param d El nuevo estado de disponibilidad. */
    public void setDisponible(boolean d) { this.disponible = d; }
}