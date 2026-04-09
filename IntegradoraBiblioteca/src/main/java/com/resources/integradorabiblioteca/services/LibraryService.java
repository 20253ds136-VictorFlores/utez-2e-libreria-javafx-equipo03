package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.*;
import java.util.*;

/**
 * Servicio que administra las reglas de negocio del catalogo de libros.
 * Coordina la comunicacion entre la interfaz de usuario y el acceso a datos.
 */
public class LibraryService {

    private final FileRepository repositorio;
    private final ReportExporter exportador;
    private final List<LibroModel> inventario;

    /**
     * Inicializa el servicio conectando el repositorio de archivos y cargando los datos a memoria.
     * @param repositorio Dependencia para leer y guardar los libros en el disco.
     */
    public LibraryService(FileRepository repositorio) {
        this.repositorio = repositorio;
        this.exportador = new ReportExporter();
        this.inventario = repositorio.load();
    }

    /**
     * Devuelve el catalogo completo de libros disponibles en memoria.
     * @return Lista de objetos LibroModel.
     */
    public List<LibroModel> listar() {
        return inventario;
    }

    /**
     * Valida y guarda las modificaciones hechas a un libro existente.
     * @param libro Objeto editado con los nuevos datos.
     * @throws Exception Si falta informacion obligatoria.
     */
    public void actualizar(LibroModel libro) throws Exception {
        validar(libro, false);
        repositorio.save(inventario);
    }

    /**
     * Registra un nuevo libro en el catalogo despues de verificar que no este duplicado.
     * @param nuevoLibro Libro a registrar.
     * @throws Exception Si el libro ya existe o le faltan datos obligatorios.
     */
    public void agregar(LibroModel nuevoLibro) throws Exception {
        validar(nuevoLibro, true);
        inventario.add(nuevoLibro);
        repositorio.save(inventario);
    }

    /**
     * Evalua que el libro cumpla con los datos minimos y verifica duplicados si es un registro nuevo.
     * @param libro Entidad a validar.
     * @param esNuevo Indica si se deben aplicar validaciones de duplicidad en el inventario.
     * @throws Exception Si falla alguna regla de validacion.
     */
    private void validar(LibroModel libro, boolean esNuevo) throws Exception {

        if (libro.getIsbn().isBlank() || libro.getTitulo().isBlank()) {
            throw new Exception("Error: Campos obligatorios vacios.");
        }

        if (esNuevo) {
            for (LibroModel libroExistente : inventario) {

                if (libroExistente.getIsbn().equals(libro.getIsbn())) {
                    throw new Exception("Error: El ISBN ya se encuentra registrado.");
                }

                if (libroExistente.getTitulo().equalsIgnoreCase(libro.getTitulo())) {
                    throw new Exception("Error: Ya existe un libro con ese titulo.");
                }
            }
        }
    }

    /**
     * Busca y elimina un libro del catalogo usando su ISBN, actualizando el archivo final.
     * @param isbn Identificador unico del libro a eliminar.
     */
    public void eliminar(String isbn) {
        Iterator<LibroModel> iterador = inventario.iterator();

        while (iterador.hasNext()) {
            LibroModel libroActual = iterador.next();

            if (libroActual.getIsbn().equals(isbn)) {
                iterador.remove();
            }
        }

        repositorio.save(inventario);
    }

    /**
     * Crea un archivo de texto con el resumen del catalogo actual en la carpeta Descargas.
     * @throws Exception Si ocurre un error al escribir el archivo.
     */
    public void generarReporte() throws Exception {
        exportador.exportar(inventario);
    }
}