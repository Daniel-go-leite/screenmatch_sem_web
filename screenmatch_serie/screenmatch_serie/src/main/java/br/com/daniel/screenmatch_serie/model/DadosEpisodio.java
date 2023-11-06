package br.com.daniel.screenmatch_serie.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String tituloi,
                           @JsonAlias("Episode") Integer numero,
                           @JsonAlias("imdbRating") String avaliacao,
                           @JsonAlias("Episode") String dataLancamento) {
}
