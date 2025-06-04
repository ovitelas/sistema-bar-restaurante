package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;
	
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
	public String salvarProduto(@Valid @ModelAttribute("produto") Produto produto, BindingResult result) {
		if (result.hasErrors()) {
			return "produtos/formulario";
		}
		System.out.println("Produto a ser salvo: " + produto);
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
	
	@GetMapping("/buscar")
	public String buscarProdutos(@RequestParam (required = false) String nome,
			@RequestParam(required = false) String codigoBarras, Model model) {
		if (nome != null && !nome.isEmpty()) {
			model.addAttribute("produtos", produtoRepository.findByNomeContainingIgnoreCase(nome));
		} else if (codigoBarras != null && !codigoBarras.isEmpty()) {
			Produto produto = produtoRepository.findByCodigoBarras(codigoBarras);
			model.addAttribute("produtos", produto != null ? List.of(produto) : List.of());
		} else {
			model.addAttribute("produtos", produtoRepository.findAll());
		}
		
		return "produtos/lista";
	}
	
	
}
