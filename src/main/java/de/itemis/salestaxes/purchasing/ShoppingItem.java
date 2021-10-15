package de.itemis.salestaxes.purchasing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.itemis.salestaxes.products.Product;

/**
 * A wrapper class for {@link Product}. A product is wrapped inside this class
 * whenever it is added to the {@link ShoppingCart}. It tracks the quantity of
 * the product and it is responsible for calculating its sales taxes and gross
 * price at creation time.
 * 
 * @author Mahmoud Ragab
 */
public class ShoppingItem {

	public static BigDecimal ROUNDING_INCREMENT = new BigDecimal("0.05");

	private Product product;

	/**
	 * The quantity of the product (e.g. number of units) currently in the shopping
	 * cart
	 */
	private int quantity;

	/**
	 * The overall amount of sales taxes on the specified quantity of the wrapped
	 * product
	 */
	private BigDecimal salesTaxes;

	/**
	 * The overall gross price (e.g. including sales taxes) of the specified
	 * quantity of the wrapped product
	 */
	private BigDecimal grossPrice;

	public ShoppingItem(Product product, int quantity) {

		this.product = product;

		this.quantity = quantity;

		calculateTaxesAndGross();
	}

	/**
	 * Calculates the overall amount of sales taxes and gross prices according to
	 * the product and its quantity. This method updates the values of
	 * {@link ShoppingItem#salesTaxes} and {@link ShoppingItem#grossPrice}
	 */
	private void calculateTaxesAndGross() {

		// Calculate sales taxes

		BigDecimal salesTaxesPerUnit = product.getNetPrice().multiply(product.getTaxRate());
		
		salesTaxesPerUnit = roundUpToNearestRoundingIncrement(salesTaxesPerUnit, ROUNDING_INCREMENT);

		salesTaxes = salesTaxesPerUnit.multiply(BigDecimal.valueOf(quantity));
		
		// Calculate grossPrice

		BigDecimal grossPricePerUnit = product.getNetPrice().add(salesTaxesPerUnit);

		grossPrice = grossPricePerUnit.multiply(BigDecimal.valueOf(quantity));

	}

	public BigDecimal getSalesTaxes() {
		return salesTaxes;
	}

	public BigDecimal getGrossPrice() {
		return grossPrice;
	}

	@Override
	public String toString() {

		return String.format(quantity + " " + product + ": %.2f", grossPrice);

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShoppingItem))
			return false;

		ShoppingItem item = (ShoppingItem) obj;

		return (item.quantity == quantity && item.product.equals(product));
	}

	/**
	 * Rounds the value up to the nearest rounding increment
	 * 
	 * @param value     the value to be rounded
	 * @param increment the value to round up to
	 * @return the rounded value
	 */
	private static BigDecimal roundUpToNearestRoundingIncrement(BigDecimal value, BigDecimal increment) {

		return value.divide(increment, 0, RoundingMode.UP).multiply(increment);

	}

}
