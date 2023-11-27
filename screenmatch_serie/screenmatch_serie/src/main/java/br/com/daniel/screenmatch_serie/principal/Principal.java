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

                Optional<Serie>serie = series.stream()
                .filter(s-> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

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
}
