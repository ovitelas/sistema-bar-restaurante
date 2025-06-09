package com.victor.sistemabar.service;

import com.victor.sistemabar.model.*;
import com.victor.sistemabar.repository.ClienteRepository;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComandaService {

	
    private final ComandaRepository comandaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Comanda salvarComanda(Long clienteId, List<Long> produtoIds) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<Produto> produtos = produtoRepository.findAllById(produtoIds);

        Comanda comanda = new Comanda();
        comanda.setCliente(cliente);
        comanda.setData(LocalDateTime.now());
        comanda.setDataHora(LocalDateTime.now());
        comanda.setProdutos(produtos);
        comanda.setStatus(StatusComanda.ABERTA);

        // Calcular total
        BigDecimal total = produtos.stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        comanda.setTotal(total);
        comanda.setCodigoBarras("CMD" + System.currentTimeMillis()); // simples para testes

        return comandaRepository.save(comanda);
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
    
}
