package br.com.JairPassarela.controller;

import br.com.JairPassarela.model.DadosSerie;
import br.com.JairPassarela.model.DadosTemporada;
import br.com.JairPassarela.model.Episodio;
import br.com.JairPassarela.model.Serie;
import br.com.JairPassarela.repository.SerieRepository;
import br.com.JairPassarela.service.ConsumoAPI;
import br.com.JairPassarela.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "*")
public class SerieController {

    @Autowired
    private SerieRepository repositorio;

    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    @GetMapping
    public List<Serie> obterSeries() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public Serie obterSeriePorId(@PathVariable Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @GetMapping("/{id}/episodios")
    public List<Episodio> obterEpisodios(@PathVariable Long id) {
        Serie serie = repositorio.findById(id).orElse(null);
        if (serie != null) {
            return serie.getEpisodios();
        }
        return List.of();
    }

    @GetMapping("/top5")
    public List<Serie> obterTop5() {
        return repositorio.findTop5ByOrderByAvaliacaoDesc();
    }

    @GetMapping("/busca")
    public List<Serie> buscarPorTitulo(@RequestParam String titulo) {
        return repositorio.findAllByTituloContainingIgnoreCase(titulo);
    }

    @PostMapping("/cadastrar")
    public Serie cadastrarSerie(@RequestParam String nome) {
        // 1. Busca os dados da série na OMDB
        String json = consumo.obterDados(
                "https://www.omdbapi.com/?t="
                        + nome.replace(" ", "+")
                        + "&apikey=5b69fc12"
        );
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        Serie serie = new Serie(dados);
        repositorio.save(serie);

        // 2. Busca episódios de todas as temporadas
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
            String jsonTemporada = consumo.obterDados(
                    "https://www.omdbapi.com/?t="
                            + nome.replace(" ", "+")
                            + "&season=" + i
                            + "&apikey=5b69fc12"
            );
            DadosTemporada dadosTemporada = conversor.obterDados(jsonTemporada, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        // 3. Converte e salva os episódios
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodio().stream()
                        .map(e -> new Episodio(t.numero(), e)))
                .collect(Collectors.toList());

        serie.setEpisodios(episodios);
        repositorio.save(serie);

        return serie;
    }
}