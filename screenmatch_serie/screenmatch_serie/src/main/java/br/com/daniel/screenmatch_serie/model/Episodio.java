package br.com.daniel.screenmatch_serie.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer NumeroDeEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    @ManyToOne
    private Serie serie;

    public Episodio(){

    }

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio ){
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.NumeroDeEpisodio = dadosEpisodio.numero();


        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.dataLancamento = null;
        }


    }


    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada){
            this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDeEpisodio() {
        return NumeroDeEpisodio;
    }

    public void setNumeroDeEpisodio(Integer numeroDeEpisodio) {
        NumeroDeEpisodio = numeroDeEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataDelancamento() {
        return dataLancamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public void setDataDelancamento(LocalDate dataDelancamento) {
        this.dataLancamento = dataDelancamento;
    }


    @Override
    public String toString() {
        return "Episodio{" +
                "temporada=" + temporada +
                ", titulo='" + titulo + '\''+
                ", NumeroDeEpisodio=" + NumeroDeEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataDelancamento=" + dataLancamento +
                '}';
    }

}
