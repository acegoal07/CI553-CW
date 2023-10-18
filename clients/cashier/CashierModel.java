package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CashierModel extends Observable {

  private enum State {process, checked}

  // Current state
  private State theState = State.process;
  // Current product
  private Product theProduct = null;
  // Bought items
  private Basket theBasket = null;
  // Product being processed
  private String pn = "";

  private StockReadWriter theStock = null;
  private OrderProcessing theOrder = null;

  /**
   * Construct the model of the Cashier
   * @param mf The factory to create the connection objects
   */
  public CashierModel(MiddleFactory mf) {
    try {
      // Database access
      theStock = mf.makeStockReadWriter();
      // Process order
      theOrder = mf.makeOrderProcessing();
    } catch (Exception e) {
      DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
    }
    // Current state
    theState = State.process;
  }

  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket() {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum) {
    String theAction = "";
    // State process
    theState  = State.process;
    // Product no.
    pn = productNum.trim();
    // & quantity
    int amount  = 1;
    try {
      // Stock Exists?
      if (theStock.exists(pn)) {
        // Get details
        Product pr = theStock.getDetails(pn);
        // In stock?
        if (pr.getQuantity() >= amount) {
          // Display
          theAction = String.format("%s : %7.2f (%2d) ",
            // description
            pr.getDescription(),
            // price
            pr.getPrice(),
            // quantity
            pr.getQuantity()
          );
          // Remember prod.
          theProduct = pr;
          // & quantity
          theProduct.setQuantity(amount);
          // OK await BUY
          theState = State.checked;
        } else {
          // Not in Stock
          theAction =
            pr.getDescription() +" not in stock";
        }
      } else {
        //  product no.
        theAction = "Unknown product number " + pn;
      }
    } catch(StockException e) {
      DEBUG.error( "%s\n%s",
        "CashierModel.doCheck", e.getMessage() );
      theAction = e.getMessage();
    }
    setChanged();
    notifyObservers(theAction);
  }

  /**
   * Buy the product
   */
  public void doBuy() {
    String theAction = "";
    // & quantity
    int amount  = 1;
    try {
       // Not checked
      if (theState != State.checked) {
        //  with customer
        theAction = "Check if OK with customer first";
      } else {
        boolean stockBought = theStock.buyStock(
            theProduct.getProductNum(),
            theProduct.getQuantity()
          );
        // Stock bought
        if (stockBought) {
          //  new Basket
          makeBasketIfReq();
          // Add to bought
          theBasket.add( theProduct );
          // details
          theAction = "Purchased " + theProduct.getDescription();
        } else {
          //  Now no stock
          theAction = "!!! Not in stock";
        }
      }
    } catch(StockException e) {
      DEBUG.error("%s\n%s",  "CashierModel.doBuy", e.getMessage());
      theAction = e.getMessage();
    }
    // All Done
    theState = State.process;
    setChanged();
    notifyObservers(theAction);
  }

  /**
   * Customer pays for the contents of the basket
   */
  public void doBought() {
    String theAction = "";
    // & quantity
    int amount  = 1;
    try {
      // items > 1
      if (theBasket != null && theBasket.size() >= 1) {
        // Process order
        theOrder.newOrder(theBasket);
        // reset
        theBasket = null;
      }
      // New Customer
      theAction = "Next customer";
      // All Done
      theState = State.process;
      theBasket = null;
    } catch(OrderException e) {
      DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
      theAction = e.getMessage();
    }
    theBasket = null;
    setChanged();
    notifyObservers(theAction);
  }

  /**
   * ask for update of view called at start of day
   * or after system reset
   */
  public void askForUpdate() {
    setChanged();
    notifyObservers("Welcome");
  }

  /**
   * make a Basket when required
   */
  private void makeBasketIfReq() {
    if (theBasket == null) {
      try {
        // Unique order num.
        int uon   = theOrder.uniqueNumber();
        // basket list
        theBasket = makeBasket();
        // Add an order number
        theBasket.setOrderNum(uon);
      } catch (OrderException e) {
        DEBUG.error( "Comms failure\n" +
          "CashierModel.makeBasket()\n%s", e.getMessage() );
      }
    }
  }

  /**
   * return an instance of a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket() {
    return new Basket();
  }
}