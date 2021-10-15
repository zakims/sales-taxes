package de.itemis.salestaxes.products;

import java.math.BigDecimal;

public class TaxedProduct extends Product {

	public TaxedProduct(String name, BigDecimal netPrice, String unit, boolean isImported) {
		super(name, netPrice, unit, isImported);
	}

	@Override
	protected BigDecimal getBasicTaxRate() {
		return Product.DEFAULT_BASIC_TAX_RATE;
	}

}
