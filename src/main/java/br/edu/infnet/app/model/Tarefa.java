package br.edu.infnet.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tarefa")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tarefa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Min(value = 1, message = "ID inválido, o id deve ser um inteiro positivo.")
	private Integer Id;
	@NotBlank(message = "A descrição não pode ser vazia.")
	@Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres.")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "A descrição deve ter apenas letras e números.")
	private String descricao;
	@Enumerated(EnumType.ORDINAL)
	private Categoria categoria;
	private boolean concluido;
	
}
