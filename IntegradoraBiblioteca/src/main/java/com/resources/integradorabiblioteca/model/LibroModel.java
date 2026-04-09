package com.resources.integradorabiblioteca.model;

/**
 * Entidad que representa un libro dentro del sistema.
 * Define los atributos necesarios para la gestion del inventario y permite
 * el mapeo automatico de datos en las tablas de la interfaz de usuario.
 */
public class LibroModel {

    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String genero;
    private boolean disponible;

    /**
     * Constructor completo para inicializar un ejemplar con toda su informacion.
     * @param isbn Identificador unico internacional del libro.
     * @param titulo Nombre de la obra.
     * @param autor Escritor responsable de la obra.
     * @param anio Fecha de publicacion original.
     * @param genero Categoria literaria a la que pertenece.
     * @param disponible Estado actual de prestamo en la biblioteca.
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
     * Metodos de acceso (getters and setters).
     */
    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public String getAutor() {return autor;}
    public void setAutor(String autor) {this.autor = autor;}
    public int getAnio() {return anio;}
    public void setAnio(int anio) {this.anio = anio;}
    public String getGenero() {return genero;}
    public void setGenero(String genero) {this.genero = genero;}
    public boolean isDisponible() {return disponible;}
    public void setDisponible(boolean disponible) {this.disponible = disponible;}
}