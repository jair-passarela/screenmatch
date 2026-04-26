package br.com.JairPassarela.Principal;

import br.com.JairPassarela.Categoria;
import br.com.JairPassarela.model.DadosSerie;
import br.com.JairPassarela.model.DadosTemporada;
import br.com.JairPassarela.model.Episodio;
import br.com.JairPassarela.model.Serie;
import br.com.JairPassarela.repository.SerieRepository;
import br.com.JairPassarela.service.ConsumoAPI;
import br.com.JairPassarela.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=5b69fc12";

    private List<DadosSerie> dadosSeries = new ArrayList<>();


    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0 ) {
            var menu = """
                    1 - Buscar série
                    2 - Buscar Episódio
                    3 - Listar séries buscadas
                    4 - Buscar série por tíulo
                    5 - Buscar série por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar série
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                    0 - Sair
                    
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();


            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    bucarSeriePorCategoria();
                    break;
                case 8:
                    filtrarSeriePorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção invalida");
            }
        }
    }


    private void buscarSerieWeb(){
            DadosSerie dados = getDadosSerie();
            Serie serie = new Serie(dados);
            repositorio.save(serie);
            System.out.println(dados);
        }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para buscar");
        var nomeSerie = leitura.nextLine();

        var json = consumo.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY
        );

        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série: ");
        var nomeSerie  = leitura.nextLine();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(
                        ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +
                                "&season=" + i + API_KEY
                );

                DadosTemporada dadosTemporada =
                        conversor.obterDados(json, DadosTemporada.class);

                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodio().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }else {
            System.out.println("Série não encontrada");
        }


    }

    private void listarSeriesBuscadas(){
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série: ");
        var nomeSerie  = leitura.nextLine();
        serieBusca= repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        }else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor(){
        System.out.println("Qual o nome para a busca");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor?");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();

        List<Serie> serieEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou:");
        serieEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void buscarTop5Series(){
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void bucarSeriePorCategoria(){
        System.out.println("Dejesa buscar série de que categoria/gênero?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriePorTemporadaEAvaliacao() {
        System.out.println("Filtrar série até quantas temporadas? ");
        int totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor?");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas,avaliacao);
        System.out.println("*** Série filtradas ***");
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + " - avaliação : " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episodio para busca");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodioEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodioEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s \n",e.getSerie(),e.getTitulo(),e.getTemporada(),
                e.getNumeroEpisodio(),e.getTitulo()));
    }


    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliaco() ));
        }
    }
    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodioPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }









}
