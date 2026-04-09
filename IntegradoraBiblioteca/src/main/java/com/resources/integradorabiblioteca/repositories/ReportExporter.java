package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase encargada de generar reportes visuales en formato de texto plano (.txt).
 * El diseño sigue un formato de tabla ASCII para facilitar su lectura rápida.
 */
public class ReportExporter {

    /**
     * Genera un archivo .txt en la carpeta de Descargas con el inventario formateado.
     *
     * @param lista Lista de libros a procesar.
     * @throws IOException Si ocurre un error al escribir en el disco.
     */
    public void exportar(List<LibroModel> lista) throws IOException {
        String home = System.getProperty("user.home");
        File archivo = new File(home + File.separator + "Downloads" + File.separator + "inventario_biblioteca.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            int disponibles = 0;
            int prestados = 0;

            pw.println("==========================================================");
            pw.println("                INVENTARIO DE BIBLIOTECA");
            pw.println("==========================================================");
            pw.println("Fecha de generación: " + LocalDate.now());
            pw.println();

            for (LibroModel l : lista) {
                String status = l.isDisponible() ? "[DISP]" : "[NODISP]";

                pw.printf("%-7s %-35s | ISBN: %s%n", status, l.getTitulo(), l.getIsbn());

                if (l.isDisponible()) disponibles++; else prestados++;
            }

            pw.println();
            pw.println("----------------------------------------------------------");
            pw.printf("Resumen: %d libros totales.%n", lista.size());
            pw.printf("Disponibles: %d | Prestados: %d%n", disponibles, prestados);
            pw.println("----------------------------------------------------------");
        }
    }
}