package com.victor.sistemabar.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {
	

	    @Autowired
	    private ProdutoRepository produtoRepository;

	    public void salvar(Produto produto) {
	        produtoRepository.save(produto);
	    }

	    public List<Produto> listarTodos() {
	        return produtoRepository.findAll();
	    }
	
}
