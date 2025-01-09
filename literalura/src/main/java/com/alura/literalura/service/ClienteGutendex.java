package com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteGutendex {

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    public String buscarLibroPorTitulo(String titulo) {
        try {
            // Reemplaza espacios con + para la URL
            String tituloUrl = titulo.trim().replace(" ", "+");
            String urlCompleta = URL_BASE + tituloUrl;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlCompleta))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al consumir la API Gutendex", e);
        }
    }
}
