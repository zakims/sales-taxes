package de.itemis.salestaxes.products;

import java.math.BigDecimal;

import de.itemis.salestaxes.purchasing.Taxing;

public class TaxedProduct extends Product {

	public TaxedProduct(String name, BigDecimal netPrice, String unit, boolean isImported) {
		super(name, netPrice, unit, isImported);
	}

	@Override
	protected BigDecimal getBasicTaxRate() {
		return Taxing.DEFAULT_BASIC_TAX_RATE;
	}

}
