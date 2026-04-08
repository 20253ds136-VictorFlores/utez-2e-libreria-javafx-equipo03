package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private final String filePath;

    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<LibroModel> load() {
        List<LibroModel> libros = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return libros;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 6) {
                    libros.add(new LibroModel(
                            data[0], data[1], data[2],
                            Integer.parseInt(data[3]),
                            data[4], Boolean.parseBoolean(data[5])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public void save(List<LibroModel> libros) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (LibroModel libro : libros) {
                pw.println(libro.getIsbn() + ";" + libro.getTitulo() + ";" + libro.getAutor() + ";" +
                        libro.getAnio() + ";" + libro.getGenero() + ";" + libro.isDisponible());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}