package br.com.daniel.screenmatch_serie.repository;

import br.com.daniel.screenmatch_serie.model.Categoria;
import br.com.daniel.screenmatch_serie.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {


    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeDoAtor,
                                                                             Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);


    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual
            (int totalTemporadas, double avaliacao);
}
