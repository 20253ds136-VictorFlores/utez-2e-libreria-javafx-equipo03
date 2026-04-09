package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Capa de Servicio encargada de la lógica de negocio para las reseñas de los libros.
 * Actúa como intermediario entre el controlador de la interfaz y el repositorio de datos,
 * gestionando una memoria caché para optimizar las consultas.
 */
public class ResenaService {

    /** Repositorio para la persistencia de datos en el sistema de archivos. */
    private final ResenaRepository repo;

    /** Memoria volátil (caché) que almacena las reseñas cargadas para agilizar el acceso. */
    private final List<ResenaModel> cache;

    /**
     * Constructor de la clase. Inicializa el repositorio y sincroniza la caché
     * cargando los datos existentes desde la fuente de persistencia.
     * * @param r Instancia de ResenaRepository configurada.
     */
    public ResenaService(ResenaRepository r) {
        this.repo = r;
        this.cache = r.load();
    }

    /**
     * Registra una nueva reseña en el sistema.
     * El proceso actualiza la memoria caché y persiste la lista completa
     * en el almacenamiento físico de manera inmediata.
     * * @param r Objeto ResenaModel con la información de la calificación y comentario.
     */
    public void agregar(ResenaModel r) {
        cache.add(r);
        repo.save(cache);
    }

    /**
     * Filtra y recupera todas las reseñas asociadas a un libro específico.
     * Utiliza la API de Streams de Java para realizar un filtrado eficiente
     * basado en el código ISBN.
     * * @param isbn Identificador único del libro a consultar.
     * @return List de ResenaModel que coinciden con el ISBN proporcionado.
     */
    public List<ResenaModel> listarPorLibro(String isbn) {
        return cache.stream()
                .filter(r -> r.getIsbnLibro().equals(isbn))
                .collect(Collectors.toList());
    }
}