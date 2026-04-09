package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.*;

/**
 * Clase de la capa de Servicios encargada de la lógica de negocio para las reseñas.
 * Actúa como intermediario entre la persistencia y la interfaz, gestionando
 * los datos en memoria (caché) para optimizar el rendimiento de las consultas.
 */
public class ResenaService {

    /** Repositorio encargado de la persistencia de datos en archivos externos. */
    private ResenaRepository repo;

    /** Lista que almacena las reseñas en memoria para agilizar el acceso. */
    private List<ResenaModel> cache;

    /**
     * Constructor del servicio.
     * Inicializa el repositorio y carga los datos existentes desde el archivo a la caché.
     * @param r Repositorio de reseñas previamente instanciado.
     */
    public ResenaService(ResenaRepository r) {
        this.repo = r;
        this.cache = r.load();
    }

    /**
     * Filtra las reseñas guardadas para devolver solo las que pertenecen a un libro específico.
     * Utiliza un proceso de recolección manual mediante un bucle para identificar coincidencias.
     * @param isbn Identificador único del libro a consultar.
     * @return Lista de objetos ResenaModel vinculados al ISBN proporcionado.
     */
    public List<ResenaModel> listarPorLibro(String isbn) {
        // Se crea una lista temporal para almacenar los resultados encontrados.
        List<ResenaModel> resultados = new ArrayList<>();

        // Recorrido uno a uno de todos los elementos almacenados en la caché.
        for (ResenaModel resena : cache) {
            // Comparación lógica entre el ISBN del libro y el ISBN registrado en la reseña.
            if (resena.getIsbnLibro().equals(isbn)) {
                // Si coinciden, se agrega el objeto a la lista de resultados.
                resultados.add(resena);
            }
        }
        return resultados;
    }

    /**
     * Agrega una nueva reseña al sistema y asegura su persistencia.
     * Actualiza la lista en memoria y escribe el estado actual en el archivo físico.
     * @param r Objeto con la información de la nueva reseña.
     */
    public void agregar(ResenaModel r) {
        // Inserción en la lista de memoria activa.
        cache.add(r);

        // Sincronización con el almacenamiento físico a través del repositorio.
        repo.save(cache);
    }
}