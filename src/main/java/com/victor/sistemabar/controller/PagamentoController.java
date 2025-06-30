package com.victor.sistemabar.controller;

import com.victor.sistemabar.dto.FiltroPagamentoDTO;
import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.FormaPagamento;
import com.victor.sistemabar.model.Pagamento;
import com.victor.sistemabar.service.ComandaService;
import com.victor.sistemabar.service.PagamentoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PagamentoController {
	
	private final ComandaService comandaService;
	private final PagamentoService pagamentoService;
	
	@GetMapping("/registrar/{comandaId}")
	public String mostrarFormularioPagamento(@PathVariable Long comandaId, Model model) {
		Comanda comanda = comandaService.buscarPorId(comandaId);
		model.addAttribute("comanda", comanda);
		return "pagamentos/formulario";
	}
	
	@PostMapping("/registrar")
	public String registrarPagamentoSubmit(@ModelAttribute("pagamento") Pagamento pagamento,
	                                       BindingResult result,
	                                       Model model,
	                                       RedirectAttributes redirect) {

	    Comanda comanda = comandaService.buscarPorId(pagamento.getComanda().getId());
	    
	    if (pagamento.getValorPago().compareTo(comanda.getTotal()) < 0) {
	        result.rejectValue("valorPago", null, "O valor pago não pode ser menor que o total da comanda.");
	        model.addAttribute("formasPagamento", FormaPagamento.values());
	        model.addAttribute("comanda", comanda);
	        return "pagamentos/registrar";
	    }

	    pagamentoService.registrarPagamento(pagamento);
	    redirect.addFlashAttribute("mensagemSucesso", "Pagamento registrado com sucesso!");
	    return "redirect:/comandas/detalhes/" + comanda.getId();
	}

	
	@GetMapping("/listar")
	public String listarPagamentos(Model model) {
		model.addAttribute("pagamentos", pagamentoService.listarTodos());
		return "pagamentos/lista";
	}
	
	@GetMapping("/filtro")
	public String mostrarFiltro(Model model) {
	    model.addAttribute("filtro", new FiltroPagamentoDTO());
	    model.addAttribute("formas", FormaPagamento.values());
	    return "pagamentos/filtro";
	}

	@PostMapping("/filtro")
	public String filtrarPagamentos(@ModelAttribute("filtro") FiltroPagamentoDTO filtro, Model model) {
	    List<Pagamento> pagamentosFiltrados = pagamentoService.filtrarPagamentos(filtro);
	    model.addAttribute("pagamentos", pagamentosFiltrados);
	    return "pagamentos/lista";
	}
	
	@GetMapping("/relatorio/pdf")
	public void gerarRelatorioPagamentosPDF(HttpServletResponse response) throws Exception {
	    List<Pagamento> pagamentos = pagamentoService.listarTodos();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=pagamentos.pdf");

	    pagamentoService.gerarRelatorioPagamentosPDF(pagamentos, response.getOutputStream());
	}

	

}
