package com.alura.literalura.service;

import com.alura.literalura.dto.AutorJson;
import com.alura.literalura.dto.ConversorJackson;
import com.alura.literalura.dto.LibroJson;
import com.alura.literalura.dto.ResultadoBusqueda;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiteraluraService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

    private final ClienteGutendex clienteGutendex = new ClienteGutendex();
    private final ConversorJackson conversor = new ConversorJackson();

    @Transactional
    public Libro buscarYRegistrarLibroPorTitulo(String tituloBuscado) {
        // 1) Consumir API
        String json = clienteGutendex.buscarLibroPorTitulo(tituloBuscado);

        if (json == null || json.isBlank()) {
            System.out.println("No se encontró el libro en la API (respuesta vacía)");
            return null;  // O maneja el caso adecuadamente
        }

        // 2) Convertir JSON a objeto
        ResultadoBusqueda resultado = conversor.convertir(json);

        if (resultado.getResults() == null || resultado.getResults().isEmpty()) {
            // No se encontró nada
            System.out.println("El libro no fue encontrado.");
            return null;
        }

        // Tomar el primer resultado de la lista
        LibroJson primerLibroJson = resultado.getResults().get(0);

        // Verificar si ya existe en la base de datos
        Libro libroExistente = libroRepository.findByTituloIgnoreCase(primerLibroJson.getTitle());
        if (libroExistente != null) {
            // Ya existe
            return null; // o lanzar excepción, según tu preferencia
        }

        // Tomar el primer autor
        Autor autor = null;
        if (!primerLibroJson.getAuthors().isEmpty()) {
            AutorJson autorJson = primerLibroJson.getAuthors().get(0);
            // Partir "Austen, Jane" en [apellido, nombre]
            String[] partes = autorJson.getName().split(",", 2);
            String apellido = partes[0].trim();
            String nombre = (partes.length > 1) ? partes[1].trim() : "";

            //autor = new Autor(nombre, apellido, autorJson.getBirthYear(), autorJson.getDeathYear());
            // Podríamos verificar si ya existe un autor con ese mismo nombre/apellido
            // y reusarlo. Ejemplo:
            // Autor autorExistente = autorRepository.findByNombreAndApellido(...);
            var autorExistente = autorRepository.findByNombreIgnoreCaseAndApellidoIgnoreCase(nombre, apellido);

            if (autorExistente.isPresent()) {
                // Reutilizar autor ya guardado
                autor = autorExistente.get();
            } else {
                // Crear un nuevo autor si no existe
                autor = new Autor(nombre, apellido, autorJson.getBirthYear(), autorJson.getDeathYear());
                autorRepository.save(autor);
            }

        }

        String idioma = "Desconocido";
        if (primerLibroJson.getLanguages() != null && !primerLibroJson.getLanguages().isEmpty()) {
            idioma = primerLibroJson.getLanguages().get(0);
        }


        // 3) Construir objeto Libro
        Libro nuevoLibro = new Libro(
                primerLibroJson.getTitle(),
                idioma,
                primerLibroJson.getDownloadCount(),
                autor
        );

        // 4) Guardar en la base de datos
        libroRepository.save(nuevoLibro);

        return nuevoLibro;
    }

    public void listarLibros() {
        var libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }
        for (Libro l : libros) {
            System.out.println("---------LIBRO----------");
            System.out.println("Título: " + l.getTitulo());
            System.out.println("Autor: " + (l.getAutor() != null
                    ? (l.getAutor().getApellido() + ", " + l.getAutor().getNombre())
                    : "Desconocido"));
            System.out.println("Idioma: " + l.getIdioma());
            System.out.println("Número de descargas: " + l.getNumeroDescargas());
        }
    }
    @Transactional(readOnly = true)

    public void listarAutores() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }
        for (Autor a : autores) {
            System.out.println("--------------------------------");
            System.out.println("Autor: " + a.getApellido() + ", " + a.getNombre());
            System.out.println("Fecha de nacimiento: " + a.getAnioNacimiento());
            System.out.println("Fecha de fallecimiento: " + a.getAnioFallecimiento());

            if (a.getLibros() == null || a.getLibros().isEmpty()) {
                System.out.println("Libros: [Ninguno]");
            } else {
                // Convertir la lista de objetos Libro a una sola cadena
                String listaLibros = a.getLibros().stream()
                        .map(Libro::getTitulo)
                        .reduce((acum, titulo) -> acum + ", " + titulo)
                        .orElse("");
                System.out.println("Libros: [" + listaLibros + "]");
            }
        }
    }

    public void listarAutoresVivosEnAnio(int anio) {
        var autores = autorRepository.findAutoresVivosEnAnio(anio);
        // Ajustar la query para la lógica “vivo en año X” si es necesario
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
            return;
        }
        for (Autor a : autores) {
            System.out.println("--------------------------------");
            System.out.println("Autor vivo en " + anio + ": " + a.getApellido() + ", " + a.getNombre());
        }
    }

    public void listarLibrosPorIdioma(String idiomaBuscado) {
        var libros = libroRepository.findByIdiomaIgnoreCase(idiomaBuscado);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idiomaBuscado);
            return;
        }
        for (Libro l : libros) {
            System.out.println("-------------LIBRO---------------");
            System.out.println("Título: " + l.getTitulo());
            System.out.println("Autor: " + (l.getAutor() != null
                    ? (l.getAutor().getApellido() + ", " + l.getAutor().getNombre())
                    : "Desconocido"));
            System.out.println("Idioma: " + l.getIdioma());
            System.out.println("Número de descargas: " + l.getNumeroDescargas());
        }
    }
}