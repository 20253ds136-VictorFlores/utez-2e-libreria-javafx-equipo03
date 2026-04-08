package com.resources.integradorabiblioteca.model;

/**
 * Entidad principal de dominio que estructura los datos basicos de un volumen literario.
 */
public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String genero;
    private boolean disponible;

    /**
     * Constructor general que instancia y asigna completamente el estado de un objeto Libro.
     * @param isbn Cadena alfanumerica que funge como clave primaria del registro.
     * @param titulo Nombre de la obra.
     * @param autor Creador o escritor intelectual de la obra.
     * @param anio Año de publicacion original (entero).
     * @param genero Clasificacion tematica.
     * @param disponible Estado booleano de inventario.
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
     * Metodos de acceso (getters and setters).
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
     * Transforma el objeto actual a una estructura de texto estandarizada CSV separada por comas.
     * @return Cadena que contiene los seis parametros del modelo unidos por comas.
     */
    @Override
    public String toString() {
        return isbn + "," + titulo + "," + autor + "," + anio + "," + genero + "," + disponible;
    }
}