package de.itemis.salestaxes.main;

import java.math.BigDecimal;

import de.itemis.salestaxes.products.Food;
import de.itemis.salestaxes.products.Medicine;
import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.TaxedProduct;
import de.itemis.salestaxes.purchasing.ShoppingCart;
import de.itemis.salestaxes.purchasing.ShoppingItem;

public class Main {

	public static String INPUT_FILE_PATH = "input1.txt";

	public static String MEDICAL_PRODUCT_FILE_PATH = "medical-products.txt";

	public static String FOOD_PRODUCT_FILE_PATH = "food-products.txt";

	public static void main(String[] args) {

//		ShoppingCart mockCart = createMockShoppingCart();
//		
//		mockCart.printReceipt();

		ShoppingCart cart = Parser.parseShoppingCart(INPUT_FILE_PATH, MEDICAL_PRODUCT_FILE_PATH,
				FOOD_PRODUCT_FILE_PATH);

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
