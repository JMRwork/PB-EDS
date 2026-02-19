package br.edu.infnet.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.infnet.app.model.Categoria;
import br.edu.infnet.app.model.Tarefa;
import br.edu.infnet.app.repository.TarefaRepository;

@Service
public class TarefaService {
	private TarefaRepository tarefaRepository;

	public TarefaService(TarefaRepository tarefaRepository) {
		this.tarefaRepository = tarefaRepository;
	}

	public List<Tarefa> consultarTarefas() {
		return tarefaRepository.findAll();
	}

	public Optional<Tarefa> consultarTarefa(int id) {
		return tarefaRepository.findById(id);
	}

	public void incluirTarefa(String descricao, Categoria categoria) {
		Tarefa tarefa = new Tarefa(null, descricao, categoria, false);
		tarefaRepository.save(tarefa);
	}

	public void alterarTarefa(int id, String descricao, Categoria categoria, boolean concluido) throws Exception{
		try {
			Optional<Tarefa> tarefa = consultarTarefa(id);
			if (tarefa.isPresent()) {
				Tarefa tarefaAlvo = tarefa.get();
				tarefaAlvo.setDescricao(descricao);
				tarefaAlvo.setCategoria(categoria);
				tarefaAlvo.setConcluido(concluido);
				tarefaRepository.save(tarefaAlvo);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void excluirTarefa(int id) {
		tarefaRepository.deleteById(id);
	}
}
