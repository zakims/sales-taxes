package de.itemis.salestaxes;

import de.itemis.salestaxes.purchasing.ShoppingCart;

public class Main {

	public static void main(String[] args) {

		ShoppingCart cart = Parser.getInstance().parseShoppingCartFromFile(args[0]);

		cart.printReceipt();

	}

}
