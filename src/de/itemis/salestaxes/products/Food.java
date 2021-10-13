package de.itemis.salestaxes.products;

import java.math.BigDecimal;

public class Food extends Product {

	public Food(String name, BigDecimal netPrice, String unit, boolean isImported) {
		super(name, netPrice, unit, isImported);
	}

	@Override
	protected BigDecimal getBasicTaxRate() {
		return BigDecimal.ZERO;
	}

}
