package com.victor.sistemabar.controller.usuarios;

import com.victor.sistemabar.dto.UsuarioDTO;
import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class CadastroUsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/cadastro")
	public String mostrarFormulario(Model model) {
		model.addAttribute("usuarioDTO", new UsuarioDTO());
		return "usuarios/cadastro";
	}
	
	@PostMapping("/cadastro")
	public String processarCadastro(@ModelAttribute("usuarioDTO") @Valid UsuarioDTO dto,
			BindingResult result, Model model) {
		if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
			result.rejectValue("confirmarSenha", "error.usuarioDTO", "As senhas não coincidem.");
		}
		
		if (result.hasErrors()) {
			return "usuarios/cadastro";
		}
		
		Usuario usuario = new Usuario();
		usuario.setUsername(dto.getUsername());
		usuario.setPassword(passwordEncoder.encode(dto.getSenha()));
		usuario.setRole(dto.getRole());
		
		usuarioRepository.save(usuario);
		
		return "redirect:/login";
	}
	
}
