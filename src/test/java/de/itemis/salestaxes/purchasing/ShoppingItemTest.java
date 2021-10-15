package de.itemis.salestaxes.purchasing;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.TaxedProduct;

class ShoppingItemTest {

	@Test
	void testGetSalesTaxes() {

		Product importedPerfume = new TaxedProduct("perfume", new BigDecimal("47.50"), "bottle", true);

		ShoppingItem item = new ShoppingItem(importedPerfume, 1);

		Assertions.assertEquals(item.getSalesTaxes(), new BigDecimal("7.15"),
				"sales taxes calculation for item failed");
	}

	@Test
	void testGetGrossPrice() {
		Product importedPerfume = new TaxedProduct("perfume", new BigDecimal("47.50"), "bottle", true);

		ShoppingItem item = new ShoppingItem(importedPerfume, 1);

		Assertions.assertEquals(item.getGrossPrice(), new BigDecimal("54.65"),
				"gross price calculation for item failed");
	}

}
