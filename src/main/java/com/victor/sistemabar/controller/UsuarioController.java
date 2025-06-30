package com.victor.sistemabar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.victor.sistemabar.dto.UsuarioDTO;
import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/novo")
	public String formCadastro(Model model) {
	    model.addAttribute("usuarioDTO", new UsuarioDTO());
	    return "usuarios/formulario"; 
	}

	
	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
	                     BindingResult result, Model model) {

	    if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
	        result.rejectValue("username", null, "Nome de usuário já está em uso.");
	    }

	    if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmarSenha())) {
	        result.rejectValue("confirmarSenha", null, "As senhas não coincidem.");
	    }

	    if (result.hasErrors()) {
	        model.addAttribute("usuarioDTO", usuarioDTO);
	        return "usuarios/formulario";
	    }

	    Usuario usuario = new Usuario();
	    usuario.setUsername(usuarioDTO.getUsername());
	    usuario.setPassword(passwordEncoder.encode(usuarioDTO.getSenha()));
	    usuario.setRole("ROLE_" + usuarioDTO.getRole().toUpperCase());

	    usuarioRepository.save(usuario);
	    return "redirect:/login";
	}

	
	@PostMapping("/criar")
	public String criarUsuario(@RequestParam String username, @RequestParam String senha) {
		Usuario usuario = new Usuario();
		usuario.setUsername(username);
		usuario.setPassword(passwordEncoder.encode(senha));
		usuario.setRole("ROLE_USER");
		
		usuarioRepository.save(usuario);
		return "redirect:/login";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listar")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista"; 
    }
	
	@GetMapping("/editar")
	public String editarUsuario(@RequestParam ("id") Long id, Model model) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
		model.addAttribute("usuario", usuario);
		return "usuarios/editar";
	}
	
	@PostMapping("/atualizar")
	public String atualizarUsuario(@RequestParam Long id,
								   @RequestParam String username,
								   @RequestParam String senha,
								   @RequestParam String role) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
		usuario.setUsername(username);
		if (!senha.isBlank()) {
			usuario.setPassword(passwordEncoder.encode(senha));
		}
		usuario.setRole(role);
		usuarioRepository.save(usuario);
		return "redirect:/usuarios/listar";
		
	}
	
	@GetMapping("/excluir")
	public String excluirUsuario(@RequestParam("id") Long id) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
		usuarioRepository.delete(usuario);
		return "redirect:/usuarios/listar";
	}
	

}


