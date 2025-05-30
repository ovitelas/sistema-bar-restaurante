package com.victor.sistemabar.dto;

import com.victor.sistemabar.dto.FiltroRelatorioDTO;
import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.repository.ComandaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/relatorio")
public class RelatorioControllerDTO {
	
	private final ComandaRepository comandaRepository;
	
	@GetMapping
	public String mostraFormulario(Model model) {
		model.addAttribute("filtro", new FiltroRelatorioDTO());
		return "relatorio/formulario";
	}
	
	@PostMapping
	public String gerarRelatorio(@ModelAttribute("filtro") FiltroRelatorioDTO filtro, Model model) {
		List<Comanda> resultado = comandaRepository.findByFiltro(null, null, filtro.getDataInicio(), filtro.getDataFim(), filtro.getNomeCliente());
		model.addAttribute("comandas", resultado);
		return "relatorio/resultado";
	}
	

}
