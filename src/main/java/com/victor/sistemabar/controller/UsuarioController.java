package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/criar")
	public String criarUsuario(@RequestParam String username, @RequestParam String senha) {
		Usuario usuario = new Usuario();
		usuario.setUsername(username);
		usuario.setPassword(passwordEncoder.encode(senha));
		usuario.setRole("ROLE_USER");
		
		usuarioRepository.save(usuario);
		return "redirect:/login";
	}

}
