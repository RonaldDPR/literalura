package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("""
       SELECT a
       FROM Autor a
       WHERE a.anioNacimiento <= :anio
         AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)
       """)
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    Optional<Autor> findByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);

}
