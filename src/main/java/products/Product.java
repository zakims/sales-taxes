package main.java.products;

import java.math.BigDecimal;

import main.java.purchasing.Taxing;

/**
 * An abstract product class which contains all basic fields and methods of a
 * product. Subclasses shall implement {@link #getBasicTaxRate()} to return any
 * arbitrary basic tax rate dependent on the product type
 * 
 * @author Mahmoud Ragab
 *
 */
public abstract class Product {

	/**
	 * Shelf name of the product
	 */
	private String name;

	/**
	 * Net Price of the product before sales taxes
	 */
	private BigDecimal netPrice;

	/**
	 * Sale unit of the product
	 */
	private String unit;

	/**
	 * A flag representing if the product is imported (e.g. import duties are added
	 * to sales taxes)
	 */
	private boolean isImported;

	public Product(String name, BigDecimal netPrice, String unit, boolean isImported) {
		this.name = name;
		this.netPrice = netPrice;
		this.unit = unit;
		this.isImported = isImported;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public boolean isImported() {
		return isImported;
	}

	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * This method should be overridden by subclasses
	 * 
	 * @return basic tax rate
	 */
	abstract protected BigDecimal getBasicTaxRate();

	/**
	 * Get the import tax rate if applicable (e.g. product is imported), 0 otherwise
	 * 
	 * @return Import tax rate
	 */
	private BigDecimal getImportTaxRate() {
		return isImported ? Taxing.DEFAULT_IMPORT_TAX_RATE : BigDecimal.ZERO;
	}

	/**
	 * Gets the total sales tax rate including both basic sales taxes and import
	 * duties if applicable
	 * 
	 * @return total sales tax rate
	 */
	public BigDecimal getTaxRate() {

		BigDecimal totalTaxRate = getBasicTaxRate().add(getImportTaxRate());

		return totalTaxRate;
	}

	@Override
	public String toString() {

		String unitOfLiteral = unit.isBlank() ? "" : (unit + " of ");
		
		String importedLiteral = isImported ? "imported " : "";

		return importedLiteral + unitOfLiteral + name;

	}

}
