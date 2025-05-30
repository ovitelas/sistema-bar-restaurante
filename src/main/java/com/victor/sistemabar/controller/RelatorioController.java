package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RelatorioController {

	private final ComandaRepository comandaRepository;
	private final RelatorioService relatorioService;
	
	@GetMapping("/relatorio/pdf")
	public void downoadPDF (HttpServletResponse response,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
			@RequestParam(required = false) Long clienteId) throws Exception {
		List<Comanda> comandas = comandaRepository.findByFiltro(clienteId, data, null, null, null);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=relatorio.pdf");
		
		relatorioService.gerarRelatorioPDF(comandas, response.getOutputStream());
	}
	
	@GetMapping("/relatorio/excel")
	public void downloadExcel(HttpServletResponse response,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
			@RequestParam(required = false) Long clienteId) throws Exception {
		
		List<Comanda> comandas = comandaRepository.findByFiltro(clienteId, data, null, null, null);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=relatorio.xlsx");
		
		relatorioService.gerarRelatorioExcel(comandas, response.getOutputStream());
	}
}
	
