package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.FileRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de servicio encargada de la lógica de negocio de la biblioteca.
 * Actúa como intermediario entre los controladores (interfaz de usuario) y
 * el repositorio (persistencia de datos). Mantiene una lista en memoria
 * para operaciones rápidas y sincroniza los cambios con el archivo de almacenamiento.
 */
public class LibraryService {

    /** Repositorio utilizado para leer y guardar los datos permanentemente. */
    private final FileRepository repository;

    /** Lista en memoria que actúa como caché temporal de los libros. */
    private List<LibroModel> libros;

    /**
     * Constructor del servicio. Al instanciarse, inyecta el repositorio
     * y precarga inmediatamente los datos almacenados en memoria.
     * * @param repository Instancia de {@link FileRepository} para gestionar la persistencia.
     */
    public LibraryService(FileRepository repository) {
        this.repository = repository;
        this.libros = repository.load();
    }

    /**
     * Obtiene la lista completa de libros registrados.
     * Devuelve una nueva instancia de ArrayList (copia defensiva) para evitar
     * que la lista original sea modificada accidentalmente desde fuera del servicio.
     * * @return Una lista con todos los objetos {@link LibroModel}.
     */
    public List<LibroModel> listar() {
        return new ArrayList<>(libros);
    }

    /**
     * Agrega un nuevo libro al sistema.
     * Primero verifica si el libro ya existe (utilizando el método equals() de LibroModel,
     * el cual compara por ISBN). Si no existe, lo agrega a la lista en memoria y
     * guarda los cambios en el archivo.
     * * @param libro El nuevo objeto {@link LibroModel} a registrar.
     * @return true si el libro se agregó correctamente, false si ya existía un libro con ese ISBN.
     */
    public boolean agregar(LibroModel libro) {
        // contains() internamente llama al equals() de LibroModel
        if (libros.contains(libro)) return false;

        libros.add(libro);
        repository.save(libros);
        return true;
    }

    /**
     * Actualiza la información de un libro existente.
     * Busca en la lista un libro que tenga el mismo ISBN y lo reemplaza con
     * la nueva información proporcionada, guardando los cambios posteriormente.
     * * @param libro El objeto {@link LibroModel} con los datos actualizados.
     * @return true si se encontró y actualizó el libro, false si el ISBN no fue encontrado.
     */
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

    /**
     * Elimina un libro del sistema basándose en su ISBN.
     * Si la eliminación en memoria es exitosa, sincroniza el cambio guardando
     * la nueva lista en el archivo.
     * * @param isbn El identificador (ISBN) del libro que se desea eliminar.
     * @return true si el libro fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminar(String isbn) {
        // removeIf() elimina elementos que cumplan con la condición dada
        boolean removed = libros.removeIf(l -> l.getIsbn().equals(isbn));

        if (removed) repository.save(libros);
        return removed;
    }
}