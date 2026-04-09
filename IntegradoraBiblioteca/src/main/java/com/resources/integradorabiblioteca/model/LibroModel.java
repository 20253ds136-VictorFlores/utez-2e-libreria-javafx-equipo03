package com.resources.integradorabiblioteca.model;

/**
 * Clase que representa la entidad Libro dentro del sistema.
 * Implementa el patrón POJO (Plain Old Java Object) para el transporte de datos
 * entre las capas de persistencia y la interfaz de usuario.
 */
public class LibroModel {

    // --- MÉTODOS DE ACCESO (GETTERS Y SETTERS) ---
    private String isbn;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    private String titulo;
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String autor;
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    private int anio;
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    private String genero;
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Verifica si el libro se encuentra disponible para préstamo.
     * @return true si está disponible, false en caso contrario.
     */
    private boolean disponible;
    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Constructor para inicializar una instancia completa de LibroModel.
     * * @param isbn       Identificador único del libro (International Standard Book Number).
     * @param titulo     Nombre de la obra literaria.
     * @param autor      Nombre del autor o autores del libro.
     * @param anio       Año de publicación original o edición.
     * @param genero     Categoría literaria a la que pertenece.
     * @param disponible Estado actual (true si está en biblioteca, false si está prestado).
     */
    public LibroModel(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    /**
     * Devuelve una representación en cadena del libro para fines de depuración y logs.
     * @return String con el formato "Título (ISBN)".
     */
    @Override
    public String toString() {
        return "LibroModel{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}