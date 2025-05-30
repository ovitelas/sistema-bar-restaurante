package com.victor.sistemabar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FiltroRelatorioDTO {

	private LocalDate dataInicio;
	private LocalDate dataFim;
	private String nomeCliente;
	
}
