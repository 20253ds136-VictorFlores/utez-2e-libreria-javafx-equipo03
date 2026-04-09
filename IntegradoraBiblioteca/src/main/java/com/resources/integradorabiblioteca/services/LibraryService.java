package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.LibroModel;
import com.resources.integradorabiblioteca.repositories.*;
import java.util.*;

/**
 * Clase central de la capa de Servicios que orquestas todas las reglas de negocio
 * del inventario. Esta clase gestiona la comunicación entre los controladores
 * de la interfaz y el almacenamiento físico de datos, asegurando la integridad
 * de la información.
 */
public class LibraryService {

    /** Repositorio encargado de leer y escribir en el archivo CSV de libros. */
    private final FileRepository repo;

    /** Servicio especializado en la generación de reportes externos (TXT). */
    private final ReportExporter exporter;

    /** Lista maestra en memoria que representa el catálogo activo durante la ejecución. */
    private final List<LibroModel> inventario;

    /**
     * Constructor del servicio. Realiza la inyección del repositorio y
     * sincroniza la memoria RAM con el archivo de disco al iniciar la aplicación.
     * * @param repo Dependencia de persistencia necesaria para la carga de datos.
     */
    public LibraryService(FileRepository repo) {
        this.repo = repo;
        this.exporter = new ReportExporter();
        // Se puebla la lista inicial con los datos recuperados del archivo CSV.
        this.inventario = repo.load();
    }

    /**
     * Proporciona acceso al catálogo completo de libros.
     * * @return El objeto {@code List<LibroModel>} que reside en la memoria activa.
     */
    public List<LibroModel> listar() {
        return inventario;
    }

    /**
     * Gestiona la actualización de los metadatos de un libro existente.
     * Valida que los nuevos datos cumplan las normas antes de confirmar la escritura.
     * * @param l Referencia del objeto LibroModel que contiene los cambios.
     * @throws Exception Si la validación de integridad detecta datos erróneos.
     */
    public void actualizar(LibroModel l) throws Exception {
        // Se valida el estado del objeto sin aplicar reglas de duplicidad.
        validar(l, false);
        // Se solicita al repositorio que sobrescriba el archivo con la lista actualizada.
        repo.save(inventario);
    }

    /**
     * Procesa la inserción de un nuevo ejemplar al catálogo.
     * Realiza una validación rigurosa para evitar que se repitan títulos o códigos ISBN.
     * * @param l Nueva instancia de LibroModel a registrar.
     * @throws Exception Si el libro ya existe o si la información está incompleta.
     */
    public void agregar(LibroModel l) throws Exception {
        // Validación estricta incluyendo búsqueda de duplicados.
        validar(l, true);
        // Incorporación a la colección en memoria.
        inventario.add(l);
        // Sincronización inmediata con el almacenamiento físico.
        repo.save(inventario);
    }

    /**
     * Motor de validación de reglas de negocio.
     * Implementa una búsqueda lineal manual para verificar la unicidad de los registros.
     * * @param l       El libro a evaluar.
     * @param esNuevo Si es verdadero, activa la detección de duplicados en la lista.
     * @throws Exception Lanza un error con un mensaje descriptivo si la regla se rompe.
     */
    private void validar(LibroModel l, boolean esNuevo) throws Exception {
        // Regla 1: Verificación de presencia de datos esenciales.
        if (l.getIsbn().isBlank() || l.getTitulo().isBlank()) {
            throw new Exception("Error: Campos obligatorios vacíos.");
        }

        // Regla 2: Prevención de colisiones en registros nuevos.
        if (esNuevo) {
            // Bucle tradicional para inspeccionar cada elemento del inventario.
            for (LibroModel libro : inventario) {
                // Comparación de Identificador Único (ISBN).
                if (libro.getIsbn().equals(l.getIsbn())) {
                    throw new Exception("Error: El ISBN ya existe.");
                }
                // Comparación de Título (Ignorando diferencias de mayúsculas).
                if (libro.getTitulo().equalsIgnoreCase(l.getTitulo())) {
                    throw new Exception("Error: Ya existe un libro con ese título.");
                }
            }
        }
    }

    /**
     * Ejecuta la baja de un libro utilizando un Iterator para garantizar la
     * estabilidad de la colección durante el proceso de eliminación.
     * * @param isbn Código identificador del libro que se desea remover.
     */
    public void eliminar(String isbn) {
        // Uso de un iterador para navegar por la lista de forma segura.
        Iterator<LibroModel> it = inventario.iterator();
        while (it.hasNext()) {
            LibroModel l = it.next();
            // Si se localiza la coincidencia, se remueve a través del iterador.
            if (l.getIsbn().equals(isbn)) {
                it.remove();
            }
        }
        // Se actualiza el archivo físico tras la eliminación en memoria.
        repo.save(inventario);
    }

    /**
     * Dispara la generación del reporte externo en la carpeta de descargas.
     * * @throws Exception Si ocurre un fallo en el flujo de salida del sistema de archivos.
     */
    public void generarReporte() throws Exception {
        exporter.exportar(inventario);
    }
}