package com.victor.sistemabar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
	
	@GetMapping("/admin/dashboard")
	public String dashboard() {
		return "admin/dashboard";
	}
	
	@GetMapping("/user/home")
	public String homeUser() {
		return "dashboard";
	}
	
	
	
}
