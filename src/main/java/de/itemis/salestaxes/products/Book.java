package de.itemis.salestaxes.products;

import java.math.BigDecimal;

public class Book extends Product {

	public Book(String name, BigDecimal netPrice, String unit, boolean isImported) {
		super(name, netPrice, unit, isImported);
	}

	@Override
	protected BigDecimal getBasicTaxRate() {
		return BigDecimal.ZERO;
	}

}
