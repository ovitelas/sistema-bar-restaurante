package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.Pagamento;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.PagamentoRepository;
import com.victor.sistemabar.service.RelatorioService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	
	@GetMapping("/relatorio/pdf")
	public void downloadPDF(HttpServletResponse response,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
	    @RequestParam(required = false) Long clienteId,
	    @RequestParam(required = false) String nomeCliente
	) throws Exception {
		List<Pagamento> pagamentos = pagamentoRepository.findByFiltro(dataInicio, dataFim, clienteId, nomeCliente);
		relatorioService.gerarRelatorioPDFPagamentos(pagamentos, response.getOutputStream());

	}

	
	
	@GetMapping("/relatorio/excel")
	public void downloadExcel(HttpServletResponse response,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
	    @RequestParam(required = false) Long clienteId,
	    @RequestParam(required = false) String nomeCliente
	) throws Exception {

	    List<Pagamento> pagamentos = pagamentoRepository.findByFiltro(dataInicio, dataFim, clienteId, nomeCliente);

	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=relatorio-pagamentos.xlsx");

	    relatorioService.gerarRelatorioExcelPagamento(pagamentos, response.getOutputStream());
	}


}
	
