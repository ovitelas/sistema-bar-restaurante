package com.victor.sistemabar.service;


import com.victor.sistemabar.model.Usuario;
import com.victor.sistemabar.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username).
				orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: "));
		
		return new User(usuario.getUsername(), 
				usuario.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole())));
	}
	
}
