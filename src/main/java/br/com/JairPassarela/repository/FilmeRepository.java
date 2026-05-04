package br.com.JairPassarela.repository;
import br.com.JairPassarela.Categoria;
import br.com.JairPassarela.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findAllByTituloContainingIgnoreCase(String titulo);
    List<Filme> findByGenero(Categoria categoria);
    List<Filme> findTop5ByOrderByAvaliacaoDesc();
}