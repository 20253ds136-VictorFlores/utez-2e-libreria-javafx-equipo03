# utez-2e-libreria-javafx-equipo03
Integradora JavaFX — CRUD con Persistencia en Archivo


Tecnología: Java + JavaFX (FXML + Controller)

Modalidad: Parejas

Equipo: Rene Alejandro Salgado Uresti y Victor Alexander Flores Villegas

Grupo: 2°E

Descripción del proyecto

Este proyecto consiste en el desarrollo de una aplicación de escritorio para la gestión de un catálogo de libros de una biblioteca escolar.

El sistema permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre los libros, además de mantener la información guardada mediante persistencia en archivo local, asegurando que los datos se conserven entre ejecuciones del programa.

La aplicación fue desarrollada utilizando JavaFX para la interfaz gráfica y aplicando principios básicos de la Programación Orientada a Objetos (POO).

Objetivo

Desarrollar una aplicación de escritorio funcional que implemente:

Un sistema CRUD completo

Persistencia de datos en archivo

Validaciones de datos

Manejo de errores

Interfaz gráfica con múltiples pantallas

Requerimientos de software



Para ejecutar el proyecto se necesita:

Java JDK 8 o superior

JavaFX SDK (compatible con la versión de Java)

IDE recomendado: IntelliJ IDEA / NetBeans / Eclipse

Sistema operativo: Windows, Linux o macOS


Funcionalidades:

CRUD de libros

Alta: Registro de nuevos libros mediante formulario

Consulta: Visualización del catálogo en una tabla

Actualización: Edición de libros existentes

Eliminación: Eliminación con confirmación

Persistencia en archivo

Guardado de datos en archivo .csv o .txt

Carga automática al iniciar la aplicación

Actualización del archivo al modificar datos

Validaciones

Campos obligatorios (no vacíos)

Título y autor con mínimo 3 caracteres

Año numérico dentro de un rango válido

Evitar duplicados por ISBN o ID

Funcionalidades extra

Pantalla de detalle del libro

Exportación de reporte (reporte_catalogo.csv)

Interfaz de usuario

La aplicación cuenta con 3 pantallas principales:

1. Pantalla principal
Tabla (TableView) con columnas:
ISBN/ID

Título

Autor

Año

Género

Disponibilidad


Botones:

Nuevo

Editar

Eliminar

Ver detalle

Exportar reporte

3. Formulario
   
Campos para registrar o editar libros

Botones:

Guardar

Cancelar

5. Pantalla de detalle
   
Muestra la información completa del libro

Botón para regresar

Persistencia de datos

El sistema utiliza archivos locales para almacenar la información de los libros.

Formato: .csv o .txt

Al iniciar: se cargan los datos desde el archivo

Al realizar cambios: se actualiza automáticamente el archivo

Permite mantener la información entre ejecuciones

Exportación de reporte


Se genera un archivo llamado:

reporte_catalogo.csv

Este archivo contiene todos los libros registrados en el sistema en formato estructurado, permitiendo su uso externo (por ejemplo, en Excel).
