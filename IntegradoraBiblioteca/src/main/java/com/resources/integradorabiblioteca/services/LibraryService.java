package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;
import java.util.List;

/**
 * Servicio encargado de la lógica de negocio para la gestión de libros.
 * Actúa como un puente entre los controladores de la interfaz y el repositorio de archivos,
 * manteniendo una copia en memoria (caché) para optimizar el rendimiento de la aplicación.
 */
public class LibraryService {
    private final FileRepository repository;
    private final List<LibroModel> libros;

    /**
     * Inicializa el servicio cargando los datos existentes desde el repositorio.
     * @param repository Instancia del repositorio de archivos configurada.
     */
    public LibraryService(FileRepository repository) {
        this.repository = repository;
        // Se cargan los libros al iniciar para evitar lecturas constantes al disco
        this.libros = repository.load();
    }

    /**
     * Obtiene la lista actual de libros en memoria.
     * @return Lista de {@link LibroModel}.
     */
    public List<LibroModel> listar() {
        return libros;
    }

    /**
     * Registra un nuevo libro en la lista local y sincroniza los cambios con el archivo.
     * @param libro El nuevo objeto libro a integrar.
     */
    public void agregar(LibroModel libro) {
        libros.add(libro);
        repository.save(libros);
    }

    /**
     * Persiste los cambios realizados sobre un libro ya existente en la lista.
     * Dado que los objetos se manejan por referencia, solo es necesario disparar el guardado.
     * @param libroEditado El libro con la información actualizada.
     */
    public void actualizar(LibroModel libroEditado) {
        repository.save(libros);
    }

    /**
     * Elimina un libro de la memoria basándose en su ISBN y actualiza el almacenamiento.
     * @param isbn Identificador único del libro a remover.
     */
    public void eliminar(String isbn) {
        // Uso de predicados para una eliminación eficiente y limpia
        libros.removeIf(libro -> libro.getIsbn().equals(isbn));
        repository.save(libros);
    }
}