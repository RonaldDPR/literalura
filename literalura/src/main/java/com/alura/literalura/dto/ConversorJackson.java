package com.alura.literalura.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorJackson {
    private final ObjectMapper mapper = new ObjectMapper();

    public ResultadoBusqueda convertir(String json) {
        try {
            return mapper.readValue(json, ResultadoBusqueda.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al parsear JSON", e);
        }
    }
}
