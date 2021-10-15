package de.itemis.salestaxes;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.itemis.salestaxes.products.Book;
import de.itemis.salestaxes.products.Food;
import de.itemis.salestaxes.products.Medicine;
import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.TaxedProduct;
import de.itemis.salestaxes.purchasing.ShoppingCart;
import de.itemis.salestaxes.purchasing.ShoppingItem;

class ParserTest {

	@Test
	void testParseShoppingCart() {

		ShoppingCart actual = Parser.getInstance().parseShoppingCartFromFile("test.txt");

		ShoppingCart expected = createExpectedShopiingCart();

		Assertions.assertEquals(expected, actual, "Failed to parse a test input file to a shopping cart");

	}

	private static ShoppingCart createExpectedShopiingCart() {

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

	@Test
	void testParseFood() {

		ShoppingItem actual = Parser.getInstance().parseShoppingItem("1 chocolate bar at 55.99");

		ShoppingItem expected = new ShoppingItem(new Food("chocolate bar", new BigDecimal("55.99"), "", false), 1);

		Assertions.assertEquals(expected, actual, "Failed to identify food product");

	}

	@Test
	void testParseMedicine() {

		ShoppingItem actual = Parser.getInstance().parseShoppingItem("1 headache pills at 15.99");

		ShoppingItem expected = new ShoppingItem(new Medicine("headache pills", new BigDecimal("15.99"), "", false), 1);

		Assertions.assertEquals(expected, actual, "Failed to identify medical product");

	}

	@Test
	void testParseBook() {

		ShoppingItem actual = Parser.getInstance().parseShoppingItem("1 harry potter book at 15.99");

		ShoppingItem expected = new ShoppingItem(new Book("harry potter book", new BigDecimal("15.99"), "", false), 1);

		Assertions.assertEquals(expected, actual, "Failed to identify book product");

	}

	@Test
	void testParseTaxedProduct() {

		ShoppingItem actual = Parser.getInstance().parseShoppingItem("1 perfume at 15.99");

		ShoppingItem expected = new ShoppingItem(new TaxedProduct("perfume", new BigDecimal("15.99"), "", false), 1);

		Assertions.assertEquals(expected, actual, "Failed to identify a taxed product");

	}

}
