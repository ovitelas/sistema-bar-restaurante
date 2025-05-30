package com.victor.sistemabar.repository;

import com.victor.sistemabar.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {

}
