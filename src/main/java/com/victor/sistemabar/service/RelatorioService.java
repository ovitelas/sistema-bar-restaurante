package com.victor.sistemabar.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.victor.sistemabar.model.Comanda;
import com.victor.sistemabar.model.Pagamento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RelatorioService {
	
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

	
	public void gerarRelatorioExcelPagamento(List<Pagamento> pagamentos, OutputStream out) throws IOException {
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Pagamentos");

	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("ID");
	    header.createCell(1).setCellValue("Cliente");
	    header.createCell(2).setCellValue("Forma de Pagamento");
	    header.createCell(3).setCellValue("Valor Pago");
	    header.createCell(4).setCellValue("Data/Hora");

	    int rowNum = 1;
	    for (Pagamento p : pagamentos) {
	        Row row = sheet.createRow(rowNum++);
	        row.createCell(0).setCellValue(p.getId());
	        row.createCell(1).setCellValue(p.getComanda().getCliente().getNome());
	        row.createCell(2).setCellValue(p.getFormaPagamento().name());
	        row.createCell(3).setCellValue(p.getValorPago().toString());
	        row.createCell(4).setCellValue(p.getDataHora().toString());
	    }

	    workbook.write(out);
	    workbook.close();
	}


}
