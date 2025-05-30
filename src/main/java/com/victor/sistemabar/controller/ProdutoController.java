package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {
	
	private final ProdutoRepository produtoRepository;
	
	@GetMapping
	public String listarProdutos(Model model) {
		model.addAttribute("produtos", produtoRepository.findAll());
		return "produtos/lista";
	}
	
	@GetMapping("/novo")
	public String mostrarFormulario(Model model) {
		model.addAttribute("produto", new Produto());
		return "produtos/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarProduto(@Valid Produto produto, BindingResult result) {
		if (result.hasErrors()) {
			return "produtos/formulario";
		}
		produtoRepository.save(produto);
		return "redirect:/produtos";
		
	}
	
	@GetMapping("/editar/{id}")
	public String editarProduto(@PathVariable Long id, Model model) {
		var produto = produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));
		model.addAttribute("produto", produto);
		return "produtos/formulario";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirProduto(@PathVariable Long id) {
		produtoRepository.deleteById(id);
		return "redirect:/produtos";
	}
	
	
}
