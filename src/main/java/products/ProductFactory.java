package main.java.products;

import java.math.BigDecimal;

public class ProductFactory {

	private static ProductFactory INSTANCE;

	private ProductFactory() {
	}

	public static synchronized ProductFactory getInstance() {

		if (INSTANCE == null)
			INSTANCE = new ProductFactory();

		return INSTANCE;
	}

	public Product getProduct(String type, String name, BigDecimal price, String unit, boolean isImported) {

		if (type == null)
			return null;

		if (type.equalsIgnoreCase("medicine")) {
			return new Medicine(name, price, unit, isImported);
		} else if (type.equalsIgnoreCase("food")) {
			return new Food(name, price, unit, isImported);
		} else if (type.equalsIgnoreCase("book")) {
			return new Book(name, price, unit, isImported);
		} else if (type.equalsIgnoreCase("taxed")) {
			return new TaxedProduct(name, price, unit, isImported);
		}

		return null;
	}

}
