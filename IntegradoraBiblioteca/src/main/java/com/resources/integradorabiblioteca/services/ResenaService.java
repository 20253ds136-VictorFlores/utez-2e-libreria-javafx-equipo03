package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.*;

/**
 * Procesa las reseñas utilizando filtrado manual mediante bucles de recolección.
 */
public class ResenaService {
    private ResenaRepository repo;
    private List<ResenaModel> cache;

    public ResenaService(ResenaRepository r) {
        this.repo = r;
        this.cache = r.load();
    }

    /**
     * Reemplazo de Stream: Filtrado manual con lista temporal.
     */
    public List<ResenaModel> listarPorLibro(String isbn) {
        List<ResenaModel> resultados = new ArrayList<>();

        // Recorremos la caché una por una
        for (ResenaModel resena : cache) {
            if (resena.getIsbnLibro().equals(isbn)) {
                resultados.add(resena);
            }
        }
        return resultados;
    }

    public void agregar(ResenaModel r) {
        cache.add(r);
        repo.save(cache);
    }
}