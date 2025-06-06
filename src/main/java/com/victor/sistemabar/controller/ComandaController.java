package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.repository.ClienteRepository;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.ProdutoRepository;
import com.victor.sistemabar.util.CodigoBarrasUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comandas")
public class ComandaController {
	
	private final ComandaRepository comandaRepository;
	private final ProdutoRepository produtoRepository;
	private final ClienteRepository clienteRepository;
	
	@GetMapping
	public String listarComandas(Model model) {
		model.addAttribute("comandas", comandaRepository.findAll());
		return "comandas/lista";
	}
	
	@GetMapping("/nova")
	public String novaComanda(Model model) {
		model.addAttribute("comanda", new Comanda());
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("produtos", produtoRepository.findAll());
		return "comandas/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarComanda(@Valid Comanda comanda, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("clientes", clienteRepository.findAll());
			model.addAttribute("produtos", produtoRepository.findAll());
			return "comandas/formulario";
		}
		
		BigDecimal total = comanda.getItens().stream()
				.map(item -> item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		comanda.setTotal(total);
		comanda.setCodigoBarras(CodigoBarrasUtil.gerarCodigoBarras());
		
		comandaRepository.save(comanda);
		
		return "redirect:/comandas";
		
				
	}
	
	
	@GetMapping("/editar/{id}")
	public String editarComanda(@PathVariable Long id, Model model) {
		Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda inválida: " + id));
		model.addAttribute("comanda", comanda);
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("produtos", produtoRepository.findAll());
		return "comandas/formulario";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirComanda(@PathVariable Long id) {
		comandaRepository.deleteById(id);
		return "redirect:/comandas";
	}
}
