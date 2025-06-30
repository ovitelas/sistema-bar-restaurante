package com.victor.sistemabar.repository;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.StatusComanda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    @Query("SELECT c FROM Comanda c " +
           "WHERE (:clienteId IS NULL OR c.cliente.id = :clienteId) " +
           "AND (:dataInicio IS NULL OR c.data >= :dataInicio) " +
           "AND (:dataFim IS NULL OR c.data <= :dataFim) " +
           "AND (:nomeCliente IS NULL OR LOWER(c.cliente.nome) LIKE LOWER(CONCAT('%', :nomeCliente, '%')))")
    List<Comanda> findByFiltro(
            @Param("clienteId") Long clienteId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("nomeCliente") String nomeCliente
    );
    
    boolean existsByCodigoBarras (String codigoBarras);
    
    long countByStatus(StatusComanda status);
}