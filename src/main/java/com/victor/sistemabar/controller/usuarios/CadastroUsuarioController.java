package com.victor.sistemabar.controller.usuarios;

import com.victor.sistemabar.dto.UsuarioDTO;
import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class CadastroUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/cadastro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "usuarios/cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@ModelAttribute("usuarioDTO") @Valid UsuarioDTO dto,
                                    BindingResult result, Model model) {

        // Verifica se username já está em uso
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
        	result.rejectValue("username", "error.usuarioDTO", "Este nome de usuário já está em uso.");
        }

        // Verifica se as senhas coincidem
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

        return "redirect:/login?cadastroSucesso";
    }
}
