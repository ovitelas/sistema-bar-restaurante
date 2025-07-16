package com.victor.sistemabar.controller;

import com.victor.sistemabar.repository.ClienteRepository;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.ProdutoRepository;
import com.victor.sistemabar.model.StatusComanda;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ComandaRepository comandaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
    	
    	System.out.println("Acessando o dashboard...");

        long comandasAbertas = comandaRepository.countByStatus(StatusComanda.ABERTA);

        BigDecimal faturamentoHoje = comandaRepository
                .findAll().stream()
                .filter(c -> c.getDataHora() != null &&
                c.getDataHora().toLocalDate().equals(LocalDate.now()) &&
                c.getStatus() == StatusComanda.FECHADA)
                .map(c -> c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalClientes = clienteRepository.count();
        long totalProdutos = produtoRepository.count();

        model.addAttribute("comandasAbertas", comandasAbertas);
        model.addAttribute("faturamentoHoje", faturamentoHoje);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalProdutos", totalProdutos);

        return "dashboard";
    }
}
