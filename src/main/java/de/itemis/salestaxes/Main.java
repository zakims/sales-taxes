package de.itemis.salestaxes;

import java.math.BigDecimal;

import de.itemis.salestaxes.products.Food;
import de.itemis.salestaxes.products.Medicine;
import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.TaxedProduct;
import de.itemis.salestaxes.purchasing.ShoppingCart;
import de.itemis.salestaxes.purchasing.ShoppingItem;

public class Main {

	public static void main(String[] args) {

		ShoppingCart cart = Parser.getInstance().parseShoppingCart(args[0]);

		cart.printReceipt();

	}

	@SuppressWarnings("unused")
	private static ShoppingCart createMockShoppingCart() {

		ShoppingCart cart = new ShoppingCart();

		Product p1 = new TaxedProduct("perfume", new BigDecimal("27.99"), "bottle", true);
		cart.addItem(new ShoppingItem(p1, 1));

		Product p2 = new TaxedProduct("perfume", new BigDecimal("18.99"), "bottle", false);
		cart.addItem(new ShoppingItem(p2, 1));

		Product p3 = new Medicine("headache pills", new BigDecimal("9.75"), "packet", false);
		cart.addItem(new ShoppingItem(p3, 1));

		Product p4 = new Food("chocolates", new BigDecimal("11.25"), "box", true);
		cart.addItem(new ShoppingItem(p4, 1));

		return cart;
	}

}
