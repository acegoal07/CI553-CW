package clients.warehousePick;

import catalogue.Basket;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessing;
import middle.StockReadWriter;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements the Model of the warehouse pick client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class PickModel extends Observable {
  private AtomicReference<Basket> theBasket = new AtomicReference<>();

  private StockReadWriter theStock;
  private OrderProcessing theOrder;
  private String theAction = "";

  private StateOf worker = new StateOf();

  /*
   * Construct the model of the warehouse pick client
   * @param mf The factory to create the connection objects
   */
  public PickModel(MiddleFactory mf) {
    try {
      // Database access
      theStock = mf.makeStockReadWriter();
      // Process order
      theOrder = mf.makeOrderProcessing();
    } catch (Exception e) {
      DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
    }
    // Initial Basket
    theBasket.set(null);
    // Start a background check to see when a new order can be picked
    new Thread(this::checkForNewOrder).start();
  }
  /**
   * Semaphore used to only allow 1 order
   * to be picked at once by this person
   */
  class StateOf {
    private boolean held;
    /**
     * Claim exclusive access
     * @return true if claimed else false
     */
    public synchronized boolean claim() {
      return held ? false : (held = true);
    }
    /**
     * Free the lock
     */
    public synchronized void free() {
      assert held;
      held = false;
    }
  }
  /**
   * Method run in a separate thread to check if there
   * is a new order waiting to be picked and we have
   * nothing to do.
   */
  private void checkForNewOrder() {
    while (true) {
      try {
        // Are we free
        boolean isFree = worker.claim();
        if (isFree) {
          // Order
          Basket sb = theOrder.getOrderToPick();
          // Order to pick
          if (sb != null) {
            // Working on
            theBasket.set(sb);
            // what to do
            theAction = "Order to pick";
          } else {
            worker.free();
            theAction = "";
          }
          setChanged();
          notifyObservers(theAction);
        }
        // idle
        Thread.sleep(1000);
      } catch (Exception e) {
        DEBUG.error("%s\n%s", "BackGroundCheck.run()\n%s", e.getMessage());
      }
    }
  }
  /**
   * Return the Basket of products that are to be picked
   * @return the basket
   */
  public Basket getBasket() {
    return theBasket.get();
  }
  /**
   * Process a picked Order
   */
  public void doPick() {
    String theAction = "";
    try {
      // Basket being picked
      Basket basket = theBasket.get();
      if (basket != null) {
        // Picked
        theBasket.set(null);
        // Order no
        int no = basket.getOrderNum();
        // Tell system
        theOrder.informOrderPicked(no);
        // Inform picker
        theAction = "Order details will appear here";
        // Can pick some more
        worker.free();
      } else {
        // Not picked order
        theAction = "No order to pick";
      }
      setChanged();
      notifyObservers(theAction);
    } catch(OrderException e) {
      DEBUG.error("PickModel.doPick()\n%s\n", e.getMessage());
    }
    setChanged();
    notifyObservers(theAction);
  }
}