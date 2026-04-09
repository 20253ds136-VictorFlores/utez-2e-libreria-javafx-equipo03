package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.*;
import java.util.stream.Collectors;

public class ResenaService {
    private ResenaRepository repo;
    private List<ResenaModel> cache;

    public ResenaService(ResenaRepository r) { this.repo = r; this.cache = r.load(); }

    public void agregar(ResenaModel r) { cache.add(r); repo.save(cache); }

    public List<ResenaModel> listarPorLibro(String isbn) {
        return cache.stream().filter(r -> r.getIsbnLibro().equals(isbn)).collect(Collectors.toList());
    }
}