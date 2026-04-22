package br.com.JairPassarela.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliaco;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodios) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodios.titulo();
        this.numeroEpisodio = dadosEpisodios.numero();
        try{
            this.avaliaco = Double.valueOf(dadosEpisodios.avaliaco());
        }catch (NumberFormatException ex){
            this.avaliaco = 0.0;
        }
        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodios.dataLancamento());
        }catch (DateTimeException ex){
            this.dataLancamento = null;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumero() {
        return numeroEpisodio;
    }

    public void setNumero(Integer numero) {
        this.numeroEpisodio = numero;
    }

    public Double getAvaliaco() {
        return avaliaco;
    }

    public void setAvaliaco(Double avaliaco) {
        this.avaliaco = avaliaco;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliaco=" + avaliaco +
                ", dataLancamento=" + dataLancamento ;

    }
}
