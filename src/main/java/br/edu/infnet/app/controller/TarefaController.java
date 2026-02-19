package br.edu.infnet.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.GsonEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.infnet.app.dto.AdicionarTarefaDto;
import br.edu.infnet.app.dto.TarefaDto;
import br.edu.infnet.app.model.Tarefa;
import br.edu.infnet.app.service.TarefaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.websocket.server.PathParam;

@Controller
@Validated
public class TarefaController {
	TarefaService service;

	public TarefaController(TarefaService service) {
		this.service = service;
	}

	@GetMapping()
	public String getIndexPage() {
		return "index.html";
	}

	@GetMapping("/tarefas")
	@ResponseBody
	public List<Tarefa> getTarefas() {
		return service.consultarTarefas();
	}

	@GetMapping("tarefa/{id}")
	@ResponseBody
	public ResponseEntity getTarefa(
			@PathVariable("id") @Positive(message = "ID inválido, deve ser número inteiro positivo.") @Validated Integer id) {
		try {
			Optional<Tarefa> tarefa = service.consultarTarefa(id);
			if (tarefa.isPresent()) {
				return ResponseEntity.ok(tarefa.get());
			}
			return ResponseEntity.ok().body(String.format("Não há tarefa com id %d", id));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PostMapping("tarefas/adicionar")
	@ResponseBody
	public ResponseEntity adicionarTarefa(@ModelAttribute @Valid AdicionarTarefaDto novaTarefa) {
		try {
			service.incluirTarefa(novaTarefa.getDescricao(), novaTarefa.getCategoria());
			return ResponseEntity.created(null).build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PutMapping("tarefas/editar")
	public ResponseEntity atualizaTarefa(@RequestBody @Valid TarefaDto tarefaEditada) {
		try {

			service.alterarTarefa(tarefaEditada.getId(), tarefaEditada.getDescricao(), tarefaEditada.getCategoria(),
					tarefaEditada.isConcluido());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Não foi possível editar a tarefa. " + e.getMessage());
		}
	}

	@DeleteMapping("tarefas/excluir")
	public ResponseEntity excluirTarefa(@RequestBody @Valid @Min(value = 1) Integer id) {
		Optional<Tarefa> tarefa = service.consultarTarefa(id);
		tarefa.ifPresentOrElse(value -> {
			service.excluirTarefa(id);
		}, () -> {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Não há tarefa com id $d", id), null);
		});
		return ResponseEntity.ok().build();
	}
}
