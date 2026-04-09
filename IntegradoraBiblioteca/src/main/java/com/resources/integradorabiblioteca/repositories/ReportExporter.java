package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase de utilidad encargada de la exportación de información a formatos de lectura humana.
 * Genera reportes en texto plano (.txt) utilizando estructuras visuales de tipo ASCII
 * para facilitar la interpretación de los datos sin necesidad de software especializado.
 */
public class ReportExporter {

    /**
     * Procesa la lista de libros y genera un documento de inventario en la carpeta
     * de descargas del sistema operativo del usuario.
     * * @param lista La colección de libros que se incluirán en el reporte.
     * @throws IOException Si ocurre un error de acceso a disco o permisos denegados.
     */
    public void exportar(List<LibroModel> lista) throws IOException {
        // Localización dinámica de la carpeta raíz del usuario (ej: C:\Users\Nombre).
        String home = System.getProperty("user.home");

        // Construcción de la ruta hacia 'Downloads', asegurando compatibilidad
        // entre sistemas (Windows/Linux) mediante File.separator.
        File archivo = new File(home + File.separator + "Downloads" + File.separator + "inventario_biblioteca.txt");

        // Apertura del flujo de escritura con gestión automática de cierre.
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // Contadores para el resumen estadístico final.
            int disponibles = 0;
            int prestados = 0;

            // --- Bloque de Encabezado ---
            pw.println("==========================================================");
            pw.println("                INVENTARIO DE BIBLIOTECA");
            pw.println("==========================================================");
            pw.println("Fecha de generación: " + LocalDate.now());
            pw.println();

            // --- Bloque de Listado de Datos ---
            for (LibroModel l : lista) {
                // Traducción del estado booleano a una etiqueta visual legible.
                String status = l.isDisponible() ? "[DISP]" : "[NODISP]";

                /**
                 * Formateo de columnas:
                 * %-7s: Reserva 7 espacios alineados a la izquierda para el estado.
                 * %-35s: Reserva 35 espacios para el título, evitando que el ISBN se desalinee.
                 * %n: Representa un salto de línea independiente de la plataforma.
                 */
                pw.printf("%-7s %-35s | ISBN: %s%n", status, l.getTitulo(), l.getIsbn());

                // Actualización de contadores estadísticos según el estado del libro.
                if (l.isDisponible()) disponibles++; else prestados++;
            }

            // --- Bloque de Resumen (Pie de Página) ---
            pw.println();
            pw.println("----------------------------------------------------------");
            pw.printf("Resumen: %d libros totales.%n", lista.size());
            pw.printf("Disponibles: %d | Prestados: %d%n", disponibles, prestados);
            pw.println("----------------------------------------------------------");
        }
    }
}