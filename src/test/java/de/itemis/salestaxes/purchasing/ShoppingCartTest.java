package de.itemis.salestaxes.purchasing;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.itemis.salestaxes.products.Food;
import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.TaxedProduct;

class ShoppingCartTest {

	@Test
	void testAddItem() {

		ShoppingCart cart = new ShoppingCart();

		Product importedPerfume = new TaxedProduct("perfume", new BigDecimal("47.50"), "bottle", true);

		ShoppingItem item = new ShoppingItem(importedPerfume, 1);

		cart.addItem(item);

		Assertions.assertEquals(cart.getItems().size(), 1, "Failed to add items to cart");

	}

	@Test
	void testTotalTaxCalculation() {

		ShoppingCart cart = new ShoppingCart();

		Product importedChocolates = new Food("chocolates", new BigDecimal("10.00"), "box", true);
		cart.addItem(new ShoppingItem(importedChocolates, 1));

		Product importedPerfume = new TaxedProduct("perfume", new BigDecimal("47.50"), "bottle", true);
		cart.addItem(new ShoppingItem(importedPerfume, 1));

		Assertions.assertEquals(new BigDecimal("7.65"), cart.getTotalSalesTaxes(),
				"Failed to calculate correct total sales tax for a shopping cart");
	}

	@Test
	void testOrderTotalCalculation() {

		ShoppingCart cart = new ShoppingCart();

		Product importedPerfume = new TaxedProduct("perfume", new BigDecimal("47.50"), "bottle", true);
		cart.addItem(new ShoppingItem(importedPerfume, 1));

		Product importedChocolates = new Food("chocolates", new BigDecimal("10.00"), "box", true);
		cart.addItem(new ShoppingItem(importedChocolates, 1));

		Assertions.assertEquals(new BigDecimal("65.15"), cart.getOrderTotal(),
				"Failed to calculate correct order total for a shopping cart");
	}

}
