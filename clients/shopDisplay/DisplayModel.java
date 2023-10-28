package clients.shopDisplay;

import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessing;

import java.util.List;
import java.util.Map;
import java.util.Observable;

// File is complete but not optimal
//  Will force update of display every 1 seconds
//  Could be clever & only ask for an update of the display
//   if it really has changed

/**
 * Implements the Model of the display client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DisplayModel extends Observable {
  // The data for the display
  private OrderProcessing theOrder;

  /**
   * Set up initial connection to the order processing system
   * @param mf Factory to return an object to access the order processing system
   */
  public DisplayModel(MiddleFactory mf) {
    try {
      // Process order
      theOrder = mf.makeOrderProcessing();
    } catch (Exception e) {
      // Serious error in system (Should not occur)
      DEBUG.error("ModelOfDisplay: " + e.getMessage());
    }
    new Thread(this::backgroundRun).start();
  }
  /**
   * Run as a thread in background to continually update the display
   */
  public void backgroundRun() {
    // Forever
    while (true) {
      try {
        Thread.sleep(1000);
        DEBUG.trace("ModelOfDisplay call view");
        setChanged();
        notifyObservers();
      } catch (InterruptedException e) {
        DEBUG.error("%s\n%s\n",  "ModelOfDisplay.run()", e.getMessage());
      }
    }
  }
  /**
   * Return the current state of the order processing system
   * @return Map of order state
   * @throws OrderException if communication error
   */
  public synchronized Map<String, List<Integer> > getOrderState() throws OrderException {
    return theOrder.getOrderState();
  }
}