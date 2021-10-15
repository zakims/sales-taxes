package de.itemis.salestaxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

	private static Parser INSTANCE;

	/**
	 * The name of the file listing the names of all medical products
	 */
	public static String MEDICAL_PRODUCTS_FILE_NAME = "medical-products.txt";

	/**
	 * The name of the file listing the names of all food products
	 */
	public static String FOOD_PRODUCT_FILE_NAME = "food-products.txt";

	/**
	 * List of medical product names used to identify food products in the shopping
	 * cart
	 */
	private List<String> medicalProducts;

	/**
	 * List of food product names used to identify food products in the shopping
	 * cart
	 */
	private List<String> foodProducts;

	
	private Parser() {

		medicalProducts = loadMedicalProducts();

		foodProducts = loadFoodProducts();
	}

	public static Parser getInstance() {

		if (INSTANCE == null)
			INSTANCE = new Parser();

		return INSTANCE;
	}

	/**
	 * Parses the names of the food products from the food products file
	 * 
	 * @return A list of the names of the food products as parsed from the food
	 *         products file
	 */
	private static List<String> loadFoodProducts() {

		InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream(FOOD_PRODUCT_FILE_NAME);

		if (inputStream == null) {

			System.err.println("Food products file cannot be found.");
			System.err.println("Food products will not be correctly identified.");

			return Collections.emptyList();
		}

		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader bufferedReader = new BufferedReader(streamReader);

		Stream<String> lines = bufferedReader.lines();

		return lines.collect(Collectors.toList());

	}

	/**
	 * Parses the names of the medical products from the medical products file
	 * 
	 * @return A list of the names of the medical products as parsed from the
	 *         medical products file
	 */
	private static List<String> loadMedicalProducts() {

		InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream(MEDICAL_PRODUCTS_FILE_NAME);

		if (inputStream == null) {

			System.err.println("Medical products file cannot be found.");
			System.err.println("Medical products will not be correctly identified.");

			return Collections.emptyList();
		}

		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

		BufferedReader bufferedReader = new BufferedReader(streamReader);

		Stream<String> lines = bufferedReader.lines();

		return lines.collect(Collectors.toList());
	}
	
	/**
	 * Parses the contents of the input file and creates a shopping cart containing
	 * all the items
	 * 
	 * @param inputFilePath   Relative path to the file containing the contents of
	 *                        the shopping cart to be parsed
	 * 
	 * @return A shopping cart containing all the items as parsed from the specified
	 *         file
	 */
	public ShoppingCart parseShoppingCart(String inputFilePath) {

		ShoppingCart cart = new ShoppingCart();

		try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {

			lines.map(this::parseShoppingItem).forEach(item -> cart.addItem(item));

		} catch (IOException e) {
			System.err.println("Input file cannot be found at: " + inputFilePath);
			System.err.println("Please provide the correct file path and retry!");
			System.exit(-1);
		}

		return cart;
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
	public ShoppingItem parseShoppingItem(String line) {

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
	private String getProductType(String name) {

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

}
