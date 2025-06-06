package com.victor.sistemabar.dto;

import jakarta.validation.constraints.*;

public class UsuarioDTO {
	
	@NotBlank(message = "O nome de usuario é obrigatório.")
	private String username;
	
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
	private String senha;
	
	@NotBlank(message = "A confirmação de senha é obrigatória.")
	@Size(min = 6, message = "A confirmação de senha deve ter no mínimo 6 caracteres.")
	private String confirmarSenha;
	
	@NotBlank(message = "O papel do usuario é obrigatório.")
	private String role;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	

}
