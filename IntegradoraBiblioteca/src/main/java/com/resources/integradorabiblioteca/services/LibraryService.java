package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;

import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private final FileRepository repository;
    private List<LibroModel> libros;

    public LibraryService(FileRepository repository) {
        this.repository = repository;
        this.libros = repository.load();
    }

    public List<LibroModel> listar() {
        return new ArrayList<>(libros);
    }

    public boolean agregar(LibroModel libro) {
        if (libros.contains(libro)) return false;
        libros.add(libro);
        repository.save(libros);
        return true;
    }

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

    public boolean eliminar(String isbn) {
        boolean removed = libros.removeIf(l -> l.getIsbn().equals(isbn));
        if (removed) repository.save(libros);
        return removed;
    }
}