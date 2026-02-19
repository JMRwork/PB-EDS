package br.edu.infnet.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.app.model.Categoria;
import br.edu.infnet.app.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer>   {
}
