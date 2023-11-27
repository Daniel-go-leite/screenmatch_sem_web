package br.com.daniel.screenmatch_serie;


import br.com.daniel.screenmatch_serie.principal.Principal;

import br.com.daniel.screenmatch_serie.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchSerieApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSerieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(repositorio);
		principal.exibeMenu();


	}

}