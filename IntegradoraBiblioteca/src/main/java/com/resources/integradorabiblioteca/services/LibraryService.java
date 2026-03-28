package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio principal de la biblioteca.
 * Gestiona el catálogo de libros aplicando lógica CRUD
 * y delega la persistencia al FileRepository.
 */
public class LibraryService {
    private final FileRepository repository;
    private List<LibroModel> libros;

    /**
     * Inicializa el servicio cargando datos desde archivo.
     * @param repository repositorio que maneja la persistencia
     */
    public LibraryService(FileRepository repository) {
        this.repository = repository;
        this.libros = repository.load(); // carga inicial
    }

    /**
     * Devuelve todos los libros en memoria.
     * @return lista de libros
     */
    public List<LibroModel> listar() {
        return new ArrayList<>(libros); // copia defensiva
    }

    /**
     * Agrega un nuevo libro si no existe ISBN duplicado.
     * @param libro libro a registrar
     * @return true si se agregó, false si ya existía
     */
    public boolean agregar(LibroModel libro) {
        if (libros.contains(libro)) return false; // equals por ISBN
        libros.add(libro);
        repository.save(libros); // persistir cambios
        return true;
    }

    /**
     * Actualiza un libro existente (por ISBN).
     * @param libro libro con datos actualizados
     * @return true si se actualizó, false si no se encontró
     */
    public boolean actualizar(LibroModel libro) {
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getIsbn().equals(libro.getIsbn())) {
                libros.set(i, libro);
                repository.save(libros);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un libro por ISBN.
     * @param isbn identificador del libro
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminar(String isbn) {
        boolean removed = libros.removeIf(l -> l.getIsbn().equals(isbn));
        if (removed) repository.save(libros);
        return removed;
    }
}