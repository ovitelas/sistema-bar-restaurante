package com.victor.sistemabar.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @NotBlank(message = "Nome é obrigatório")
	 private String nome;

	 @NotBlank(message = "Código de barras é obrigatório")
	 @Column(unique = true)
	 private String codigoBarras;

	 @NotBlank(message = "Descrição é obrigatória")
	 private String descricao;

	 @NotNull(message = "Preço é obrigatório")
	 @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
	 private BigDecimal preco;
	 
	 @NotNull(message = "Quantidade em estoque é obrigatória")
	 @Min(value = 0, message = "Quantidade deve ser 0 ou maior")
	 private Integer quantidadeEstoque;
	 
	 private boolean ativo = true;
	 
	 @Column(updatable = false)
	 private LocalDateTime dataCadastro;
	 
	 @PrePersist
	 public void prePersist() {
	     this.dataCadastro = LocalDateTime.now();
	 }

}
