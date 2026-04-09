# utez-2e-libreria-javafx-equipo03

## Integradora JavaFX — CRUD con Persistencia en Archivo

**Tecnología:** Java + JavaFX (FXML + Controllers)  
**Modalidad:** Parejas  
**Equipo:** Rene Alejandro Salgado Uresti y Victor Alexander Flores Villegas  
**Grupo:** 2°E

---

## 1. Descripción del proyecto
Este proyecto consiste en el desarrollo de una aplicación de escritorio basada en el patrón MVC para la gestión del catálogo de libros de una biblioteca escolar.

El sistema permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre los registros, aplicando principios de la Programación Orientada a Objetos (POO). Además, incorpora un sistema de persistencia de datos mediante archivos locales (`.csv`), asegurando que la información del catálogo se conserve entre las distintas ejecuciones del programa de manera autónoma, sin depender de bases de datos externas.

---

## 2. Pasos de ejecución

Al ser un proyecto gestionado con Maven, la ejecución es directa desde el IDE. Sigue estos pasos:

1. **Clonar el repositorio:** `git clone https://github.com/TU-USUARIO/utez-2e-libreria-javafx-equipo03.git`

2. **Abrir el proyecto:**
    - Abre tu IDE (IntelliJ IDEA recomendado, NetBeans o Eclipse).
    - Selecciona `Open` o `Import Project` y elige la carpeta raíz del repositorio clonado.
    - Espera a que el IDE descargue las dependencias de JavaFX definidas en el archivo `pom.xml`.

3. **Ejecutar la aplicación:**
    - Navega hasta el paquete `src/main/java/com/resources/integradorabiblioteca/`.
    - Localiza la clase `Launcher.java` (o `RunApplication.java`).
    - Haz clic derecho sobre el archivo y selecciona **Run 'Launcher.main()'**.

---
## 4. Descripción General
Este proyecto es una solución integral diseñada para la administración de acervos escolares. Permite gestionar de forma eficiente el inventario físico de libros y ofrece un sistema interactivo de reseñas y calificaciones. La aplicación se destaca por su arquitectura robusta y un enfoque preventivo en la seguridad y persistencia de los datos.

## 5. Objetivo
Desarrollar una aplicación de escritorio profesional utilizando **JavaFX 21**, bajo los siguientes pilares:
* **Arquitectura de Capas:** Separación total de responsabilidades para un código escalable y mantenible.
* **Programación Imperativa:** Implementación de lógica mediante estructuras tradicionales (Iteradores, Clases Anónimas, Bucles `for`) para un control explícito del flujo, sin uso de lambdas.
* **Integridad de Datos:** Validación rigurosa de entradas y persistencia local sincronizada.

---

## 6. Arquitectura y Lógica de Negocio

### Gestión de Inventario (CRUD)
* **Alta y Edición:** Formulario dinámico con validación de campos. Durante la edición, se protege la integridad de la llave primaria (ISBN).
* **Buscador Reactivo:** Filtro en tiempo real mediante `FilteredList` que responde a criterios de Título o ISBN conforme el usuario escribe.
* **Borrado Seguro (Failsafe):** Mecanismo de validación que obliga al usuario a reescribir manualmente el ISBN para confirmar la eliminación de un registro.

### Sistema de Reseñas
* **Relación Lógica:** Implementación de un modelo relacional donde las reseñas se vinculan a los libros mediante el ISBN (Foreign Key).
* **Identidad:** Generación de identificadores únicos universales (**UUID**) para cada valoración.

### Reglas de Validación
* **Unicidad:** Restricción de duplicados por ISBN y Título (case-insensitive).
* **Tipado:** Manejo de excepciones para asegurar que el Año y la Calificación sean numéricos.
* **Rango Cronológico:** Solo se permiten libros publicados entre **1450 y 2026**.

---

## 7. Estructura del Proyecto
Organización de archivos basada en el estándar de Maven:

```text
IntegradoraBiblioteca/
├── data/                         # Almacenamiento físico (.csv)
├── src/main/java/com.resources.integradorabiblioteca/
│   ├── model/                    # Entidades de datos
│   ├── repositories/             # Capa de persistencia (I/O)
│   ├── services/                 # Lógica de negocio y validación
│   ├── controllers/              # Controladores de la UI (FXML)
│   ├── Launcher.java             # Clase de entrada auxiliar
│   └── RunApplication.java       # Clase principal de JavaFX
├── src/main/resources/           # Vistas (Archivos .fxml)
└── module-info.java              # Configuración de módulos
```
## 8. Persistencia y Reportes
Sincronización: Cada cambio se guarda inmediatamente en los archivos .csv ubicados en la carpeta data/.

Reporte de Inventario: Genera un archivo profesional llamado inventario_biblioteca.txt en la carpeta de Descargas del sistema, con estadísticas de disponibilidad y formato de tabla ASCII.

## 9. Datos de Prueba
El sistema incluye el archivo libros.csv con 5 registros de prueba precargados. Estos datos permiten evaluar las funciones de búsqueda, filtrado y generación de reportes desde la primera ejecución.

## 10. Flujo de Ramas (Git Flow)
Se ha implementado una estrategia de ramificación para asegurar la estabilidad del proyecto:

main: Versión estable y lista para entrega.

dev: Rama de integración de módulos finalizados.

Ramas personales: Formato <usuario>/nombre-apellido para el desarrollo individual de componentes.

Proceso: Rama Personal ➔ dev (Pruebas) ➔ main (Entrega Final).
