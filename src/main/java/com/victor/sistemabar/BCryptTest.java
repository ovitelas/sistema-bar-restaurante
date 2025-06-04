package com.victor.sistemabar;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {

	public static void main(String[] args) {
        String senha = "1234";
        String hash = new BCryptPasswordEncoder().encode(senha);
        System.out.println("Senha criptografada: " + hash);
    }
}
