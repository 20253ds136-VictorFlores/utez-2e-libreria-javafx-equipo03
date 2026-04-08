package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.repositories.*;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de procesar la logica de negocio y validaciones del dominio de biblioteca,
 * actuando como mediador entre los controladores de vista y las capas de acceso a datos.
 */
public class LibraryService {

    private final int ANIOLIMIT = 1500;
    private final int CHARLIMIT = 3;

    private FileRepository fileRepository;
    private ReportExporter reportExporter;
    private List<Libro> catalogo;

    /**
     * Construye el servicio instanciando los repositorios necesarios y cargando el estado inicial del catalogo en memoria.
     */
    public LibraryService() {
        this.fileRepository = new FileRepository();
        this.reportExporter = new ReportExporter();
        try {
            this.catalogo = fileRepository.loadBooks();
        } catch (IOException e) {
            System.err.println("Error inicializando el catalogo: " + e.getMessage());
        }
    }

    /**
     * Recupera el estado en memoria de la coleccion completa de libros.
     * @return Lista de todos los registros disponibles en la sesion actual.
     */
    public List<Libro> getCatalogo() {
        return catalogo;
    }

    /**
     * Incorpora un registro nuevo a la coleccion principal despues de someterlo a validaciones.
     * @param nuevoLibro Objeto que contiene las caracteristicas del nuevo registro a ingresar.
     * @throws Exception Si las propiedades del objeto violan alguna de las reglas de negocio.
     */
    public void agregarLibro(Libro nuevoLibro) throws Exception {
        validar(nuevoLibro, true);
        catalogo.add(nuevoLibro);
        fileRepository.saveBooks(catalogo);
    }

    /**
     * Modifica los valores de un registro preexistente e impacta dichos cambios en la base de archivos.
     * @param libroEditado Objeto Libro que incluye las nuevas modificaciones, conservando el ISBN original.
     * @throws Exception Si los nuevos valores infringen reglas minimas o validaciones obligatorias.
     */
    public void actualizarLibro(Libro libroEditado) throws Exception {
        validar(libroEditado, false);
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getIsbn().equals(libroEditado.getIsbn())) {
                catalogo.set(i, libroEditado);
                break;
            }
        }
        fileRepository.saveBooks(catalogo);
    }

    /**
     * Remueve un elemento especifico del catalogo mediante la evaluacion de su clave principal (ISBN).
     * @param isbn Secuencia de caracteres que identifica univocamente al objeto a excluir.
     * @throws IOException Si ocurre un fallo durante la sobreescritura del archivo tras la remocion.
     */
    public void eliminarLibro(String isbn) throws IOException {
        for (int i = 0; i < catalogo.size(); i++) {
            if (catalogo.get(i).getIsbn().equals(isbn)) {
                catalogo.remove(i);
                fileRepository.saveBooks(catalogo);
                break;
            }
        }
    }

    /**
     * Procesa y delega la generacion de un documento analitico considerando unicamente los libros habilitados.
     * @throws IOException Si el exportador encuentra bloqueos o excepciones de acceso a disco.
     */
    public void exportarReporte() throws IOException {
        List<Libro> soloDisponibles = new ArrayList<>();
        for (Libro libro : catalogo) {
            if (libro.isDisponible()) {
                soloDisponibles.add(libro);
            }
        }
        reportExporter.exportarCatalogo(soloDisponibles);
    }

    /**
     * Evalua las reglas estrictas de consistencia y logica de negocio frente a una instancia de Libro.
     * @param libro Entidad a someter al proceso de validacion.
     * @param esNuevo Bandera que determina si se debe realizar la comprobacion de unicidad del ISBN.
     * @throws Exception Si cualquier parametro del libro esta vacio, o incumple limites historicos y de longitud.
     */
    private void validar(Libro libro, boolean esNuevo) throws Exception {
        if (libro.getIsbn().trim().isEmpty() || libro.getTitulo().trim().isEmpty() ||
                libro.getAutor().trim().isEmpty() || libro.getGenero().trim().isEmpty()) {
            throw new Exception("Los campos obligatorios no pueden estar vacios.");
        }

        if (libro.getTitulo().length() < CHARLIMIT) throw new Exception("El titulo requiere minimo 3 caracteres.");
        if (libro.getAutor().length() < CHARLIMIT) throw new Exception("El autor requiere minimo 3 caracteres.");

        int anioActual = Year.now().getValue();
        if (libro.getAnio() < ANIOLIMIT || libro.getAnio() > anioActual) {
            throw new Exception("El año de publicacion es invalido.");
        }

        if (esNuevo) {
            for (Libro b : catalogo) {
                if (b.getIsbn().equals(libro.getIsbn())) {
                    throw new Exception("El ISBN ingresado ya se encuentra registrado.");
                }
            }
        }
    }
}
