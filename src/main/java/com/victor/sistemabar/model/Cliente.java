package com.victor.sistemabar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome é obrigatorio")
	@Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
	private String nome;
	
	@Email(message = "Email inválido")
	private String email;
	
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
	@Column(unique = true)
	private String cpf;

	@Size(max = 15, message = "O telefone deve ter no máximo 15 dígitos.")
	private String telefone;
}
