package com.victor.sistemabar.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class CodigoBarrasController {

	@GetMapping(value = "/codigo-barras", produces = MediaType.IMAGE_PNG_VALUE)
	public void gerarCodigoBarras(@RequestParam("codigo") String codigo, HttpServletResponse response) {
		try {
			Code128Writer writer = new Code128Writer();
			BitMatrix bitMatrix = writer.encode(codigo, BarcodeFormat.CODE_128, 300, 100);
			try (OutputStream out = response.getOutputStream()) {
				MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
			}
		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	} 
}
