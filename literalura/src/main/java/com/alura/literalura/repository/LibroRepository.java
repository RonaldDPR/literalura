package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // 1) Buscar por título (ignora mayúsculas/minúsculas).
    Libro findByTituloIgnoreCase(String titulo);

    // 2) Listar por idioma (ignora mayúsculas también).
    List<Libro> findByIdiomaIgnoreCase(String idioma);
}
