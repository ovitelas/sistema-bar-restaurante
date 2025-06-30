package com.victor.sistemabar.validacao;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SenhasIguaisValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhasIguais {
    String message() default "As senhas não coincidem.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
