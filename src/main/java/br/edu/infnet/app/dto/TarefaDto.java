package br.edu.infnet.app.dto;
import java.lang.IllegalArgumentException;

import br.edu.infnet.app.model.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TarefaDto {
	@Min(value = 1, message = "ID inválido, o id deve ser um inteiro positivo.")
	private Integer Id;
	@NotBlank(message = "A descrição não pode ser vazia.")
	@Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres.")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "A descrição deve ter apenas letras e números.")
	private String descricao;
	private Categoria categoria;
	private boolean concluido;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		switch(categoria) {
		case "trabalho" -> this.categoria = Categoria.TRABALHO;
		case "domestica" -> this.categoria = Categoria.DOMESTICA;
		case "pessoal" -> this.categoria = Categoria.PESSOAL;
		case "programacao" -> this.categoria = Categoria.PROGRAMACAO;
		default -> throw new IllegalArgumentException("Categoria Inválida.");
		}
	}
	public boolean isConcluido() {
		return concluido;
	}
	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
	
	
}
