package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReadWriter;
import java.util.Locale;
import java.util.Observable;

/**
 * Implements the Model of the cashier client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CashierModel extends Observable {

  // Possible states of the client
  private enum State {process, checked}

  // Current state
  private State theState = State.process;
  // Current product
  private Product theProduct;
  // Bought items
  private Basket theBasket;
  // Product being processed
  private String pn = "";

  // The data for the current product
  private StockReadWriter theStock;
  private OrderProcessing theOrder;

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
   * @param productQuantity The quantity of the product
   */
  public void doCheck(String productNum, int productQuantity) {
    String theAction = "";
    // State process
    theState = State.process;
    // Product no.
    pn = productNum.trim();
    // & quantity
    int amount = productQuantity;
    try {
      // Stock Exists?
      if (theStock.exists(pn)) {
        // Get details
        Product pr = theStock.getDetails(pn);
        // In stock?
        if (pr.getQuantity() >= amount) {
          // Display
          theAction = String.format(Locale.UK, "%s : %7.2f (%2d) ",
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
      DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
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
          theBasket.add(theProduct);
          // details
          theAction = "Purchased " + theProduct.getDescription();
        } else {
          //  Now no stock
          theAction = "!!! Not in stock";
        }
      }
    } catch(StockException e) {
      DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
      theAction = e.getMessage();
    }
    // All Done
    theState = State.process;
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * Cancel the sale
   */
  public void doCancel() {
    String theAction = "";
    try {
      // items > 1
      if (theBasket != null && !theBasket.isEmpty()) {
        // Cancel order
        for (Product pr : theBasket) {
          theStock.addStock(pr.getProductNum(), pr.getQuantity());
        }
        // reset
        theBasket = null;
        // New Customer
        theAction = "Next customer";
      } else {
        // no items
        theAction = "No items to cancel";
      }
    } catch(StockException e) {
      DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
      theAction = e.getMessage();
    }
    // All done
    theState = State.process;
    setChanged();
    notifyObservers(theAction);
  }
  /**
   * Customer pays for the contents of the basket
   */
  public void doBought() {
    String theAction = "";
    try {
      // items > 1
      if (theBasket != null && !theBasket.isEmpty()) {
        // Process order
        theOrder.newOrder(theBasket);
        // reset
        theBasket = null;
        // New Customer
        theAction = "Next customer";
      } else {
        //  no items
        theAction = "No items bought";
      }
      // All Done
      theState = State.process;
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
    notifyObservers("Enter product ID");
  }
  /**
   * make a Basket when required
   */
  private void makeBasketIfReq() {
    if (theBasket == null) {
      try {
        // Unique order num.
        int uon = theOrder.uniqueNumber();
        // basket list
        theBasket = makeBasket();
        // Add an order number
        theBasket.setOrderNum(uon);
      } catch (OrderException e) {
        DEBUG.error( "Comms failure\n" +
          "CashierModel.makeBasket()\n%s", e.getMessage());
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