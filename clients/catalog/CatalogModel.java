package clients.catalog;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;
import java.util.Locale;
import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the catalog client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CatalogModel extends Observable {
  // Current product
  private Product theProduct;
  // Bought items
  private Basket theBasket;
  // Product being processed
  private String pn = "";

  // The interface to the stock object
  private StockReader theStock;
  private OrderProcessing theOrder;
  private ImageIcon thePic;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CatalogModel(MiddleFactory mf) {
    try {
      // Database access
      theStock = mf.makeStockReader();
    } catch (Exception e) {
      DEBUG.error("CustomerModel.constructor\n" + "Database not created?\n%s\n", e.getMessage());
    }
    // Initial Basket
    theBasket = makeBasket();
  }
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket() {
    return theBasket;
  }


  public void doCheckByName(String productName) {
    // Clear s. list
      theBasket.clear();
      // Product name.
      String theAction = "";
      pn = productName;
      // & quantity
      int amount  = 1;
      try {
        // Stock Exists?
          if (theStock.existsByName(pn)) {
            // Product
            Product pr = theStock.getDetailsByName(pn);
            //  In stock?
            if (pr.getQuantity() >= amount) {
              // Display
                theAction =
                  String.format("%s : %7.2f (%2d) ",
                    // description
                    pr.getDescription(),
                    // price
                    pr.getPrice(),
                    // quantity
                    pr.getQuantity()
                    );
                  // Require 1
              pr.setQuantity( amount );
              //   Add to basket
              theBasket.add( pr );
            } else {
              // Inform product not in stock
              theAction = pr.getDescription() + "not in stock";
            }
          } else {
            // Inform Unknown product number
            theAction = "Unknown product number " + pn;
          }
      } catch(StockException e) {
          DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
      }
      setChanged();
      notifyObservers(theAction);
  }

  /**
   * Clear the products from the basket
   */
  public void doClear() {
    String theAction = "";
    // Clear s. list
    theBasket.clear();
    // Set display
    theAction = "Enter Product Number";
    // No picture
    thePic = null;
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */
  public ImageIcon getPicture() {
    return thePic;
  }
  /**
   * ask for update of view called at start
   */
  private void askForUpdate() {
    setChanged();
    // Notify
    notifyObservers("START only");
  }
  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket() {
    return new Basket();
  }
}