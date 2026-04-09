package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.*;
import java.util.*;
import java.time.Year;

/**
 * Servicio encargado de gestionar las reglas de integridad del catálogo.
 * Incluye validaciones estructurales y de negocio, como la verificación
 * de títulos y códigos ISBN duplicados.
 */
public class LibraryService {
    private final FileRepository repo;
    private final ReportExporter exporter;
    private final List<LibroModel> cache;

    /**
     * Constructor de la clase. Inicializa las dependencias y carga
     * el inventario en memoria.
     *
     * @param r Repositorio para la persistencia física de los libros.
     */
    public LibraryService(FileRepository r) {
        this.repo = r;
        this.exporter = new ReportExporter();
        this.cache = r.load();
    }

    /**
     * Recupera la lista de libros en el inventario actual.
     *
     * @return Lista de objetos LibroModel en memoria.
     */
    public List<LibroModel> listar() {
        return cache;
    }

    /**
     * Registra un nuevo libro en el sistema validando previamente sus datos.
     *
     * @param l El objeto LibroModel a agregar.
     * @throws Exception Si los datos no cumplen con las reglas de negocio.
     */
    public void agregar(LibroModel l) throws Exception {
        validar(l, true);
        cache.add(l);
        repo.save(cache);
    }

    /**
     * Actualiza un registro existente aplicando las validaciones pertinentes.
     *
     * @param l El objeto LibroModel modificado.
     * @throws Exception Si los datos actualizados son inválidos.
     */
    public void actualizar(LibroModel l) throws Exception {
        validar(l, false);
        repo.save(cache);
    }

    /**
     * Valida que el libro cumpla con las normas estipuladas por la biblioteca.
     *
     * @param l     El objeto LibroModel a evaluar.
     * @param nuevo Indica si es un registro nuevo (true) para aplicar validaciones
     * de unicidad, o una actualización (false) para omitirlas.
     * @throws Exception Si falta información obligatoria, el año es superior al actual,
     * o si existe colisión de datos (ISBN o Título duplicados).
     */
    private void validar(LibroModel l, boolean nuevo) throws Exception {
        if (l.getIsbn().isBlank() || l.getTitulo().isBlank()) {
            throw new Exception("Todos los campos obligatorios deben estar llenos.");
        }

        if (l.getAnio() > Year.now().getValue()) {
            throw new Exception("El año no puede ser mayor al actual.");
        }

        if (nuevo) {
            boolean isbnExiste = cache.stream()
                    .anyMatch(b -> b.getIsbn().equals(l.getIsbn()));
            if (isbnExiste) throw new Exception("Error: El ISBN ya está registrado.");

            boolean tituloExiste = cache.stream()
                    .anyMatch(b -> b.getTitulo().equalsIgnoreCase(l.getTitulo()));
            if (tituloExiste) throw new Exception("Error: Ya existe un libro con ese nombre en el sistema.");
        }
    }

    /**
     * Exporta el inventario actual a un reporte físico.
     *
     * @throws Exception Si ocurre un problema de entrada/salida durante la generación.
     */
    public void generarReporte() throws Exception {
        exporter.exportar(cache);
    }

    /**
     * Elimina un libro de la memoria y actualiza la persistencia física.
     *
     * @param isbn Identificador único del libro a eliminar.
     */
    public void eliminar(String isbn) {
        cache.removeIf(b -> b.getIsbn().equals(isbn));
        repo.save(cache);
    }
}