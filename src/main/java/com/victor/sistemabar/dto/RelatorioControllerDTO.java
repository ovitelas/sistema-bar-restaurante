package com.victor.sistemabar.dto;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.dto.FiltroRelatorioDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/relatorio")
public class RelatorioControllerDTO {

    private final ComandaRepository comandaRepository;

    // Exibe o formulário de filtro para o usuário
    @GetMapping
    public String mostraFormulario(Model model) {
        model.addAttribute("filtro", new FiltroRelatorioDTO());
        return "relatorios/formulario";
        
    } 

    // Gera o relatório com base nos filtros preenchidos
    @PostMapping
    public String gerarRelatorio(@ModelAttribute("filtro") FiltroRelatorioDTO filtro, Model model) {
        List<Comanda> resultado = comandaRepository.findByFiltro(
                null,
                filtro.getDataInicio(),
                filtro.getDataFim(),
                filtro.getNomeCliente()
        );
        model.addAttribute("comandas", resultado);
        return "relatorios/resultado"; 
    }
}
