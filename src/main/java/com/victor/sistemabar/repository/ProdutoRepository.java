package com.victor.sistemabar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.victor.sistemabar.model.Produto;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	Produto findByCodigoBarras(String codigoBarras);
	List<Produto> findByNomeContainingIgnoreCase(String nome);
}
