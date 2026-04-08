package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la gestión y filtrado de reseñas de libros.
 * Proporciona la lógica necesaria para separar los comentarios por ejemplar
 * y asegurar que las nuevas opiniones se guarden de forma persistente.
 */
public class ResenaService {
    private final ResenaRepository repository;
    private final List<ResenaModel> resenas;

    /**
     * Inicializa el servicio cargando el histórico de reseñas desde el repositorio.
     * @param repository Repositorio de datos para el acceso al archivo CSV de reseñas.
     */
    public ResenaService(ResenaRepository repository) {
        this.repository = repository;
        this.resenas = repository.load();
    }

    /**
     * Filtra las reseñas almacenadas para devolver únicamente las que pertenecen a un libro.
     * @param isbn El identificador del libro del cual se desean obtener los comentarios.
     * @return Una lista filtrada de objetos {@link ResenaModel}.
     */
    public List<ResenaModel> listarPorLibro(String isbn) {
        // Uso de Streams para un filtrado declarativo y eficiente
        return resenas.stream()
                .filter(r -> r.getIsbnLibro().equals(isbn))
                .collect(Collectors.toList());
    }

    /**
     * Registra una nueva reseña en el sistema y actualiza el almacenamiento físico.
     * @param resena La instancia de la reseña creada por el usuario.
     */
    public void agregar(ResenaModel resena) {
        resenas.add(resena);
        repository.save(resenas);
    }
}