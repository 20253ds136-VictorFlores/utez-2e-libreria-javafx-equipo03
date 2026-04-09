package com.resources.integradorabiblioteca.model;

/**
 * Entidad fundamental (POJO) que representa un libro en el sistema.
 * Esta clase define la estructura de datos para el catálogo y permite
 * el intercambio de información entre los formularios de edición y
 * el almacenamiento persistente en el archivo CSV.
 */
public class LibroModel {

    /** Identificador único internacional del libro. Se usa como llave primaria. */
    private String isbn;

    /** Título completo de la obra. */
    private String titulo;

    /** Nombre de la persona o entidad que escribió la obra. */
    private String autor;

    /** Año en que se imprimió o registró la edición actual. */
    private int anio;

    /** Clasificación temática del libro (ej. Novela, Terror, Ciencia Ficción). */
    private String genero;

    /** Atributo de estado: true si el ejemplar se encuentra en los estantes. */
    private boolean disponible;

    /**
     * Constructor maestro para la creación de instancias de libros.
     * Se utiliza tanto al registrar libros nuevos como al reconstruir objetos
     * leídos desde el repositorio físico.
     * * @param isbn       Código identificador único.
     * @param titulo     Nombre de la pieza literaria.
     * @param autor      Responsable de la obra.
     * @param anio       Año de publicación.
     * @param genero     Categoría literaria.
     * @param disponible Estado de circulación.
     */
    public LibroModel(String isbn, String titulo, String autor, int anio, String genero, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.genero = genero;
        this.disponible = disponible;
    }

    // --- Métodos de Acceso (Getters) ---
    // Permiten que las tablas y el repositorio lean los datos del objeto.

    /** @return El código ISBN. */
    public String getIsbn() { return isbn; }

    /** @return El título registrado. */
    public String getTitulo() { return titulo; }

    /** @return El nombre del autor. */
    public String getAutor() { return autor; }

    /** @return El año de publicación. */
    public int getAnio() { return anio; }

    /** @return El género del libro. */
    public String getGenero() { return genero; }

    /** @return Estado booleano de disponibilidad. */
    public boolean isDisponible() { return disponible; }

    // --- Métodos de Modificación (Setters) ---
    // Permiten que el formulario de edición actualice los valores del objeto en memoria.

    /** @param t Actualiza el título del libro. */
    public void setTitulo(String t) { this.titulo = t; }

    /** @param a Actualiza el nombre del autor. */
    public void setAutor(String a) { this.autor = a; }

    /** @param a Actualiza el año de publicación. */
    public void setAnio(int a) { this.anio = a; }

    /** @param g Actualiza el género literario. */
    public void setGenero(String g) { this.genero = g; }

    /** @param d Actualiza el estado de disponibilidad en el sistema. */
    public void setDisponible(boolean d) { this.disponible = d; }
}