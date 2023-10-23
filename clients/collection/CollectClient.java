package clients.collection;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javax.swing.*;

/**
 * The standalone Collection Client.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class CollectClient {
  public static void main (String[] args) {
    // URL of stock RW
    String stockURL = args.length < 1
      // default  location
      ? Names.STOCK_RW
      // supplied location
      : args[0];
    // URL of order
    String orderURL = args.length < 2
    // default  location
      ? Names.ORDER
      // supplied location
      : args[1];

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo(stockURL);
    mrf.setOrderInfo  (orderURL);
    // Create GUI
    displayGUI(mrf);
  }

  /**
   * Create the GUI
   * @param mf The factory to create the connection objects
   */
  private static void displayGUI(MiddleFactory mf) {
    JFrame  window = new JFrame();

    window.setTitle("Collection Client (MVC RMI)");
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    CollectModel model = new CollectModel(mf);
    CollectView view  = new CollectView(window, mf, 0, 0);
    CollectController cont  = new CollectController(model, view);

    view.setController(cont);
    // Add observer to the model
    model.addObserver(view);
    // Display Screen
    window.setVisible(true);
  }
}