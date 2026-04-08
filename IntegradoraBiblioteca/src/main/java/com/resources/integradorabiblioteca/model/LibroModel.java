package com.resources.integradorabiblioteca.model;

/**
 * Clase que representa la entidad Libro dentro del sistema.
 * Implementa el patrón POJO (Plain Old Java Object) para el transporte de datos
 * entre las capas de persistencia y la interfaz de usuario.
 */
public class LibroModel {

    // El ISBN actúa como identificador único.
    // Se recomienda mantenerlo privado y usar métodos de acceso.
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String genero;
    private boolean disponible;

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

    // --- MÉTODOS DE ACCESO (GETTERS Y SETTERS) ---

    public String getIsbn() {
        return isbn;
    }

    /**
     * Nota: Generalmente el ISBN no debería cambiar tras su creación.
     * Se mantiene el setter por compatibilidad con algunos frameworks de persistencia.
     */
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

    /**
     * Verifica si el libro se encuentra disponible para préstamo.
     * @return true si está disponible, false en caso contrario.
     */
    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
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