# literalura
Literalura es una aplicación de consola desarrollada en Java que consume la API de Gutendex para buscar libros y registrarlos en una base de datos PostgreSQL. Utiliza Spring Boot, Spring Data JPA y la biblioteca Jackson para el mapeo de JSON a objetos Java.

Características

Menú de consola con 6 opciones principales:

## 1 - Buscar libro por título

Consume la API Gutendex con el título ingresado por el usuario y registra el libro en la base de datos si no está duplicado.

## 2 - Listar libros registrados

Muestra todos los libros guardados en la base de datos.

## 3 - Listar autores registrados
Muestra todos los autores existentes en la base de datos, sin duplicarlos si tienen varios libros.

## 4 - Listar autores vivos en un determinado año
Presenta todos los autores que estaban vivos en el año ingresado (con base en sus años de nacimiento y fallecimiento).

## 5 - Listar libros por idioma
Permite filtrar todos los libros por un idioma específico.

## 0 - Salir
Cierra la aplicación.

Evita duplicados de libros en la base de datos al verificar el título antes de guardar.
Evita duplicados de autores: si ya existe el autor en la base, reutiliza la misma entidad para asociarla a un nuevo libro.
Mapeo de JSON usando Jackson para convertir la respuesta de Gutendex en objetos Java (DTO).

## Tecnologías utilizadas

Java 17 o superior

Spring Boot (3.4.1)

Spring Data JPA

PostgreSQL

Jackson para mapeo JSON

Maven para la gestión de dependencias

IntelliJ IDEA (o tu IDE favorito)

Requisitos previos

Java 17 (o versión compatible con Spring Boot 3.x).

PostgreSQL instalado y corriendo.

Base de datos creada (por ejemplo, literalura).

Configuración en application.properties (o application.yml) con tu URL, usuario y contraseña de la BD.

## Ejemplo de application.properties:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```
## Estructura del proyecto
```
└── src
    └── main
        └── java
            └── com.alura.literalura
                ├── dto
                │   ├── AutorJson.java
                │   ├── LibroJson.java
                │   ├── ResultadoBusqueda.java
                │   └── ConversorJackson.java
                ├── model
                │   ├── Autor.java
                │   └── Libro.java
                ├── repository
                │   ├── AutorRepository.java
                │   └── LibroRepository.java
                ├── service
                │   ├── ClienteGutendex.java
                │   └── LiteraluraService.java
                ├── principal
                │   └── Principal.java
                └── LiteraluraApplication.java
        └── resources
            └── application.properties
```

## Descripción breve de los paquetes


dto: Contiene clases para mapear la respuesta JSON de Gutendex (DTOs) y la clase ConversorJackson para parsear el JSON.

model: Entidades JPA (Autor y Libro).

repository: Interfaces que extienden de JpaRepository para acceder a la BD.

service: Lógica de negocio.

ClienteGutendex: Clase que realiza la solicitud HTTP a Gutendex.

LiteraluraService: Orquesta la búsqueda de libros, parsing, registro en la BD, etc.

principal: Menú de consola que interactúa con el usuario y llama a los métodos de la capa de servicio.

LiteraluraApplication: Clase principal con @SpringBootApplication. Implementa CommandLineRunner (o llama a la clase Principal) para lanzar el menú en la consola.

## Cómo ejecutar el proyecto

Clonar o descargar este repositorio.

Importar en tu IDE como proyecto Maven.

Configurar en application.properties la conexión a tu base de datos PostgreSQL.

Asegurarte de tener en el pom.xml las dependencias de:

org.springframework.boot:spring-boot-starter-data-jpa

org.postgresql:postgresql

com.fasterxml.jackson.core:jackson-databind

Ejecutar la clase LiteraluraApplication (o usar mvn spring-boot:run).

En la consola aparecerá un menú. Ingresa un número para escoger la opción deseada (1–5), o 0 para salir.
Uso de la aplicación

### 1. Buscar libro por título

Ingresa la opción 1.

Escribe el título o palabra clave (por ejemplo, “pride”).

La aplicación consumirá la API de Gutendex y, si encuentra resultados, tomará el primer libro y lo guardará en la BD (solo si no existe).

### 2. Listar libros registrados

Muestra todos los libros de la tabla libros, junto con su autor, idioma y número de descargas.

### 3. Listar autores registrados

Muestra todos los autores en la tabla autores, junto con sus años de nacimiento y fallecimiento.

### 4. Listar autores vivos en un determinado año

Solicita al usuario un año (por ejemplo, 1600) y muestra los autores que estaban vivos en esa fecha, basándose en su anio_nacimiento y anio_fallecimiento.

### 5. Listar libros por idioma

Pide al usuario un idioma (es, en, fr, pt, etc.) y muestra todos los libros que coincidan (ignora mayúsculas/minúsculas).

### 0. Salir

Finaliza la aplicación.

## Manejo de duplicados

Libro: Se verifica si el título ya existe (ignora mayúsculas) antes de guardar. De esa forma, no se puede insertar el mismo libro dos veces.

Autor: Se busca por nombre/apellido antes de crearlo. Si ya existe, se reutiliza la misma entidad.
Notas adicionales

Para evitar que aparezcan trazas de SQL en consola, se configuran las propiedades spring.jpa.show-sql=false y spring.jpa.properties.hibernate.format_sql=false.

Si deseas verlas, cámbialas a true.

## Licencia

Este proyecto se distribuye con fines educativos. Puedes adaptarlo o usarlo como referencia para tus propios proyectos.

