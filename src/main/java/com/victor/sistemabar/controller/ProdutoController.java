package com.victor.sistemabar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.repository.ProdutoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public String listarProdutos(Model model,
	                              @RequestParam(defaultValue = "0") int page,
	                              @RequestParam(defaultValue = "10") int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
	    Page<Produto> pagina = produtoRepository.findAll(pageable);

	    model.addAttribute("pagina", pagina);
	    model.addAttribute("produtos", pagina.getContent());
	    model.addAttribute("paginaAtual", page);
	    model.addAttribute("totalPaginas", pagina.getTotalPages());

	    return "produtos/lista";
	}
	
	@GetMapping("/novo")
	public String mostrarFormulario(Model model) {
		model.addAttribute("produto", new Produto());
		return "produtos/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarProduto(@Valid @ModelAttribute("produto") Produto produto, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "produtos/formulario";
		}
		
		try {
			produtoRepository.save(produto);
			redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
			redirectAttributes.addFlashAttribute("tipoMensagem", "sucesso");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagem", "Erro ao salvar produto: " + e.getMessage());
			redirectAttributes.addFlashAttribute("tipoMensagem", "erro");
		}

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
