package com.alura.literalura.principal;

import com.alura.literalura.model.Libro;
import com.alura.literalura.service.LiteraluraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;


@EnableTransactionManagement

@Component
public class Principal {

    @Autowired
    private LiteraluraService servicio;

    public void iniciarAplicacion() {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                    ---------------------------------
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - salir
                    ---------------------------------
                    """);
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.\n");
                pausarEjecucion(sc);
                continue;
            }

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ingrese el nombre del libro que desea buscar: ");
                    String titulo = sc.nextLine();
                    Libro libro = servicio.buscarYRegistrarLibroPorTitulo(titulo);
                    if (libro == null) {
                        // Significa libro   no encontrado o ya existente

                        System.out.println("No se puede registrar el libro. " +
                                "Puede que no exista en la API o ya esté en la base de datos.\n");
                    } else {
                        System.out.println("Libro registrado con éxito:");
                        System.out.println("Título: " + libro.getTitulo());
                        System.out.println("Autor: " + libro.getAutor().getApellido() + ", " + libro.getAutor().getNombre());
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                    }
                    pausarEjecucion(sc);
                }
                case 2 -> {
                    servicio.listarLibros();
                    pausarEjecucion(sc);
                }
                case 3 -> {
                    servicio.listarAutores();
                    pausarEjecucion(sc);
                }
                case 4 -> {
                    // Repetir hasta que sea un valor numérico válido
                    boolean anioValido = false;
                    while (!anioValido) {
                        System.out.print("Ingrese el año para verificar autores vivos en ese año: ");
                        String ingreso = sc.nextLine();
                        try {
                            int anio = Integer.parseInt(ingreso);
                            servicio.listarAutoresVivosEnAnio(anio);
                            anioValido = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Debe ingresar un valor numérico.\n");
                            pausarEjecucion(sc);
                            // no salimos del while, así que vuelve a pedir el año
                        }
                    }
                    pausarEjecucion(sc);
                }
                case 5 -> {
                    // Mostrar menú de idiomas:
                    System.out.println("""
                            Ingrese el idioma (es, en, fr, pt) para buscar los libros:
                            es - español
                            en - inglés
                            fr - francés
                            pt - portugués
                            """);
                    String idioma = sc.nextLine().trim();
                    servicio.listarLibrosPorIdioma(idioma);
                    pausarEjecucion(sc);
                }
                case 0 -> System.out.println("Saliendo de la aplicación...");
                default -> {
                    System.out.println("Opción inválida.\n");
                    pausarEjecucion(sc);
                }
            }
        }
        sc.close();
    }

    private void pausarEjecucion(Scanner sc) {
        System.out.println("\nPresione ENTER para continuar...");
        sc.nextLine(); // Espera a que el usuario presione ENTER
    }
}
