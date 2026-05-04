package br.com.JairPassarela.controller;

import br.com.JairPassarela.model.DadosSerie;
import br.com.JairPassarela.model.Filme;
import br.com.JairPassarela.repository.FilmeRepository;
import br.com.JairPassarela.service.ConsumoAPI;
import br.com.JairPassarela.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmes")
@CrossOrigin(origins = "*")
public class FilmeController {
    @Autowired
    private FilmeRepository repositorio;

    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    @GetMapping
    public List<Filme> obterFilmes(){
        return repositorio.findAll();
    }
    @GetMapping("/{id}")
    public Filme obterFilmePorId(@PathVariable long id){
        return repositorio.findById(id).orElse(null);
    }
    @GetMapping("/top5")
    public List<Filme> obterTop5(){
        return repositorio.findTop5ByOrderByAvaliacaoDesc();
    }

    @GetMapping("/busca")
    public List<Filme> buscarPorTitulo(@RequestParam String titulo){
        return repositorio.findAllByTituloContainingIgnoreCase(titulo);
    }

    @PostMapping("/cadastrar")
    public Filme cadastrarFilme(@RequestParam String nome){
        String json = consumo.obterDados(
                "https://www.omdbapi.com/?t="
                        + nome.replace(" ", "+")
                        + "&apikey=5b69fc12"
        );
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        Filme filme = new Filme(dados);
        repositorio.save(filme);
        return filme;
    }
}
