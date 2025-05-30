package com.victor.sistemabar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	  @GetMapping("/login")
	    public String login() {
	        return "login"; // Vai retornar o arquivo login.html do Thymeleaf (em templates)
	    }

}
