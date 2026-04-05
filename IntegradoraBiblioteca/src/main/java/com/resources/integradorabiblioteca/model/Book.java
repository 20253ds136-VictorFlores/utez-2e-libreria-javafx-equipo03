package com.resources.integradorabiblioteca.model;

/**
 * Modelo que representa un libro en el catálogo.
 */
public class Book {
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
    public Book(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }
}