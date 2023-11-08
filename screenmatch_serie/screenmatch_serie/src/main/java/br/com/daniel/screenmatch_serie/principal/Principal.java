package br.com.daniel.screenmatch_serie.principal;

import br.com.daniel.screenmatch_serie.model.DadosSerie;
import br.com.daniel.screenmatch_serie.model.DadosTemporadas;
import br.com.daniel.screenmatch_serie.service.ConsumoApi;
import br.com.daniel.screenmatch_serie.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO ="https://www.omdbapi.com/?t=" ;

    private final String API_KEY = "&apikey=6585022c";



    public void exibeMenu(){
        System.out.println("Digite o nome da série para a busca ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(
        ENDERECO + nomeSerie.replace(" ","+") + API_KEY);

        DadosSerie dados = conversor.obeterDados(json,DadosSerie.class);

       System.out.println(dados);

       	List<DadosTemporadas> temporadas = new ArrayList<>();

		for(int i = 1; i<=dados.totalTemporadas(); i++) {
			json = consumo.obterDados (ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY );
			DadosTemporadas dadosTemporada = conversor.obeterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporada);

		}
		temporadas.forEach(System.out::println);



    }
}
