package catalogue;

import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price and
 * Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class Product implements Serializable {
  
  // Product number
  private String theProductNum;
  // Description of product
  private String theDescription;
  // Price of product
  private double thePrice;
  // Quantity involved
  private int theQuantity;

  /**
   * Construct a product details
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice The price of the product
   * @param aQuantity The Quantity of the product involved
   */
  public Product(String aProductNum, String aDescription, double aPrice, int aQuantity) {
    // Product number
    theProductNum = aProductNum;
    // Description of product
    theDescription = aDescription;
    // Price of product
    thePrice = aPrice;
    // Quantity involved
    theQuantity = aQuantity;
  }
  /**
   * Return the product number
   * @return The product number
   */
  public String getProductNum() {
    return theProductNum;
  }
  /**
   * Return the description of the product
   * @return The description of the product
   */
  public String getDescription() {
    return theDescription;
  }
  /**
   * Return the price of the product
   * @return The price of the product
   */
  public double getPrice() {
    return thePrice;
  }
  /**
   * Return the quantity of the product involved
   * @return The quantity of the product involved
   */
  public int getQuantity() {
    return theQuantity;
  }
  /**
   * Set the product number
   * @param aProductNum The product number
   */
  public void setProductNum(String aProductNum) {
    theProductNum = aProductNum;
  }
  /**
   * Set the description of the product
   * @param aDescription The description of the product
   */
  public void setDescription(String aDescription) {
    theDescription = aDescription;
  }
  /**
   * Set the price of the product
   * @param aPrice The price of the product
   */
  public void setPrice(double aPrice) {
    thePrice = aPrice;
  }
  /**
   * Set the quantity of the product involved
   * @param aQuantity The quantity of the product involved
   */
  public void setQuantity(int aQuantity) {
    theQuantity = aQuantity;
  }
}