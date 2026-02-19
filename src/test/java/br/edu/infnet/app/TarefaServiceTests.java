package br.edu.infnet.app;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.edu.infnet.app.PbTp3Application;
import br.edu.infnet.app.model.Categoria;
import br.edu.infnet.app.model.Tarefa;
import br.edu.infnet.app.service.TarefaService;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class TarefaServiceTests {
	@Autowired
	TarefaService service;
	
	@Test
	void ConsultarTarefasTest() {
		assertEquals(4, service.consultarTarefas().size());
	}
	
	@Test
	void consultarTarefaPorIdValidoTest() {
		Optional<Tarefa> resultado = service.consultarTarefa(1);
		assertTrue(resultado.isPresent());
		assertEquals(new Tarefa(1, "Tarefa 1", Categoria.TRABALHO, true), resultado.get());
	}
	
	@Test
	void consultarTarefaPorIdInvalidoTest() {
		assertTrue(service.consultarTarefa(10).isEmpty());
	}
	
	@Test
	void inserirTarefaTest() {
		service.incluirTarefa("Tarefa 5", Categoria.PESSOAL);
		assertEquals(new Tarefa(5, "Tarefa 5", Categoria.PESSOAL, false), service.consultarTarefa(5).get());
	}
	
	@Test
	void alterarTarefaTest() throws Exception {
		assertDoesNotThrow(() -> service.alterarTarefa(1, "Tarefa 1 Alterada", Categoria.PESSOAL, false));
		assertEquals(new Tarefa(1, "Tarefa 1 Alterada", Categoria.PESSOAL, false), service.consultarTarefa(1).get());
	}
	
	@Test
	void excluirTarefaTestExistente() {
		assertDoesNotThrow(() -> service.excluirTarefa(3));
		assertTrue(service.consultarTarefa(3).isEmpty());
	}
}
