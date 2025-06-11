package com.victor.sistemabar.config;

import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {
	
	@Bean
	@Transactional
	public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuarioRepository.findByUsername("admin").isEmpty()) {
				Usuario admin = new Usuario();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123")); // senha será criptografada
				admin.setRole("ADMIN");
				usuarioRepository.save(admin);
				System.out.println("Usuário admin criado.");
			} else {
				System.out.println("Usuário admin já existe.");
			}
		};
	}
	
}
