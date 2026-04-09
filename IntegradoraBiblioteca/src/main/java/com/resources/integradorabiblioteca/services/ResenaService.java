package com.resources.integradorabiblioteca.services;

import com.resources.integradorabiblioteca.model.ResenaModel;
import com.resources.integradorabiblioteca.repositories.ResenaRepository;
import java.util.*;

/**
 * Servicio que administra la logica de negocio para las resenas.
 * Mantiene una lista en memoria para consultas rapidas y coordina el guardado en disco.
 */
public class ResenaService {

    private ResenaRepository repositorio;
    private List<ResenaModel> listaResenas;

    /**
     * Inicializa el servicio cargando las resenas guardadas previamente en el archivo.
     * @param repositorio Instancia del repositorio de resenas.
     */
    public ResenaService(ResenaRepository repositorio) {
        this.repositorio = repositorio;
        this.listaResenas = repositorio.load();
    }

    /**
     * Busca y devuelve todas las resenas asociadas a un libro en particular.
     * @param isbn Identificador unico del libro a consultar.
     * @return Lista con las resenas encontradas.
     */
    public List<ResenaModel> listarPorLibro(String isbn) {
        List<ResenaModel> resultados = new ArrayList<>();

        for (ResenaModel resena : listaResenas) {
            if (resena.getIsbnLibro().equals(isbn)) {
                resultados.add(resena);
            }
        }

        return resultados;
    }

    /**
     * Registra una nueva resena en la memoria y actualiza el archivo fisico de forma inmediata.
     * @param nuevaResena Objeto con los datos de la valoracion a guardar.
     */
    public void agregar(ResenaModel nuevaResena) {
        listaResenas.add(nuevaResena);
        repositorio.save(listaResenas);
    }
}