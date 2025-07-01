package com.victor.sistemabar.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.victor.sistemabar.dto.FiltroPagamentoDTO;
import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.FormaPagamento;
import com.victor.sistemabar.model.Pagamento;
import com.victor.sistemabar.repository.ComandaRepository;
import com.victor.sistemabar.repository.PagamentoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

	private final PagamentoRepository pagamentoRepository;
	private final ComandaRepository comandaRepository;
	
	@Transactional
	public Pagamento registrarPagamento(Pagamento pagamento) {
	    Comanda comanda = comandaRepository.findById(pagamento.getComanda().getId())
	            .orElseThrow(() -> new IllegalArgumentException("Comanda não encontrada: " + pagamento.getComanda().getId()));

	    if (pagamentoRepository.existsByComandaId(comanda.getId())) {
	        throw new IllegalStateException("Esta comanda já foi paga.");
	    }

	    if (comanda.getItens() == null || comanda.getItens().isEmpty()) {
	        throw new IllegalStateException("Não é possível pagar uma comanda sem itens.");
	    }

	    pagamento.setComanda(comanda);
	    comanda.fechar();
	    comandaRepository.save(comanda);

	    return pagamentoRepository.save(pagamento);
	}



	
	public List<Pagamento> listarTodos() {
		return pagamentoRepository.findAll();
	}
	
	public List<Pagamento> filtrarPagamentos(FiltroPagamentoDTO filtro) {
		log.debug("Filtrando pagamentos: Início={}, Fim={}, Forma={}",
                filtro.getDataInicio(), filtro.getDataFim(), filtro.getFormaPagamento());

	    return pagamentoRepository.filtrarPagamentos(
	        filtro.getDataInicio(),
	        filtro.getDataFim(),
	        filtro.getFormaPagamento()
	    );
	}



	
	public void gerarRelatorioPDFPagamentos(List<Pagamento> pagamentos, OutputStream out) throws Exception {
	    Document document = new Document(PageSize.A4);
	    PdfWriter.getInstance(document, out);
	    document.open();

	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
	    Paragraph title = new Paragraph("Relatório de Pagamentos", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(15f);
	    document.add(title);

	    PdfPTable table = new PdfPTable(5);
	    table.setWidthPercentage(100);
	    table.setWidths(new float[]{1f, 3f, 2f, 2f, 2f});
	    table.setSpacingBefore(10f);

	    table.addCell("ID");
	    table.addCell("Cliente");
	    table.addCell("Forma de Pagamento");
	    table.addCell("Valor Pago");
	    table.addCell("Data/Hora");

	    for (Pagamento p : pagamentos) {
	        table.addCell(String.valueOf(p.getId()));
	        table.addCell(p.getComanda().getCliente().getNome());
	        table.addCell(p.getFormaPagamento().name());
	        table.addCell(String.format("R$ %.2f", p.getValorPago()));
	        table.addCell(p.getDataHora().toString());
	    }

	    document.add(table);
	    document.close();
	}

	
	

}
