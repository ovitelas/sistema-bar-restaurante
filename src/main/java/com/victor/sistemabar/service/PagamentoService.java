package com.victor.sistemabar.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.victor.sistemabar.dto.FiltroPagamentoDTO;
import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.FormaPagamento;
import com.victor.sistemabar.model.Pagamento;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.PagamentoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PagamentoService {

	private final PagamentoRepository pagamentoRepository;
	private final ComandaRepository comandaRepository;
	
	@Transactional
	public Pagamento registrarPagamento(Pagamento pagamento) {
	    Comanda comanda = comandaRepository.findById(pagamento.getComanda().getId())
	            .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + pagamento.getComanda().getId()));

	    pagamento.setComanda(comanda);
	    comanda.fechar();
	    comandaRepository.save(comanda);

	    return pagamentoRepository.save(pagamento);
	}


	
	public List<Pagamento> listarTodos() {
		return pagamentoRepository.findAll();
	}
	
	public List<Pagamento> filtrarPagamentos(FiltroPagamentoDTO filtro) {
	    return pagamentoRepository.filtrarPagamentos(
	            filtro.getDataInicio(),
	            filtro.getDataFim(),
	            filtro.getFormaPagamento()
	    );
	}
	
	public void gerarRelatorioPagamentosPDF(List<Pagamento> pagamentos, OutputStream out) throws Exception {
	    Document document = new Document();
	    PdfWriter.getInstance(document, out);
	    document.open();

	    document.add(new Paragraph("Relatório de Pagamentos"));
	    document.add(new Paragraph(" "));

	    for (Pagamento p : pagamentos) {
	        document.add(new Paragraph(
	            String.format("Comanda #%d | Cliente: %s | Valor: R$ %.2f | Forma: %s | Data: %s",
	            p.getComanda().getId(),
	            p.getComanda().getCliente().getNome(),
	            p.getValorPago(),
	            p.getFormaPagamento(),
	            p.getDataHora().toString()
	        )));
	        document.add(new Paragraph(" "));
	    }

	    document.close();
	}
	
	

}
