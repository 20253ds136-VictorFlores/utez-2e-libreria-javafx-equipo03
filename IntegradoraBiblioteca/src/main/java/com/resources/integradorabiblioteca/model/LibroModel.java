package com.resources.integradorabiblioteca.model;

import java.util.Objects;

/**
 * Representa el modelo de datos de un Libro dentro del sistema de la biblioteca.
 * Contiene la información básica del material bibliográfico y gestiona su identidad
 * basándose en su código ISBN.
 */
public class LibroModel {

    /** Identificador único internacional del libro. */
    private String isbn;

    /** Título de la obra literaria. */
    private String titulo;

    /** Nombre del autor o autores del libro. */
    private String autor;

    /** Año original de publicación del libro. */
    private int anio;

    /** Género literario o categoría temática a la que pertenece la obra. */
    private String genero;

    /** Estado actual del libro en el inventario (true si está disponible para préstamo, false en caso contrario). */
    private boolean disponible;

    /**
     * Constructor principal de la clase LibroModel.
     * * @param isbn       Identificador único del libro.
     * @param titulo     El título de la obra.
     * @param autor      El autor de la obra.
     * @param anio       El año de publicación.
     * @param genero     El género literario.
     * @param disponible Estado de disponibilidad en la biblioteca.
     */
    public LibroModel(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    /** @return El ISBN del libro. */
    public String getIsbn() { return isbn; }

    /** @return El título del libro. */
    public String getTitulo() { return titulo; }

    /** @return El autor del libro. */
    public String getAutor() { return autor; }

    /** @return El año de publicación. */
    public int getAnio() { return anio; }

    /** @return El género literario del libro. */
    public String getGenero() { return genero; }

    /** @return true si el libro está disponible para ser prestado, false en caso contrario. */
    public boolean isDisponible() { return disponible; }

    /** @param titulo El nuevo título a asignar al libro. */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /** @param autor El nuevo autor a asignar al libro. */
    public void setAutor(String autor) { this.autor = autor; }

    /** @param anio El nuevo año de publicación a asignar. */
    public void setAnio(int anio) { this.anio = anio; }

    /** @param genero El nuevo género literario a asignar. */
    public void setGenero(String genero) { this.genero = genero; }

    /** @param disponible El nuevo estado de disponibilidad (true/false). */
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    /**
     * Compara este libro con otro objeto para determinar si son iguales.
     * En este sistema de biblioteca, la identidad de un libro está definida
     * EXCLUSIVAMENTE por su ISBN. Dos objetos LibroModel distintos en memoria
     * se consideran el mismo libro si comparten el mismo ISBN.
     * * @param o El objeto con el que se va a comparar.
     * @return true si ambos objetos tienen el mismo ISBN, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibroModel)) return false;
        LibroModel libro = (LibroModel) o;
        return Objects.equals(isbn, libro.isbn);
    }

    /**
     * Genera un código hash para el libro.
     * De manera congruente con el método equals, el hash se calcula
     * utilizando únicamente el ISBN del libro.
     * * @return El valor hash correspondiente al ISBN.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}