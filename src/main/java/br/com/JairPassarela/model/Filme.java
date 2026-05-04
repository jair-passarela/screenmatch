package br.com.JairPassarela.model;

import br.com.JairPassarela.Categoria;
import br.com.JairPassarela.service.TradutorService;
import jakarta.persistence.*;

@Entity
@Table(name = "filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String poster;
    private String sinopse;
    private Double avaliacao;
    private String atores;
    private Integer ano;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    public Filme() {}

    public Filme(DadosSerie dados) {
        this.titulo = dados.titulo();
        this.poster = dados.poster();

        try {
            this.sinopse = TradutorService.obterTraducao(dados.sinopse()).trim();
        } catch (Exception e) {
            this.sinopse = dados.sinopse();
        }

        this.atores = dados.atores();

        try {
            this.avaliacao = Double.valueOf(dados.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        try {
            if (dados.genero() != null && !dados.genero().isEmpty()) {
                this.genero = Categoria.fromString(dados.genero().split(",")[0].trim());
            } else {
                this.genero = Categoria.ACAO;
            }
        } catch (IllegalArgumentException e) {
            this.genero = Categoria.ACAO;
        }
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getPoster() { return poster; }
    public String getSinopse() { return sinopse; }
    public Double getAvaliacao() { return avaliacao; }
    public String getAtores() { return atores; }
    public Integer getAno() { return ano; }
    public Categoria getGenero() { return genero; }
}