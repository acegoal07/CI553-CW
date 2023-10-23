package catalogue;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products from the CatShop.
 *  used to record the products that are to be/
 *   wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> {

  // Order number
  private int theOrderNum;

  /**
   * Constructor for a basket which is
   * used to represent a customer order / wish list
   */
  public Basket() {
    theOrderNum = 0;
  }
  /**
   * Set the customers unique order number
   * Valid order Numbers 1 .. N
   * @param anOrderNum A unique order number
   */
  public void setOrderNum(int anOrderNum) {
    theOrderNum = anOrderNum;
  }
  /**
   * Returns the customers unique order number
   * @return the customers order number
   */
  public int getOrderNum() {
    return theOrderNum;
  }
  /**
   * Returns a description of the products in the basket suitable for printing.
   * @return a string description of the basket products
   */
  public String getDetails() {
    StringBuilder sb = new StringBuilder(256);
    Formatter fr = new Formatter(sb, Locale.UK);
    String cSign = (Currency.getInstance(Locale.UK)).getSymbol();

    if (theOrderNum != 0) {
      fr.format("Order number: %03d\n", theOrderNum);
      fr.format("\n");
      
      fr.format("%-7s", "ProdID");
      fr.format("%-14.14s", "Description");
      fr.format("%-7s", "Qty");
      fr.format("%-7s", "Price");
      fr.format("\n");
    }

    double total = 0.00;

    if (this.size() > 0) {
      for (Product pr: this) {

        int number = pr.getQuantity();

        fr.format("%-7s", pr.getProductNum() );
        fr.format("%-14.14s", pr.getDescription() );
        fr.format("(%3d)", number );
        fr.format("%s%7.2f", cSign, pr.getPrice() * number );
        fr.format("\n");
        
        total += pr.getPrice() * number;
      }
      fr.format("----------------------------\n");
      fr.format("Total                       ");
      fr.format("%s%7.2f\n", cSign, total );
      fr.close();
    }
    fr.close();
    return sb.toString();
  }
}