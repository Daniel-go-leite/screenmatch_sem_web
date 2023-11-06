package br.com.daniel.screenmatch_serie;

import br.com.daniel.screenmatch_serie.model.DadosEpisodio;
import br.com.daniel.screenmatch_serie.model.DadosSerie;
import br.com.daniel.screenmatch_serie.service.ConsumoApi;
import br.com.daniel.screenmatch_serie.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSerieApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSerieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi =new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obeterDados(json,DadosSerie.class);
		System.out.println(dados);
		json = consumoApi.obterDados("https://omdbapi.com/?t=gilmoegirls&season=1&episode=2apikey=6585022c");
		DadosEpisodio dadosEpisodio = conversor.obeterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);
	}
}
