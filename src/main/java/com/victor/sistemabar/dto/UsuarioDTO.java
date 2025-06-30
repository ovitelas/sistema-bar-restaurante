package com.victor.sistemabar.dto;

import com.victor.sistemabar.validacao.SenhasIguais;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SenhasIguais
public class UsuarioDTO {

    @NotBlank(message = "O nome de usuário é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;

    @NotBlank(message = "A confirmação de senha é obrigatória.")
    @Size(min = 6, message = "A confirmação de senha deve ter no mínimo 6 caracteres.")
    private String confirmarSenha;

    @NotBlank(message = "O papel do usuário é obrigatório.")
    private String role;
}

