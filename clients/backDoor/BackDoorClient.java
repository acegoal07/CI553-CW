package clients.backDoor;

import javax.swing.*;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

/**
 * The standalone BackDoor Client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class BackDoorClient {
  public static void main (String[] args) {

    // URL of stock RW default or supplied location
    String stockURL = args.length < 1 ? Names.STOCK_RW : args[0];
      
    // URL of order default or supplied location
    String orderURL = args.length < 2 ? Names.ORDER : args[1];

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo(stockURL);
    mrf.setOrderInfo(orderURL);
    // Create GUI
    displayGUI(mrf);
  }
  /**
   * Create the GUI
   * @param mf The factory to create the connection objects
   */
  private static void displayGUI(MiddleFactory mf) {
    JFrame window = new JFrame();

    window.setTitle("BackDoor Client (MVC RMI)");
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    BackDoorModel model = new BackDoorModel(mf);
    BackDoorView view = new BackDoorView(window, mf, 0, 0);
    BackDoorController cont = new BackDoorController(model, view);
    view.setController(cont);

    // Add observer to the model
    model.addObserver(view);
    // Display Screen
    window.setVisible(true);
  }
}
