package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadoBusqueda {
    private List<LibroJson> results;

    public List<LibroJson> getResults() {
        return results;
    }

    public void setResults(List<LibroJson> results) {
        this.results = results;
    }
}
