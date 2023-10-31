package clients.customer;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javax.swing.*;

/**
 * The standalone Customer Client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class CustomerClient {
  public static void main (String[] args) {
    // URL of stock default or supplied
    String stockURL = args.length < 1 ? Names.STOCK_R : args[0];

    // Create RMI servers
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    // Create GUI
    displayGUI(mrf);
  }
  /**
   * Create the GUI
   * @param mf The middle factory to use
   */
  private static void displayGUI(MiddleFactory mf) {
    JFrame  window = new JFrame();
    window.setTitle("Customer Client (MVC RMI)" );
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    CustomerModel model = new CustomerModel(mf);
    CustomerView view = new CustomerView(window, mf, 0, 0);
    CustomerController cont = new CustomerController(model, view);
    view.setController(cont);

    // Add observer to the model
    model.addObserver(view);
    // Display Screen
    window.setVisible(true);
  }
}
