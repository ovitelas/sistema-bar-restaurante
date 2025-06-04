package com.victor.sistemabar.config;


import com.victor.sistemabar.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers("/login", "/css/**", "/js/**", "/imagens/**").permitAll()
	        .requestMatchers("/produtos").permitAll() 
	        .anyRequest().authenticated()
	    )
	    .formLogin(form -> form
	        .loginPage("/login")
	        .defaultSuccessUrl("/produtos", true) 
	        .permitAll()
	    )
	    .logout(logout -> logout.permitAll())
	    .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
		.headers(headers -> headers.frameOptions(frame -> frame.disable()));
		
		
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	} 
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
	
}
