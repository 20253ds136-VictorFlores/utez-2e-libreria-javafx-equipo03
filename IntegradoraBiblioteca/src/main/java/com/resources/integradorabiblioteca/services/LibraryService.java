package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.*;
import java.util.*;

/**
 * Servicio de lógica de negocio encargado de gestionar el inventario de la biblioteca.
 * Implementación estructurada con un enfoque tradicional (sin uso de lambdas o streams)
 * para el manejo de colecciones y validaciones.
 */
public class LibraryService {

    private final FileRepository repo;
    private final ReportExporter exporter;
    private final List<LibroModel> inventario;

    /**
     * Constructor de la clase. Inicializa las dependencias y carga
     * el inventario en memoria desde el repositorio físico.
     *
     * @param repo Repositorio para la persistencia de los libros.
     */
    public LibraryService(FileRepository repo) {
        this.repo = repo;
        this.exporter = new ReportExporter();
        this.inventario = repo.load();
    }

    /**
     * Recupera la lista de libros en el inventario actual.
     *
     * @return Lista de objetos LibroModel en memoria.
     */
    public List<LibroModel> listar() {
        return inventario;
    }

    /**
     * Guarda los cambios de un libro que ya existe en la colección.
     * Al ser una referencia en memoria, los cambios de los atributos ya están
     * reflejados; este método valida la integridad y persiste el estado actual.
     *
     * @param l El objeto LibroModel modificado.
     * @throws Exception Si los datos actualizados son inválidos.
     */
    public void actualizar(LibroModel l) throws Exception {
        validar(l, false);
        repo.save(inventario);
    }

    /**
     * Registra un nuevo libro en el sistema validando previamente sus datos
     * y asegurando que no existan colisiones en el inventario.
     *
     * @param l El objeto LibroModel a agregar.
     * @throws Exception Si los datos no cumplen con las reglas de negocio.
     */
    public void agregar(LibroModel l) throws Exception {
        validar(l, true);
        inventario.add(l);
        repo.save(inventario);
    }

    /**
     * Valida que el libro cumpla con las normas de captura estipuladas.
     * Evalúa la presencia de datos obligatorios y previene la duplicidad de registros.
     *
     * @param l       El objeto LibroModel a evaluar.
     * @param esNuevo Indicador para aplicar o no la validación de duplicidad (ISBN y Título).
     * @throws Exception Si falta información o si se detecta un registro duplicado.
     */
    private void validar(LibroModel l, boolean esNuevo) throws Exception {
        if (l.getIsbn().isBlank() || l.getTitulo().isBlank()) {
            throw new Exception("Error: Campos obligatorios vacíos.");
        }

        if (esNuevo) {
            for (LibroModel libro : inventario) {
                if (libro.getIsbn().equals(l.getIsbn())) {
                    throw new Exception("Error: El ISBN ya existe.");
                }
                if (libro.getTitulo().equalsIgnoreCase(l.getTitulo())) {
                    throw new Exception("Error: Ya existe un libro con ese título.");
                }
            }
        }
    }

    /**
     * Elimina un libro de la memoria recorriendo la colección de manera segura
     * y actualiza la persistencia física.
     *
     * @param isbn Identificador único del libro a eliminar.
     */
    public void eliminar(String isbn) {
        Iterator<LibroModel> it = inventario.iterator();
        while (it.hasNext()) {
            LibroModel l = it.next();
            if (l.getIsbn().equals(isbn)) {
                it.remove();
            }
        }
        repo.save(inventario);
    }

    /**
     * Exporta el inventario actual a un reporte físico utilizando el servicio externo.
     *
     * @throws Exception Si ocurre un problema durante la generación del archivo.
     */
    public void generarReporte() throws Exception {
        exporter.exportar(inventario);
    }
}