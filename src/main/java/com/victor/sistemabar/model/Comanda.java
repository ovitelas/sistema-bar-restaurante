package com.victor.sistemabar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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

    private LocalDateTime dataHora = LocalDateTime.now();

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemComanda> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusComanda status = StatusComanda.ABERTA;

    private String codigoBarras;

    public BigDecimal getTotal() {
        return itens.stream()
                .map(ItemComanda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void fechar() {
        this.status = StatusComanda.FECHADA;
    }
}
