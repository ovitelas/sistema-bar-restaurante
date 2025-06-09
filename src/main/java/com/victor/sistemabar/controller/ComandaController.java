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
import java.io.OutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import com.google.zxing.WriterException;



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
		model.addAttribute("comanda",  comanda);
		model.addAttribute("clientes", clienteRepository.findAll());
		model.addAttribute("produtos", produtoRepository.findAll());
		return "comandas/formulario";
	}
	
	@PostMapping("/salvar")
	public String salvarComanda(@Valid Comanda comanda, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("clientes", clienteRepository.findAll());
			model.addAttribute("produtos", produtoRepository.findAll());
			return "comandas/formulario";
		}
		
		BigDecimal total = BigDecimal.ZERO;
		for (ItemComanda item : comanda.getItens()) {
			Produto produto = produtoRepository.findById(item.getProduto().getId()).orElseThrow();
			item.setProduto(produto);
			item.setComanda(comanda);
			item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
			total = total.add(item.getSubtotal());
		}
		
		comanda.setTotal(total);
		comanda.setStatus(StatusComanda.ABERTA);
		comanda.setCodigoBarras(CodigoBarrasUtil.gerarCodigoBarras());
		
		comandaRepository.save(comanda);
		
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
	public String excluirComanda(@PathVariable Long id) {
		comandaRepository.deleteById(id);
		return "redirect:/comandas";
	}
	
	
	@GetMapping("/detalhes/{id}")
	public String detalhesComanda(@PathVariable Long id, Model model) {
		Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda invalida" + id));
		model.addAttribute("comanda", comanda);
		
		return "comandas/detalhes";
	}
	
	@GetMapping("/fechar/{id}")
	public String fecharComanda(@PathVariable Long id) {
		Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada" + id));
		comanda.setStatus(StatusComanda.FECHADA);
		comandaRepository.save(comanda);
		return "redirect: /comandas / detalhes/" + id;
	}
	
	@GetMapping("/cancelar/{id}")
	public String cancelarComanda(@PathVariable Long id) {
		Comanda comanda = comandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada" + id));
		if (comanda.getStatus() == StatusComanda.ABERTA) {
			comanda.setStatus(StatusComanda.CANCELADA);
			comandaRepository.save(comanda);
		}
		
		return "redirect:/comandas/detalhes";
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
	    
	    BitMatrix matrix = new MultiFormatWriter().encode(
	    	    comanda.getCodigoBarras(),
	    	    BarcodeFormat.CODE_128,
	    	    300, 100
	    	);
	    	BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	ImageIO.write(bufferedImage, "png", baos);
	    	baos.flush();
	    	byte[] barcodeBytes = baos.toByteArray();
	    	baos.close();
	    	Image barcodeImage = Image.getInstance(barcodeBytes);
	    	barcodeImage.scalePercent(100);
	    	document.add(barcodeImage);
	    	document.add(new Paragraph(" "));

	    document.add(new Paragraph("Comanda #" + comanda.getId()));
	    document.add(new Paragraph("Cliente: " + comanda.getCliente().getNome()));
	    document.add(new Paragraph("Data: " + comanda.getData().toString()));
	    document.add(new Paragraph("Status: " + comanda.getStatus().name()));
	    document.add(new Paragraph("Total: R$ " + comanda.getTotal()));
	    document.add(new Paragraph(" "));

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


}
