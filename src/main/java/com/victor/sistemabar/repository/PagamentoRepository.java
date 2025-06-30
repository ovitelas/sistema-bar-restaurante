package com.victor.sistemabar.repository;

import com.victor.sistemabar.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    boolean existsByComandaId(Long comandaId);

    @Query("SELECT p FROM Pagamento p " +
           "WHERE (:dataInicio IS NULL OR DATE(p.dataHora) >= :dataInicio) " +
           "AND (:dataFim IS NULL OR DATE(p.dataHora) <= :dataFim) " +
           "AND (:clienteId IS NULL OR p.comanda.cliente.id = :clienteId) " +
           "AND (:nomeCliente IS NULL OR LOWER(p.comanda.cliente.nome) LIKE LOWER(CONCAT('%', :nomeCliente, '%')))")
    List<Pagamento> findByFiltro(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("clienteId") Long clienteId,
            @Param("nomeCliente") String nomeCliente
    );
}
