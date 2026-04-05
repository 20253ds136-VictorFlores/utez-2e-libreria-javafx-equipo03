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

## 3. Explicación de Persistencia y Exportación (Reglas de Negocio)

### Persistencia en Archivo Local
El sistema no utiliza bases de datos ni depende únicamente de la memoria RAM. Utiliza un repositorio basado en archivos de texto estructurado (`.csv`) ubicado en la ruta `data/books.csv`.
* **Al iniciar la aplicación (Read):** El servicio lee el archivo línea por línea, separando los datos por comas, e instancia los objetos de tipo `Book` para mostrarlos en la tabla. Si el archivo no existe, el sistema lo crea automáticamente.
* **Al modificar datos (Write):** Cada vez que se ejecuta una operación de Alta, Actualización o Eliminación, el sistema reescribe el archivo `books.csv` con la lista actualizada de la memoria. Esto garantiza que ante un cierre inesperado, el último estado de la tabla siempre esté guardado.

### Exportación del Reporte
Como funcionalidad extra, el sistema incluye un botón para generar un reporte del catálogo. Al accionarlo, el sistema toma la lista actual de libros y genera un nuevo archivo llamado `reporte_catalogo.csv` en la raíz del proyecto. Este archivo incluye encabezados de columna y puede ser abierto en software de hojas de cálculo (como Excel) para auditorías externas.

---

## 4. Arquitectura de la Interfaz (JavaFX)

La aplicación cuenta con 3 pantallas principales para separar las responsabilidades visuales:

1. **Pantalla Principal (Catálogo):**
    - Tabla interactiva con las columnas: ISBN/ID, Título, Autor, Año, Género y Disponibilidad.
    - Botones de acción principales (Nuevo, Editar, Eliminar, Ver Detalle, Exportar Reporte).
2. **Pantalla de Formulario (Altas y Ediciones):**
    - Campos de texto y selección para capturar los datos del libro.
    - Lógica compartida: se adapta dinámicamente si se va a registrar un nuevo libro o a editar uno existente (bloqueando la edición del ISBN).
3. **Pantalla de Detalle:**
    - Vista de solo lectura que muestra la información completa del registro seleccionado sin riesgo de modificación accidental.

---

## 5. Validaciones y Manejo de Errores

El sistema implementa manejo de excepciones (`try/catch`) y reglas de negocio obligatorias:
- **Campos vacíos:** No se permite registrar ni actualizar si hay campos en blanco.
- **Longitud de texto:** El título y el autor requieren un mínimo de 3 caracteres.
- **Rango numérico:** El año de publicación debe ser un número entero válido comprendido entre el año 1500 y el año actual.
- **Integridad de datos:** No se permite el registro de dos libros con el mismo ISBN/ID (validación de duplicidad).