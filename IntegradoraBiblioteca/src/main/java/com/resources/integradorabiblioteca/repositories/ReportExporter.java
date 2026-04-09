package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Exporta el catalogo de libros a un archivo de texto plano (.txt)
 * con un formato tabular y resumen estadistico, optimizado para lectura directa.
 */
public class ReportExporter {

    /**
     * Genera el documento de inventario en la carpeta de Descargas del usuario actual.
     * @param listaLibros La coleccion de libros que se incluiran en el reporte.
     * @throws IOException Si ocurre un error de permisos o acceso al disco.
     */
    public void exportar(List<LibroModel> listaLibros) throws IOException {

        String directorioUsuario = System.getProperty("user.home");
        File archivoSalida = new File(directorioUsuario + File.separator + "Downloads" + File.separator + "inventario_biblioteca.txt");

        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivoSalida))) {

            int disponibles = 0;
            int prestados = 0;

            escritor.println("==========================================================");
            escritor.println("                INVENTARIO DE BIBLIOTECA");
            escritor.println("==========================================================");
            escritor.println("Fecha de generacion: " + LocalDate.now());
            escritor.println();

            for (LibroModel libro : listaLibros) {

                String estado = libro.isDisponible() ? "[DISP]" : "[NODISP]";
                escritor.printf("%-7s %-35s | ISBN: %s%n", estado, libro.getTitulo(), libro.getIsbn());

                if (libro.isDisponible()) {
                    disponibles++;
                } else {
                    prestados++;
                }
            }

            escritor.println();
            escritor.println("----------------------------------------------------------");
            escritor.printf("Resumen: %d libros totales.%n", listaLibros.size());
            escritor.printf("Disponibles: %d | Prestados: %d%n", disponibles, prestados);
            escritor.println("----------------------------------------------------------");
        }
    }
}