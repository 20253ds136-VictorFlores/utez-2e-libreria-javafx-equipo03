package com.resources.integradorabiblioteca;

/**
 * Punto de entrada alternativo para evadir restricciones de ejecucion de modulos al compilar un Fat-JAR.
 */
public class Launcher {

    /**
     * Metodo base que arranca la aplicacion saltando configuraciones complejas de modulos JavaFX.
     * @param args Argumentos de ejecucion pasados por la linea de comandos.
     */
    public static void main(String[] args) {
        RunApplication.main(args);
    }
}
