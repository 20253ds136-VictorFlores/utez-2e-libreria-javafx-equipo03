package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;
import java.util.List;

public class LibraryService {
    private final FileRepository repository;
    private List<LibroModel> libros;

    public LibraryService(FileRepository repository) {
        this.repository = repository;
        this.libros = repository.load();
    }

    public List<LibroModel> listar() {
        return libros;
    }

    public void agregar(LibroModel libro) {
        libros.add(libro);
        repository.save(libros);
    }

    public void actualizar(LibroModel libroEditado) {
        repository.save(libros);
    }

    public void eliminar(String isbn) {
        libros.removeIf(libro -> libro.getIsbn().equals(isbn));

        repository.save(libros);
    }
}