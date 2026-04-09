package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.*;
import java.util.*;
import java.time.Year;

public class LibraryService {
    private FileRepository repo;
    private List<LibroModel> cache;
    private ReportExporter exporter = new ReportExporter();

    public LibraryService(FileRepository r) { this.repo = r; this.cache = r.load(); }

    public List<LibroModel> listar() { return cache; }

    public void agregar(LibroModel l) throws Exception {
        validar(l, true);
        cache.add(l);
        repo.save(cache);
    }

    public void actualizar(LibroModel l) throws Exception {
        validar(l, false);
        repo.save(cache);
    }

    public void eliminar(String isbn) {
        cache.removeIf(b -> b.getIsbn().equals(isbn));
        repo.save(cache);
    }

    public void exportar() throws Exception { exporter.exportarCSV(cache); }

    private void validar(LibroModel l, boolean esNuevo) throws Exception {
        if (l.getIsbn().isBlank() || l.getTitulo().isBlank()) throw new Exception("Campos vacíos");
        if (l.getAnio() > Year.now().getValue()) throw new Exception("Año inválido");
        if (esNuevo && cache.stream().anyMatch(b -> b.getIsbn().equals(l.getIsbn()))) throw new Exception("ISBN duplicado");
    }
}