package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String listarProdutos(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "nome") String sort,
                                 @RequestParam(defaultValue = "asc") String dir,
                                 Model model) {

        Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        
        Page<Produto> produtosPage = produtoService.listarPaginado(pageable);

        model.addAttribute("produtosPage", produtosPage);
        model.addAttribute("produtos", produtosPage.getContent());
        model.addAttribute("paginaAtual", page);
        model.addAttribute("totalPaginas", produtosPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        
        return "produtos/lista";
    }


    @GetMapping("/novo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/formulario";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") Produto produto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "produtos/formulario";
        }

        try {
            produtoService.salvar(produto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao salvar produto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensagem", "erro");
        }

        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        model.addAttribute("produto", produto);
        return "produtos/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoService.excluir(id);
        return "redirect:/produtos";
    }

    @GetMapping("/buscar")
    public String buscarProdutos(@RequestParam(required = false) String nome,
                                 @RequestParam(required = false) String codigoBarras,
                                 Model model) {

        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("produtos", produtoService.buscarPorNome(nome));
        } else if (codigoBarras != null && !codigoBarras.isEmpty()) {
            Produto produto = produtoService.buscarPorCodigoBarras(codigoBarras);
            model.addAttribute("produtos", produto != null ? List.of(produto) : List.of());
        } else {
            model.addAttribute("produtos", produtoService.listarTodos());
        }

        return "produtos/lista";
    }
}
