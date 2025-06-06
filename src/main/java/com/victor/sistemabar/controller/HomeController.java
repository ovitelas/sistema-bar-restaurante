package com.victor.sistemabar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class HomeController {

	@GetMapping("admin/dashboard")
	public String adminDashboard() {
		return "admin-dashboard";
	}
	
	@GetMapping("user/home")
	public String userHome() {
		return "home";
	}
	
	@GetMapping("acesso-negado")
	public String acessoNegado() {
		return "acesso-negado";
	}
	
}
