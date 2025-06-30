package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Cliente;
import com.victor.sistemabar.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {
	
	private final ClienteService clienteService;

	@GetMapping
	public String listarClientes(Model model,
	                             @RequestParam(defaultValue = "0") int page,
	                             @RequestParam(defaultValue = "20") int size) {
	    var pagina = clienteService.listarPaginado(PageRequest.of(page, size));
	    model.addAttribute("clientes", pagina.getContent());
	    model.addAttribute("paginaAtual", page);
	    model.addAttribute("totalPaginas", pagina.getTotalPages());
	    return "clientes/lista";
	}

	@GetMapping("/novo")
	public String novoCliente(Model model) {
		model.addAttribute("cliente", new Cliente()); // Corrigido "Cliente" → "cliente"
		return "clientes/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
		if (result.hasErrors()) {
			return "clientes/formulario";
		}
		clienteService.salvar(cliente); // Usando service
		return "redirect:/clientes";
	}
	
	@GetMapping("/editar/{id}")
	public String editarCliente(@PathVariable Long id, Model model) {
		Cliente cliente = clienteService.buscarPorId(id); // Usando service
		model.addAttribute("cliente", cliente);
		return "clientes/formulario";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirCliente(@PathVariable Long id) {
		clienteService.excluir(id); // Usando service
		return "redirect:/clientes";
	}
	
	@GetMapping("/buscar")
	public String buscarClientes(@RequestParam(required = false) String nome,
								@RequestParam(required = false) String cpf, Model model) {
		if (nome != null && !nome.isBlank()) {
			model.addAttribute("clientes", clienteService.buscarPorNome(nome));
		} else if (cpf != null && !cpf.isBlank()) {
			model.addAttribute("clientes", clienteService.buscarPorCpf(cpf));
		} else {
			model.addAttribute("clientes", clienteService.listarPaginado(PageRequest.of(0, 20)).getContent());
		}
		return "clientes/lista";
	}
}
