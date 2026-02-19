package br.edu.infnet.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.client.RestTestClient;

import br.edu.infnet.app.dto.TarefaDto;
import br.edu.infnet.app.model.TarefaTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TarefaControllerTests {
	@LocalServerPort
	int port;

	@Autowired
	RestTestClient restTestClient;

	@Test
	void getIndexPageTest() {
		restTestClient.get().uri("http://localhost:%d/".formatted(port)).exchange().expectStatus().isOk();
	}

	@Test
	void getTarefasTest() {
		restTestClient.get().uri("http://localhost:%d/tarefas".formatted(port)).exchange().expectBody().json("" + "["
				+ "{\"Id\": 1, \"descricao\": \"Tarefa 1\", \"categoria\": \"TRABALHO\", \"concluido\": true},"
				+ "{\"Id\": 2, \"descricao\": \"Tarefa 2\", \"categoria\": \"DOMESTICA\", \"concluido\": true},"
				+ "{\"Id\": 3, \"descricao\": \"Tarefa 3\", \"categoria\": \"PESSOAL\", \"concluido\": false},"
				+ "{\"Id\": 4, \"descricao\": \"Tarefa 4\", \"categoria\": \"PROGRAMACAO\", \"concluido\": false}"
				+ "]");
	}

	@Test
	void getTarefaComIdValidoExistenteTest() {
		int id = 1;
		restTestClient.get().uri("http://localhost:%d/tarefa/%d".formatted(port, id)).exchange().expectBody()
				.json("{\"Id\": 1, \"descricao\": \"Tarefa 1\", \"categoria\": \"TRABALHO\", \"concluido\": true}");
	}

	@Test
	void getTarefaComIdValidoInexistenteTest() {
		int id = 10;
		restTestClient.get().uri("http://localhost:%d/tarefa/%d".formatted(port, id)).exchange().expectStatus().isOk()
				.expectBody(String.class).consumeWith(resultado -> {
					assertEquals(String.format("Não há tarefa com id %d", id), resultado.getResponseBody());
				});
	}

	@Test
	void getTarefaComIdInvalidoTest() {
		List<Object> idsList = new ArrayList<>();
		idsList.add(null);
		idsList.add("abc");

		idsList.stream().forEach(id -> {
			restTestClient.get().uri("http://localhost:%d/tarefa/%s".formatted(port, id)).exchange().expectStatus()
					.isBadRequest().expectBody(String.class)
					.isEqualTo("ID inválido, o id deve ser um inteiro positivo.");
		});
	}

	@Test
	void editarTarefaComSucessoTest() {
		int id = 1;
		TarefaTest tarefa = new TarefaTest();
		tarefa.Id = id;
		tarefa.descricao = "Tarefa 1 editada";
		tarefa.categoria = "PESSOAL";
		tarefa.concluido = false;
		restTestClient.put().uri("http://localhost:%d/tarefas/editar".formatted(port)).body(tarefa).exchange()
				.expectStatus().isOk();

		restTestClient.get().uri("http://localhost:%d/tarefa/%d".formatted(port, id)).exchange().expectBody().json(
				"{\"Id\": 1, \"descricao\": \"Tarefa 1 editada\", \"categoria\": \"PESSOAL\", \"concluido\": false}");
	}

	@Test
	void editarTarefaSemCategoriaValidaTest() {
		int id = 1;
		int length = 101;
		char charToFill = 'b';

		// Using String.join and Collections.nCopies
		String string101 = String.join("", Collections.nCopies(length, String.valueOf(charToFill)));
		TarefaTest tarefa = new TarefaTest();
		tarefa.Id = id;
		tarefa.descricao = string101;
		tarefa.categoria = "PESSOsd";
		tarefa.concluido = false;
		restTestClient.put().uri("http://localhost:%d/tarefas/editar".formatted(port)).body(tarefa).exchange()
				.expectStatus().isBadRequest().expectBody()
				.json("{ \"categoria\": \"O valor da categoria está inválido, tente esses valores [TRABALHO, DOMESTICA, PROGRAMACAO, PESSOAL]\"}");
	}

	@Test
	void editarTarefaSemDescricaoValidaTest() {
		int id = 1;
		int length = 101;
		char charToFill = 'b';

		// Using String.join and Collections.nCopies
		String string101 = String.join("", Collections.nCopies(length, String.valueOf(charToFill)));
		TarefaTest tarefa = new TarefaTest();
		tarefa.Id = id;
		tarefa.descricao = string101;
		tarefa.categoria = "PESSOAL";
		tarefa.concluido = false;
		restTestClient.put().uri("http://localhost:%d/tarefas/editar".formatted(port)).body(tarefa).exchange()
				.expectStatus().isBadRequest().expectBody()
				.json("{ \"descricao\": \"A descrição deve ter no máximo 100 caracteres.\" }");
	}

	@Test
	void excluirTarefaComSucessoTest() {
		int id = 1;
		restTestClient.method(HttpMethod.DELETE).uri("http://localhost:%d/tarefas/excluir".formatted(port)).body(id)
				.exchange().expectStatus().isOk();

		restTestClient.get().uri("http://localhost:%d/tarefa/%d".formatted(port, id)).exchange()
				.expectBody(String.class).isEqualTo(String.format("Não há tarefa com id %d", id));

	}
}