package com.resources.integradorabiblioteca.repositories;

import com.resources.integradorabiblioteca.model.ResenaModel;
import java.io.*;
import java.util.*;

public class ResenaRepository {
    private final String RUTA;

    public ResenaRepository(String ruta) { this.RUTA = ruta; }

    public List<ResenaModel> load() {
        List<ResenaModel> lista = new ArrayList<>();
        File f = new File(RUTA);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] d = l.split(";");
                if (d.length >= 4) lista.add(new ResenaModel(d[0], d[1], Integer.parseInt(d[2]), d[3]));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public void save(List<ResenaModel> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA))) {
            for (ResenaModel r : lista) pw.println(r.getIdResena() + ";" + r.getIsbnLibro() + ";" + r.getCalificacion() + ";" + r.getComentario());
        } catch (Exception e) { e.printStackTrace(); }
    }
}