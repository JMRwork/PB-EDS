package br.edu.infnet.app.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.app.model.Categoria;

public class TarefaDtoTests {
	TarefaDto novaTarefaDto;
	
	@BeforeEach
	void setTarefaDto() {
		novaTarefaDto = new TarefaDto(1, "Tarefa teste", Categoria.PESSOAL, false);
	}
	
	@Test
	void tarefaDtoIdTest() {
		novaTarefaDto.setId(10);
		assertEquals(10, novaTarefaDto.getId());
	}
	
	@Test
	void tarefaDtoDescricaoTest() {
		novaTarefaDto.setDescricao("Tarefa alterada");
		assertEquals("Tarefa alterada", novaTarefaDto.getDescricao());
	}
	
	@Test
	void tarefaDtoCategoriaValidaTest() {
		assertDoesNotThrow(() -> novaTarefaDto.setCategoria("trabalho"));
		assertEquals(Categoria.TRABALHO, novaTarefaDto.getCategoria());
		assertDoesNotThrow(() -> novaTarefaDto.setCategoria("domestica"));
		assertEquals(Categoria.DOMESTICA, novaTarefaDto.getCategoria());
		assertDoesNotThrow(() -> novaTarefaDto.setCategoria("pessoal"));
		assertEquals(Categoria.PESSOAL, novaTarefaDto.getCategoria());
		assertDoesNotThrow(() -> novaTarefaDto.setCategoria("programacao"));
		assertEquals(Categoria.PROGRAMACAO, novaTarefaDto.getCategoria());
	}
	
	@Test 
	void tarefaDtoCategoriaInvalidaTest() {
		assertThrows(IllegalArgumentException.class, ( ) -> novaTarefaDto.setCategoria("Categoria inválida"));
	}
	
	@Test
	void tarefaDtoTrueConcluidoTest() {
		novaTarefaDto.setConcluido(true);
		assertTrue(novaTarefaDto.isConcluido());
	}
}
