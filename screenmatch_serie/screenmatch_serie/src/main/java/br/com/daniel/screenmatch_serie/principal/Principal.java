package br.com.daniel.screenmatch_serie.principal;

import br.com.daniel.screenmatch_serie.model.*;
import br.com.daniel.screenmatch_serie.repository.SerieRepository;
import br.com.daniel.screenmatch_serie.service.ConsumoApi;
import br.com.daniel.screenmatch_serie.service.ConverteDados;


import java.util.*;
import java.util.stream.Collectors;


public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO ="https://www.omdbapi.com/?t=" ;
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSerie = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie>series = new ArrayList<>();
    private Optional<Serie>serieBusca;



    public Principal(SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu(){
       var opcao = -1;
       while (opcao !=0) {
           var Menu = """
                   1 - Buscar séries
                   2 - Buscar episódios
                   3 - Lista séries buscadas
                   4 - Busacar série por título
                   5 - Bysacar série por ator
                   6 - Top 5 série
                   7 - Buscar séries por categoria
                   8 - Numero maximo de temporada
                   9 - Buscar episódios por trecho
                   10 - Top 5 episódios por série
                   11 - Buscar episódios a partir de uma data
                                   
                   0 - sair
                                   
                   """;

           System.out.println(Menu);
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
                   buscarSeriesPorTitulo();
                   break;
               case 5:
                   buscarSeriesPorAtor();
                   break;
               case 6:
                   buscarTop5Series();
                   break;
               case 7:
                   buscarSeriesPorCategoria();
                   break;
               case 8:
                   buscarSeriesPorNumeDetemporada();
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



    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
       // dadosSerie.add(dados);
             repository.save(serie);
        System.out.println(dados);

        }

        private DadosSerie getDadosSerie() {
            System.out.println("Digite o nome da série para a busca ");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")
            + API_KEY);
            DadosSerie dados = conversor.obeterDados(json, DadosSerie.class);
            return dados;

        }

        private void  buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha um série pelo nome: ");
        var nomeSerie = leitura.nextLine();

                Optional<Serie>serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

                if (serie.isPresent()) {

                    var serieEncontrada = serie.get();
                    List<DadosTemporadas> temporadas = new ArrayList<>();

                    for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                        var json = consumo.obterDados(ENDERECO + serieEncontrada
                                .getTitulo().replace(" ",
                                "+") + "&season=" + i + API_KEY);
                        DadosTemporadas dadosTemporadas = conversor.obeterDados(json, DadosTemporadas.class);
                        temporadas.add(dadosTemporadas);

                    }
                      temporadas.forEach(System.out::println);

                    List<Episodio> episodios = temporadas.stream()
                            .flatMap(d-> d.episodios().stream()
                                    .map(e-> new Episodio(d.numero(),e)))
                            .collect(Collectors.toList());
                    serieEncontrada.setEpisodios(episodios);
                    repository.save(serieEncontrada);

                }else {

                    System.out.println("Série não encontrada");
                }

    }

    private void listarSeriesBuscadas(){
              series = repository.findAll();
              series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escolha um série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da série: " + serieBuscada.get());

        }else {
                System.out.println("Série não encontrada! ");
        }

    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeDoAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de que valor");
        var avaliacao = leitura.nextDouble();
        List<Serie>serieEncontradas = repository
                .findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeDoAtor , avaliacao);

        System.out.println("Séries em que " + nomeDoAtor + "Trabalhou: ");

        serieEncontradas.forEach(s->
                System.out.println( s.getTitulo() + "avaliação: "  +  s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie>serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s->
                System.out.println( s.getTitulo() + "avaliação: "  +  s.getAvaliacao())  );
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Série da categoria");
        seriesPorCategoria.forEach(System.out::println);

    }

    private void buscarSeriesPorNumeDetemporada() {
        System.out.println("Digite o numero de temporas que deseja: ");
       var totalTemporadas = leitura.nextInt();
       leitura.nextLine();
       System.out.println("com avaliação a partir de que valor? ");
       var avaliacao = leitura.nextDouble();
       leitura.nextLine();
       List<Serie>filtroSerie = repository.seriesPorTemporadaEAVliacao
               (totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
       filtroSerie.forEach(s->
               System.out.println(s.getTitulo() + " -avaliação: " + s.getAvaliacao()));


    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do  episodio para busca?: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio>episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(),
                        e.getTemporada(),
                        e.getNumeroDeEpisodio(),
                        e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriesPorTitulo();
      if(serieBusca.isPresent()){
          Serie serie = serieBusca.get();
          List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
          topEpisodios.forEach(e->
                  System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                          e.getSerie().getTitulo(), e.getTemporada(),
                          e.getNumeroDeEpisodio(), e.getTitulo(), e.getAvaliacao()));
      }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriesPorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio>episodiosAnos = repository.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAnos.forEach(System.out::println);

        }



    }



}
