package com.victor.sistemabar.config;

import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        String username = "admin@sistemabar.com";
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode("admin123")); // senha real em texto
            admin.setRole("ADMIN");
            usuarioRepository.save(admin);
            System.out.println("✔ Usuário admin criado com sucesso.");
        } else {
            System.out.println("ℹ Usuário admin já existe.");
        }
    }
}
