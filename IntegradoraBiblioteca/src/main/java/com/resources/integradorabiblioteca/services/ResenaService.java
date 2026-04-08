package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResenaService {
    private final ResenaRepository repository;
    private List<ResenaModel> resenas;

    public ResenaService(ResenaRepository repository) {
        this.repository = repository;
        this.resenas = repository.load();
    }

    public List<ResenaModel> listarPorLibro(String isbn) {
        return resenas.stream()
                .filter(r -> r.getIsbnLibro().equals(isbn))
                .collect(Collectors.toList());
    }

    public void agregar(ResenaModel resena) {
        resenas.add(resena);
        repository.save(resenas);
    }
}