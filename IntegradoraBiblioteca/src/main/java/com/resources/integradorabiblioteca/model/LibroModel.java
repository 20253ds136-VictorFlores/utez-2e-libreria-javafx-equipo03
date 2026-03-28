package com.resources.integradorabiblioteca.model;

import java.util.Objects;

/**
 * Representa un libro dentro del catálogo de la biblioteca.
 * Contiene atributos básicos y validaciones mínimas.
 */
public class LibroModel {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String genero;
    private boolean disponible;
    final int CARACTER = 3;
    /**
     * Constructor con validaciones básicas.
     * @param isbn identificador único, no vacío
     * @param titulo mínimo 3 caracteres
     * @param autor mínimo 3 caracteres
     * @param anio rango lógico (1500 - año actual)
     * @param genero género literario
     * @param disponible disponibilidad del libro
     */
    public LibroModel(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        if(isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN no puede estar vacío");
        if(titulo == null || titulo.length() < CARACTER)
            throw new IllegalArgumentException("Título mínimo 3 caracteres");
        if(autor == null || autor.length() < CARACTER)
            throw new IllegalArgumentException("Autor mínimo 3 caracteres");
        if(anio < 1500 || anio > java.time.LocalDate.now().getYear())
            throw new IllegalArgumentException("Año fuera de rango lógico");

        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    // Getters y setters para cada atributo
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Dos libros se consideran iguales si tienen el mismo ISBN.
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof LibroModel)) return false;
        LibroModel libro = (LibroModel) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}