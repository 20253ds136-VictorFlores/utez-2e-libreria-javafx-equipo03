package com.resources.integradorabiblioteca.model;

/**
 * Modelo que representa un libro en el catálogo.
 */
public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String genero;
    private boolean disponible;

    /**
     * Inicializa un libro con los datos dados.
     * @param isbn código ISBN
     * @param titulo título del libro
     * @param autor autor del libro
     * @param anio año de publicación
     * @param genero género literario
     * @param disponible disponibilidad en el catálogo
     */
    public Libro(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    /**
     * Métodos de acceso (getters y setters).
     */
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    /**
     * Devuelve los atributos del libro separados por comas.
     * @return representación en texto del libro
     */
    @Override
    public String toString() {
        return isbn + "," + titulo + "," + autor + "," + anio + "," + genero + "," + disponible;
    }
}