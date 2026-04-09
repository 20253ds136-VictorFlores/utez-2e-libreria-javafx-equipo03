package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.LibroModel;
import java.io.*;
import java.util.*;

public class FileRepository {
    private final String RUTA;

    public FileRepository(String ruta) { this.RUTA = ruta; }

    public List<LibroModel> load() {
        List<LibroModel> lista = new ArrayList<>();
        File f = new File(RUTA);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] d = l.split(";");
                if (d.length >= 6) lista.add(new LibroModel(d[0], d[1], d[2], Integer.parseInt(d[3]), d[4], Boolean.parseBoolean(d[5])));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public void save(List<LibroModel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (LibroModel b : lista) {
                pw.println(b.getIsbn() + ";" + b.getTitulo() + ";" + b.getAutor() + ";" + b.getAnio() + ";" + b.getGenero() + ";" + b.isDisponible());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}