package com.victor.sistemabar.service;

import com.victor.sistemabar.model.*;
import com.victor.sistemabar.repository.ClienteRepository;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComandaService {

	
    private final ComandaRepository comandaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Comanda salvarComanda(Long clienteId, List<Long> produtoIds) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("Cliente ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        List<Produto> produtos = produtoRepository.findAllById(produtoIds);
        log.info("Criando nova comanda para cliente {} com {} produtos", cliente.getNome(), produtos.size());
        
        Comanda comanda = new Comanda();
        comanda.setCliente(cliente);
        comanda.setDataHora(LocalDateTime.now());
        comanda.setDataHora(LocalDateTime.now());
        comanda.setStatus(StatusComanda.ABERTA);

        // Calcular total
        BigDecimal total = produtos.stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        comanda.setCodigoBarras("CMD" + System.currentTimeMillis()); // simples para testes
        
        log.debug("Total calculado para a comanda: R$ {}", total);
        Comanda salva = comandaRepository.save(comanda);
        log.info("Comanda salva com ID: {}", salva.getId());

        return salva;
    }
    
    @Transactional
    public void fecharComanda(Long comandaId) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + comandaId));

        if (comanda.getItens() == null || comanda.getItens().isEmpty()) {
            throw new IllegalStateException("Não é possível fechar uma comanda sem itens.");
        }

        comanda.fechar();  // Ex: muda o status para FECHADA
        comandaRepository.save(comanda);
    }


    public List<Comanda> listarTodas() {
        return comandaRepository.findAll();
    }


    public void excluir(Long id) {
        comandaRepository.deleteById(id);
    }
    
    public Comanda buscarPorId(Long id) {
    	return comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada com ID: " + id));
    }
    
    public void salvarComanda(Comanda comanda) {
        
        BigDecimal total = comanda.getItens().stream()
            .map(ItemComanda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);


        
        comandaRepository.save(comanda);
    }

    
}
