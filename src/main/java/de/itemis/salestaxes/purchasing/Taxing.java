package de.itemis.salestaxes.purchasing;

import java.math.BigDecimal;

public class Taxing {

	/**
	 * Basic sales tax rate applied to all products except food, book and medical
	 * products
	 */
	public static BigDecimal DEFAULT_BASIC_TAX_RATE = new BigDecimal("0.10");

	/**
	 * Import duty tax rate applied to all imported products without exceptions
	 */
	public static BigDecimal DEFAULT_IMPORT_TAX_RATE = new BigDecimal("0.05");

	public static BigDecimal ROUNDING_INCREMENT = new BigDecimal("0.05");

}
