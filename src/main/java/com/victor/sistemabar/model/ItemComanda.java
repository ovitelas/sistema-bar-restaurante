package com.victor.sistemabar.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemComanda {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comanda comanda;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private BigDecimal subtotal;
}


