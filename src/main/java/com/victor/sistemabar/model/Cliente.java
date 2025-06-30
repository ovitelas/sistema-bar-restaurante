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
	private String nome;
	
	@Email(message = "Email inválido")
	private String email;
	
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
	@Column(unique = true)
	private String cpf;

	
	private String telefone;
}
