package com.victor.sistemabar.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.victor.sistemabar.model.Comanda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
	
	public void gerarRelatorioPDF(List<Comanda> comandas, OutputStream out) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		
		document.add(new Paragraph("Relatorio de Comandas"));
		document.add(new Paragraph(""));
		
		PdfPTable table = new PdfPTable(3);
		table.addCell("ID");
		table.addCell("Cliente");
		table.addCell("Data");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Comanda comanda : comandas) {
			table.addCell(String.valueOf(comanda.getId()));
			table.addCell(comanda.getCliente().getNome());
			table.addCell(sdf.format(comanda.getData()));
		}
		
		document.add(table);
		document.close();
		
	}
	
	public void gerarRelatorioExcel(List<Comanda> comandas, OutputStream out) throws Exception {
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Relatorio de Comandas");
		
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Cliente");
		headerRow.createCell(2).setCellValue("Data");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		int rowNum = 1;
		for (Comanda comanda : comandas) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(comanda.getId());
			row.createCell(1).setCellValue(comanda.getCliente().getNome());
			row.createCell(2).setCellValue(comanda.getData().format(formatter));
 
		}
		
		for (int i = 0; i < 3; i++) {
			sheet.autoSizeColumn(i);
		}
		
		
		workbook.write(out);
		workbook.close();
		
	}

}
