package com.victor.sistemabar.util;

import java.util.UUID;

public class CodigoBarrasUtil {

	public static String gerarCodigoBarras() {
		return UUID.randomUUID().toString().substring(0, 12).replace("-", "").toUpperCase();
	}
	
}
