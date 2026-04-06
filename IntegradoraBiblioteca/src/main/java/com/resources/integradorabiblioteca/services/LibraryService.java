package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.repositories.*;
import java.io.IOException;
import java.time.Year;
import java.util.List;

/**
 * Servicio para gestionar el catálogo de libros:
 * carga, alta, edición, eliminación y exportación de reportes.
 */
public class LibraryService {
    final int ANIOLIMIT=1500;
    final int CHARLIMIT=3;
    private FileRepository fileRepository;
    private ReportExporter reportExporter;
    private List<Book> catalogo;

    /**
     * Inicializa el servicio cargando el catálogo desde archivo.
     */
    public LibraryService() {
        this.fileRepository = new FileRepository();
        this.reportExporter = new ReportExporter();
        try {
            this.catalogo = fileRepository.loadBooks();
        } catch (IOException e) {
            System.err.println("Error inicializando el catálogo: " + e.getMessage());
        }
    }

    /**
     * Devuelve el catálogo actual.
     * @return lista de libros
     */
    public List<Book> getCatalogo() {
        return catalogo;
    }

    /**
     * Agrega un nuevo libro al catálogo.
     * @param nuevoLibro libro a registrar
     * @throws Exception si no cumple reglas de validación
     */
    public void agregarLibro(Book nuevoLibro) throws Exception {
        validar(nuevoLibro, true);
        catalogo.add(nuevoLibro);
        fileRepository.saveBooks(catalogo);
    }

    /**
     * Valida reglas de negocio para un libro.
     * @param libro   libro a validar
     * @param esNuevo true si es alta, false si es edición
     * @throws Exception si alguna regla se incumple
     */
    private void validar(Book libro, boolean esNuevo) throws Exception {
        if (libro.getIsbn().trim().isEmpty() || libro.getTitulo().trim().isEmpty() ||
                libro.getAutor().trim().isEmpty() || libro.getGenero().trim().isEmpty()) {
            throw new Exception("Los campos obligatorios no pueden estar vacíos.");
        }

        if (libro.getTitulo().length() < CHARLIMIT) throw new Exception("El título requiere mínimo 3 caracteres.");
        if (libro.getAutor().length() < CHARLIMIT) throw new Exception("El autor requiere mínimo 3 caracteres.");

        int anioActual = Year.now().getValue();
        if (libro.getAnio() < ANIOLIMIT || libro.getAnio() > anioActual) {
            throw new Exception("El año de publicación es inválido.");
        }

        if (esNuevo) {
            for (Book b : catalogo) {
                if (b.getIsbn().equals(libro.getIsbn())) {
                    throw new Exception("El ISBN ingresado ya se encuentra registrado.");
                }
            }
        }
    }
}
