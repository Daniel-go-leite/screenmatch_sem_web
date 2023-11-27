package br.com.daniel.screenmatch_serie.repository;

import br.com.daniel.screenmatch_serie.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie,Long> {
}
