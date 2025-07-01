package com.victor.sistemabar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "comanda_id")
    @NotNull(message = "A comanda é obrigatória.")
    private Comanda comanda;

    @NotNull(message = "O valor pago é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valorPago;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private LocalDateTime dataHora = LocalDateTime.now(); //data e hora
    
    @PrePersist  // persistir antes de salvar no banco - data e hora
    public void prePersist() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }

}
