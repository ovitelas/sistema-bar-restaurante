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
	
		 
	 private LocalDateTime dataHora = LocalDateTime.now();
	 
	 @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<ItemComanda> itens = new ArrayList<>();

	 @Enumerated(EnumType.STRING)
	 private StatusComanda status;
	 
	 @ManyToMany
	 @JoinTable(
	     name = "comanda_produtos",
	     joinColumns = @JoinColumn(name = "comanda_id"),
	     inverseJoinColumns = @JoinColumn(name = "produto_id")
	 )
	 private List<Produto> produtos = new ArrayList<>();
	 
	 private BigDecimal total;
	 
	 private String codigoBarras;

}
