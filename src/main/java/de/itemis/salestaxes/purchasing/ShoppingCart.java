package de.itemis.salestaxes.purchasing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A shopping cart that includes all products and their quantities wrapped
 * inside a {@link ShoppingItem}. It takes care of calculating the sales taxes
 * and the order total every time a new item is added to the cart
 * 
 * @author Mahmoud Ragab
 *
 */
public class ShoppingCart {

	/**
	 * An array list of all items currently in the shopping cart
	 */
	private List<ShoppingItem> items;

	/**
	 * The total amount of sales taxes for all items currently in the shopping cart
	 */
	private BigDecimal totalSalesTaxes;

	/**
	 * The total amount to be paid, including sales taxes , for all items currently
	 * in the shopping cart
	 */
	private BigDecimal orderTotal;

	public ShoppingCart() {

		items = new ArrayList<ShoppingItem>();

		totalSalesTaxes = BigDecimal.ZERO;

		orderTotal = BigDecimal.ZERO;
	}

	/**
	 * Adds a new item to the shopping cart and subsequently updates
	 * {@link ShoppingCart#totalSalesTaxes} and {@link ShoppingCart#orderTotal}
	 * 
	 * @param item The shopping item to be added to the cart
	 */
	public void addItem(ShoppingItem item) {

		items.add(item);

		// Accumulate sales tax for this item to the total sales taxes
		totalSalesTaxes = totalSalesTaxes.add(item.getSalesTaxes());

		// Accumulate gross price for this item to the order total
		orderTotal = orderTotal.add(item.getGrossPrice());
	}

	public List<ShoppingItem> getItems() {
		return items;
	}

	public BigDecimal getTotalSalesTaxes() {
		return totalSalesTaxes;
	}

	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	/**
	 * Prints a receipt for the items in the shopping cart including gross price for
	 * each item, the order total (after added taxes) and the total amount of sales
	 * taxes paid
	 */
	public void printReceipt() {

		items.forEach(System.out::println);

		System.out.format("Sales Taxes: %.2f%n", totalSalesTaxes);

		System.out.format("Total: %.2f%n", orderTotal);
	}

}
