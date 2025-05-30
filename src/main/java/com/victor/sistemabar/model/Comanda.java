package com.victor.sistemabar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Comanda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	private LocalDateTime data;
	
	 @ManyToMany
	 @JoinTable(
	        name = "comanda_produtos",
	        joinColumns = @JoinColumn(name = "comanda_id"),
	        inverseJoinColumns = @JoinColumn(name = "produto_id")
			 )
	 
	 private List<Produto> produtos = new ArrayList<>();
	 
	 private LocalDateTime dataHora = LocalDateTime.now();
	 
	 private BigDecimal total;
	 
	 private String codigoBarras;

	
	
	
}
