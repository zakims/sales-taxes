package main.java.products;

import java.math.BigDecimal;

public class Medicine extends Product {
	
	public Medicine(String name, BigDecimal netPrice, String unit, boolean isImported) {
		super(name, netPrice, unit, isImported);
	}

	@Override
	protected BigDecimal getBasicTaxRate() {
		return BigDecimal.ZERO;
	}

}
