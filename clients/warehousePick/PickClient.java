package clients.warehousePick;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone warehouse Pick Client.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class PickClient {
  public static void main (String[] args) {
    // URL of stock default or supplied
    String stockURL = args.length < 1 ? Names.STOCK_RW : args[0];
    // URL of order default or supplied
     String orderURL = args.length < 2 ? Names.ORDER : args[1];

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo(stockURL);
    mrf.setOrderInfo(orderURL);
    // Create GUI
    displayGUI(mrf);
  }
  /**
   * Create the GUI
   * @param mf The factory to create the order and stock objects
   */
  public static void displayGUI(MiddleFactory mf) {
    JFrame  window = new JFrame();

    window.setTitle( "Pick Client (RMI MVC)");
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    PickModel model = new PickModel(mf);
    PickView view = new PickView(window, mf, 0, 0);
    PickController cont = new PickController(model, view);
    view.setController( cont );

    // Add observer to the model
    model.addObserver(view);
    // Display Screen
    window.setVisible(true);
  }
}
