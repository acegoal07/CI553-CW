package clients.cashier;

import catalogue.*;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javax.swing.*;

/**
 * The standalone Cashier Client.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */


public class CashierClient {
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
      //  supplied location
      : args[1];

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo(stockURL);
    mrf.setOrderInfo(orderURL);
    // Create GUI
    displayGUI(mrf);
  }
  /**
   * Create the GUI
   * @param mf The middle factory to use
   */
  private static void displayGUI(MiddleFactory mf) {
    JFrame  window = new JFrame();

    window.setTitle("Cashier Client (MVC RMI)");
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    CashierModel model = new CashierModel(mf);
    CashierView view = new CashierView(window, mf, 0, 0);
    CashierController cont = new CashierController(model, view);
    view.setController(cont);

    // Add observer to the model
    model.addObserver(view);
    // Display Screen
    window.setVisible(true);
    model.askForUpdate();
  }
}