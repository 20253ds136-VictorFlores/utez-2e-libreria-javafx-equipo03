package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.repositories.*;
import java.io.IOException;
import java.time.Year;
import java.util.List;

/**
 * Servicio para gestionar el catalogo de libros
 * carga, alta, edicion, eliminacion y exportacion de reportes
 */
public class LibraryService {
    final int ANIOLIMIT=1500;
    final int CHARLIMIT=3;
    private FileRepository fileRepository;
    private ReportExporter reportExporter;
    private List<Libro> catalogo;

    /**
     * Inicializa el servicio cargando el catalogo desde archivo
     */
    public LibraryService() {
        this.fileRepository = new FileRepository();
        this.reportExporter = new ReportExporter();
        try {
            this.catalogo = fileRepository.loadBooks();
        } catch (IOException e) {
            System.err.println("Error inicializando el catalogo: " + e.getMessage());
        }
    }

    /**
     * Devuelve el catalogo actual
     * @return lista de libros
     */
    public List<Libro> getCatalogo() {
        return catalogo;
    }

    /**
     * Agrega un nuevo libro al catalogo
     * @param nuevoLibro libro a registrar
     * @throws Exception si no cumple reglas de validacion
     */
    public void agregarLibro(Libro nuevoLibro) throws Exception {
        validar(nuevoLibro, true);
        catalogo.add(nuevoLibro);
        fileRepository.saveBooks(catalogo);
    }

    /**
     * Actualiza un libro existente en el catalogo
     * @param libroEditado libro con datos modificados
     * @throws Exception si no cumple reglas de validación
     */
    public void actualizarLibro(Libro libroEditado) throws Exception {
        validar(libroEditado, false);
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getIsbn().equals(libroEditado.getIsbn())) {
                catalogo.set(i, libroEditado);
                break;
            }
        }
        fileRepository.saveBooks(catalogo);
    }

    /**
     * Elimina un libro por ISBN
     * @param isbn identificador unico
     * @throws IOException si falla la persistencia
     */
    public void eliminarLibro(String isbn) throws IOException {
        Libro libroAEliminar = null;
        for (Libro libro : catalogo) {
            if (libro.getIsbn().equals(isbn)) {
                libroAEliminar = libro;
                break;
            }
        }
        if (libroAEliminar != null) {
            catalogo.remove(libroAEliminar);
            fileRepository.saveBooks(catalogo);
        }
    }

    /**
     * Exporta el catalogo filtrando solo los libros que estan disponibles.
     * @throws IOException si falla la exportacion
     */
    public void exportarReporte() throws IOException {
        List<Libro> soloDisponibles = new java.util.ArrayList<>();

        for (Libro libro : catalogo) {
            if (libro.isDisponible()) {
                soloDisponibles.add(libro);
            }
        }

        reportExporter.exportarCatalogo(soloDisponibles);
    }

    /**
     * Valida reglas de negocio para un libro
     * @param libro   libro a validar
     * @param esNuevo true si es alta, false si es edicion
     * @throws Exception si alguna regla se incumple
     */
    private void validar(Libro libro, boolean esNuevo) throws Exception {
        if (libro.getIsbn().trim().isEmpty() || libro.getTitulo().trim().isEmpty() ||
                libro.getAutor().trim().isEmpty() || libro.getGenero().trim().isEmpty()) {
            throw new Exception("Los campos obligatorios no pueden estar vacios.");
        }

        if (libro.getTitulo().length() < CHARLIMIT) throw new Exception("El titulo requiere minimo 3 caracteres.");
        if (libro.getAutor().length() < CHARLIMIT) throw new Exception("El autor requiere minimo 3 caracteres.");

        int anioActual = Year.now().getValue();
        if (libro.getAnio() < ANIOLIMIT || libro.getAnio() > anioActual) {
            throw new Exception("El año de publicacion es invalido.");
        }

        if (esNuevo) {
            for (Libro b : catalogo) {
                if (b.getIsbn().equals(libro.getIsbn())) {
                    throw new Exception("El ISBN ingresado ya se encuentra registrado.");
                }
            }
        }
    }
}
