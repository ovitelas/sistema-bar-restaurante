package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Cliente;
import com.victor.sistemabar.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {
	
	private final ClienteRepository clienteRepository;
	
	@GetMapping
	public String listarClientes(Model model) {
		model.addAttribute("clientes", clienteRepository.findAll());
		return "clientes/lista";
	}
	
	@GetMapping("/novo")
	public String novoCliente(Model model) {
		model.addAttribute("Cliente", new Cliente());
		return "clientes/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
		if (result.hasErrors()) {
			return "clientes/formulario";
		}
		clienteRepository.save(cliente);
		return "redirect:/clientes";
	}
	
	@GetMapping("/editar/{id}")
	public String editarCliente(@PathVariable Long id, Model model) {
		Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente inválido: " + id));
		model.addAttribute("cliente", cliente);
		return "clientes/formulario";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluirCliente(@PathVariable Long id) {
		clienteRepository.deleteById(id);
		return "redirect:/clientes";
	}

}
