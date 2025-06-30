package com.victor.sistemabar.controller;

import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.ItemComanda;
import com.victor.sistemabar.model.Produto;
import com.victor.sistemabar.model.StatusComanda;
import com.victor.sistemabar.repository.ClienteRepository;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.ProdutoRepository;
import com.victor.sistemabar.service.ComandaService;
import com.victor.sistemabar.util.CodigoBarrasUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.DocumentException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comandas")
public class ComandaController {

    private final ComandaRepository comandaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ComandaService comandaService;

    @GetMapping
    public String listarComandas(Model model) {
        model.addAttribute("comandas", comandaRepository.findAll());
        return "comandas/lista";
    }

    @GetMapping("/nova")
    public String novaComanda(Model model) {
        Comanda comanda = new Comanda();
        for (int i = 0; i < 3; i++) {
            comanda.getItens().add(new ItemComanda());
        }
        model.addAttribute("comanda", comanda);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        return "comandas/formulario";
    }

    @PostMapping("/salvar")
    public String salvarComanda(@Valid Comanda comanda, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("produtos", produtoRepository.findAll());
            return "comandas/formulario";
        }

        BigDecimal total = calcularTotal(comanda);
        comanda.setStatus(StatusComanda.ABERTA);
        comanda.setCodigoBarras(gerarCodigoBarrasUnico());

        comandaRepository.save(comanda);

        redirect.addFlashAttribute("mensagemSucesso", "Comanda salva com sucesso!");

        return "redirect:/comandas";
    }


    @GetMapping("/editar/{id}")
    public String editarComanda(@PathVariable Long id, Model model) {
        Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda inválida: " + id));
        model.addAttribute("comanda", comanda);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        return "comandas/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirComanda(@PathVariable Long id, RedirectAttributes redirect) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + id));

        if (comanda.getStatus() == StatusComanda.ABERTA) {
            comandaRepository.deleteById(id);
            redirect.addFlashAttribute("mensagemSucesso", "Comanda excluída com sucesso.");
        } else {
            redirect.addFlashAttribute("mensagemErro", "Só é possível excluir comandas ABERTAS.");
        }

        return "redirect:/comandas";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesComanda(@PathVariable Long id, Model model) {
        Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda inválida: " + id));
        model.addAttribute("comanda", comanda);
        return "comandas/detalhes";
    }

    @GetMapping("/fechar/{id}")
    public String fecharComanda(@PathVariable Long id, RedirectAttributes redirect) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + id));

        if (comanda.getStatus() == StatusComanda.ABERTA) {
            comanda.setStatus(StatusComanda.FECHADA);
            comandaRepository.save(comanda);
            redirect.addFlashAttribute("mensagemSucesso", "Comanda fechada com sucesso.");
        } else {
            redirect.addFlashAttribute("mensagemErro", "Apenas comandas ABERTAS podem ser fechadas.");
        }

        return "redirect:/comandas/detalhes/" + id;
    }


    @GetMapping("/cancelar/{id}")
    public String cancelarComanda(@PathVariable Long id, RedirectAttributes redirect) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + id));

        if (comanda.getStatus() == StatusComanda.ABERTA) {
            comanda.setStatus(StatusComanda.CANCELADA);
            comandaRepository.save(comanda);
            redirect.addFlashAttribute("mensagemSucesso", "Comanda cancelada com sucesso.");
        } else {
            redirect.addFlashAttribute("mensagemErro", "Apenas comandas ABERTAS podem ser canceladas.");
        }

        return "redirect:/comandas/detalhes/" + id;
    }


    @GetMapping("/comandas/barcode/{codigo}")
    public void gerarCodigoBarras(@PathVariable String codigo, HttpServletResponse response) throws Exception {
        int width = 300;
        int height = 100;

        BitMatrix matrix = new MultiFormatWriter()
                .encode(codigo, BarcodeFormat.CODE_128, width, height);

        response.setContentType("image/png");
        try (OutputStream out = response.getOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        }
    }

    @GetMapping("/pdf/{id}")
    public void gerarPdf(@PathVariable Long id, HttpServletResponse response) throws IOException, DocumentException, WriterException {
        Comanda comanda = comandaService.buscarPorId(id);
        if (comanda == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=comanda" + id + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Geração do código de barras
        BitMatrix matrix = new MultiFormatWriter().encode(
                comanda.getCodigoBarras(),
                BarcodeFormat.CODE_128,
                300, 100
        );
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        Image codigoBarrasImage = Image.getInstance(baos.toByteArray());
        codigoBarrasImage.setAlignment(Image.ALIGN_CENTER);
        document.add(new Paragraph("Código de Barras:"));
        document.add(codigoBarrasImage);
        baos.close();

        // Informações da comanda
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Comanda #" + comanda.getId()));
        document.add(new Paragraph("Cliente: " + comanda.getCliente().getNome()));
        document.add(new Paragraph("Data: " + comanda.getDataHora().toString()));
        document.add(new Paragraph("Status: " + comanda.getStatus().name()));
        document.add(new Paragraph("Total: R$ " + comanda.getTotal()));
        document.add(new Paragraph(" "));

        // Itens da comanda
        document.add(new Paragraph("Itens:"));
        for (ItemComanda item : comanda.getItens()) {
            document.add(new Paragraph(
                    "- " + item.getProduto().getNome() +
                            " | Qtd: " + item.getQuantidade() +
                            " | Preço: R$ " + item.getProduto().getPreco()
            ));
        }

        document.close();
    }
    
    private BigDecimal calcularTotal(Comanda comanda) {
    	BigDecimal total = BigDecimal.ZERO;
    	
    	for (ItemComanda item : comanda.getItens()) {
    		if (item.getProduto() != null && item.getQuantidade() > 0) {
    			Produto produto = produtoRepository.findById(item.getProduto().getId())
    					.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + item.getProduto().getId()));
    			item.setProduto(produto);
    			item.setComanda(comanda);
    			total = total.add(item.getSubtotal());
    		}
    	}
    	
    	return total;
    }
    
    private String gerarCodigoBarrasUnico() {
    	String codigo;
    	do {
    		codigo = CodigoBarrasUtil.gerarCodigoBarras();
    	} while (comandaRepository.existsByCodigoBarras(codigo));
    	return codigo;
    }

}