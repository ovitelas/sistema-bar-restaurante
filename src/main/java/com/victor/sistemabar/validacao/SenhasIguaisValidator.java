package com.victor.sistemabar.validacao;

import com.victor.sistemabar.dto.UsuarioDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, UsuarioDTO> {

    @Override
    public boolean isValid(UsuarioDTO dto, ConstraintValidatorContext context) {
        if (dto.getSenha() == null || dto.getConfirmarSenha() == null) {
            return false;
        }

        boolean iguais = dto.getSenha().equals(dto.getConfirmarSenha());

        if (!iguais) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("As senhas não coincidem.")
                   .addPropertyNode("confirmarSenha")
                   .addConstraintViolation();
        }

        return iguais;
    }
}
