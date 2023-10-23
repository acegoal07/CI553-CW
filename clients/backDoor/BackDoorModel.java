package clients.backDoor;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;
import java.util.Locale;
import java.util.Observable;

/**
 * Implements the Model of the back door client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class BackDoorModel extends Observable {

  // Bought items
  private Basket theBasket;
  // Product being processed
  private String pn = "";
  // Stock list
  private StockReadWriter theStock;

  /*
   * Construct the model of the back door client
   * @param mf The factory to create the connection objects
   */
  public BackDoorModel(MiddleFactory mf) {
    try {
      // Database access
      theStock = mf.makeStockReadWriter();
    } catch (Exception e) {
      DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
    }
    // Initial Basket
    theBasket = makeBasket();
  }
  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket() {
    return theBasket;
  }
  /**
   * Check The current stock level
   * @param productNum The product number
   */
  public void doCheck(String productNum) {
    // Product no.
    pn = productNum.trim();
  }
  /**
   * Query
   * @param productNum The product number of the item
   */
  public void doQuery(String productNum) {
    String theAction = "";
    // Product no.
    pn = productNum.trim();
    // & quantity
    try {
      // Stock Exists?
      if (theStock.exists(pn)) {
        // Product
        Product pr = theStock.getDetails( pn );
        // Display
        theAction = String.format(Locale.UK, "%s : %7.2f (%2d) ",
          // description
          pr.getDescription(),
          // price
          pr.getPrice(),
          // quantity
          pr.getQuantity()
        );
      } else {
        // Inform + product number
        theAction = "Unknown product number " + pn;
      }
    } catch(StockException e) {
      theAction = e.getMessage();
    }
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * Re stock
   * @param productNum The product number of the item
   * @param quantity How many to be added
   */
  public void doRStock(String productNum, int amount) {
    String theAction = "";
    theBasket = makeBasket();
    // Product no.
    pn = productNum.trim();
    try {
      // Stock Exists?
      if (theStock.exists(pn)) {
        // Re stock
        theStock.addStock(pn, amount);
        // Get details
        Product pr = theStock.getDetails(pn);
        // Display
        theBasket.add(pr);
        theAction = "";
      } else {
        // Inform Unknown product number
        theAction = "Unknown product number " + pn;
      }
    } catch(StockException e) {
      theAction = e.getMessage();
    }
    pn = "";
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * Clear the product()
   */
  public void doClear() {
    String theAction = "";
    // Clear s. list
    theBasket.clear();
    // Set display
    theAction = "Enter Product Number";
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * return an instance of a Basket
   * @return a new instance of a Basket
   */
  protected Basket makeBasket() {
    return new Basket();
  }
}