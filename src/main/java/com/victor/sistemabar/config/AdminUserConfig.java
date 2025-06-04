package com.victor.sistemabar.config;

import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserConfig {

	@Bean
	CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuarioRepository.findByUsername("admin").isEmpty()) {
				Usuario admin = new Usuario();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("1234"));
				admin.setRole("ROLE_ADMIN");
				usuarioRepository.save(admin);
				System.out.println("Usuario admin criado com sucesso!");
			}
		};
	}
	
}
