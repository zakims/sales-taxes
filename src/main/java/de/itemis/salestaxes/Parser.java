package de.itemis.salestaxes;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.itemis.salestaxes.products.Product;
import de.itemis.salestaxes.products.ProductFactory;
import de.itemis.salestaxes.purchasing.ShoppingCart;
import de.itemis.salestaxes.purchasing.ShoppingItem;

public class Parser {

	/**
	 * Relative path to the file containing names of all medical products.
	 */
	private static List<String> medicalProducts;

	/**
	 * Relative path to the file containing names of all food products.
	 */
	private static List<String> foodProducts;

	/**
	 * Parses the contents of the input file and creates a shopping cart containing
	 * all the items
	 * 
	 * @param inputFilePath   Relative path to the file containing the contents of
	 *                        the shopping cart to be parsed
	 * 
	 * @param medicalFilePath Relative path to the file containing names of all
	 *                        medical products, used to identify food products in
	 *                        the shopping cart
	 * 
	 * @param foodFilePath    Relative path to the file containing names of all food
	 *                        products, used to identify food products in the
	 *                        shopping cart
	 * 
	 * @return A shopping cart containing all the items as parsed from the specified
	 *         file
	 */
	public static ShoppingCart parseShoppingCart(String inputFilePath, String medicalFilePath, String foodFilePath) {

		ShoppingCart cart = new ShoppingCart();

		// This method has to be called before parsing any shopping item from the file
		// because it is needed to identify the product type while parsing
		loadFoodAndMedicine(medicalFilePath, foodFilePath);

		try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {

			lines.map(Parser::parseShoppingItem).forEach(item -> cart.addItem(item));

		} catch (IOException e) {
			System.err.println("Input file cannot be found at: " + inputFilePath);
			System.err.println("Please provide the correct file path and retry!");
			System.exit(-1);
		}

		return cart;
	}

	/**
	 * Parses the contents of the medical and food product names from their
	 * respective files and stores them in {@link #medicalProducts} and
	 * {@link #foodProducts} respectively
	 * 
	 * @param medicalFilePath Relative path to the file containing names of all
	 *                        medical products, used to identify food products in
	 *                        the shopping cart
	 * 
	 * @param foodFilePath    Relative path to the file containing names of all food
	 *                        products, used to identify food products in the
	 *                        shopping cart
	 * 
	 */
	private static void loadFoodAndMedicine(String medicalFilePath, String foodFilePath) {

		medicalProducts = getMedicalProducts(medicalFilePath);

		foodProducts = getFoodProducts(foodFilePath);

	}

	/**
	 * Parses one line from the shopping cart input file (the file that specifies
	 * the content of the shopping cart). A line is expected to have a predefined
	 * format <strong>Quantity | imported? | Sales Unit | "of" | Name | "at" | Price
	 * </strong>
	 * 
	 * @param line A string representing a single line read from the shopping cart
	 *             input file
	 * 
	 * @return a shopping item as parse from the specified line
	 */
	private static ShoppingItem parseShoppingItem(String line) {

		List<String> words = Arrays.asList(line.split(" "));

		int quantity = Integer.parseInt(words.get(0));

		// A line ends with the net price of a single unit of the product
		BigDecimal price = new BigDecimal(words.get(words.size() - 1));

		// If the keyword imported exists, then the product is imported
		boolean isImported = words.contains("imported");

		// If the product has a special unit then it should be followed by "of"
		boolean hasUnit = words.contains("of");
		String unit = hasUnit ? words.get(words.indexOf("of") - 1) : "";

		// Parse product name given that it comes exactly after "imported" & unit
		// and immediately before price
		int nameStart = getProductNameStartIndex(isImported, hasUnit);
		List<String> nameAsList = words.subList(nameStart, words.size() - 2);
		String name = nameAsList.stream().collect(Collectors.joining(" "));

		// Infer product type from product name.
		String type = getProductType(name);

		// Create product from parsed data and add it to the shopping cart
		Product p = ProductFactory.getInstance().getProduct(type, name, price, unit, isImported);

		ShoppingItem item = new ShoppingItem(p, quantity);

		return item;
	}

	/**
	 * 
	 * Helper method for parsing used to get index of the first word of the product
	 * name. The method requires knowledge of whether product is imported and
	 * whether it has a unit, because product name comes immediately after
	 * "imported" & unit if applicable.
	 * 
	 * @return The index of the first word of the product name
	 */
	private static int getProductNameStartIndex(boolean isImported, boolean hasUnit) {

		// If product is not imported and has no unit then the name starts at index 1
		// after quantity
		int startIndex = 1;

		// If product is imported, the start index is incremented by 1 (e.g. comes after
		// "imported")
		if (isImported)
			startIndex += 1;

		// If product has a special sales unit, the start index is incremented by 2
		// (e.g. comes after "bottle of")
		if (hasUnit)
			startIndex += 2;

		return startIndex;
	}

	/**
	 * If name is listed in food-products.txt: it is of type Food If name is listed
	 * in medical-products.txt: it is of type Medicine If none of the above: it is
	 * other type which should be taxed
	 * 
	 * @param name The product name as parsed from the input file
	 * @return The inferred product type
	 */
	private static String getProductType(String name) {

		// If the product name contains “book”: it is assumed to be of type Book.
		if (name.contains("book"))
			return "book";

		// If name is listed in the food products file, it is assumed to be of type Food
		if (foodProducts.contains(name))
			return "food";

		// If name is listed in the medical products file, it is assumed to be of type
		// Food
		if (medicalProducts.contains(name))
			return "medicine";

		// If the product is not book, food, or medical product, it is assumed to be
		// another type which is taxed
		return "taxed";
	}

	/**
	 * Parses the names of the food products from the specified file
	 * 
	 * @param filePath Relative path to the file containing the names of all food
	 *                 products
	 * @return A list of the names of the food products as parsed from the file
	 */
	private static List<String> getFoodProducts(String filePath) {

		Path path = Paths.get(filePath);

		try (Stream<String> lines = Files.lines(path)) {

			return lines.collect(Collectors.toList());

		} catch (IOException e) {
			System.err.println("Food products file cannot be found at: " + filePath);
			System.err.println("Food products will not be correctly identified");
		}

		return Collections.emptyList();
	}

	/**
	 * Parses the names of the medical products from the specified file
	 * 
	 * @param filePath Relative path to the file containing the names of all medical
	 *                 products
	 * @return A list of the names of the medical products as parsed from the file
	 */
	private static List<String> getMedicalProducts(String filePath) {

		Path path = Paths.get(filePath);

		try (Stream<String> lines = Files.lines(path)) {

			return lines.collect(Collectors.toList());

		} catch (IOException e) {
			System.err.println("Medical products file cannot be found at: " + filePath);
			System.err.println("Medical products will not be correctly identified");
		}

		return Collections.emptyList();
	}

}
