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
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    public BigDecimal getSubtotal() {
        if (produto != null && quantidade != null) {
            return produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
        }
        return BigDecimal.ZERO;
    }
}


