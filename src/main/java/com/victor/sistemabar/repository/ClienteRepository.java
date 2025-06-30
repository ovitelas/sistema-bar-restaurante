package com.victor.sistemabar.repository;

import com.victor.sistemabar.model.Cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {
	
	List<Cliente> findByNomeContainingIgnoreCase(String nome);
	List<Cliente> findByCpfContaining(String cpf);

}
