package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.*;
import com.resources.integradorabiblioteca.repositories.*;
import java.io.IOException;
import java.time.Year;
import java.util.List;

/**
 * Servicio para gestionar el catálogo de libros:
 * carga, alta, edición, eliminación y exportación de reportes.
 */
public class LibraryService {

    private FileRepository fileRepository;
    private ReportExporter reportExporter;
    private List<Book> catalogo;

    /**
     * Inicializa el servicio cargando el catálogo desde archivo.
     */
    public LibraryService() {
        this.fileRepository = new FileRepository();
        this.reportExporter = new ReportExporter();
        try {
            this.catalogo = fileRepository.loadBooks();
        } catch (IOException e) {
            System.err.println("Error inicializando el catálogo: " + e.getMessage());
        }
    }

    /**
     * Devuelve el catálogo actual.
     * @return lista de libros
     */
    public List<Book> getCatalogo() {
        return catalogo;
    }


}
