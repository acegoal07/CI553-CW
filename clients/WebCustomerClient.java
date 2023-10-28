package clients;

import debug.DEBUG;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The Customer Client as an Applet
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class WebCustomerClient extends JApplet {

  private static final long serialVersionUID = 1;
  
  public void init () {
    String supplied = getParameter("stock");
    // URL of stock R
    String stockURL = "".equals(supplied)
      // default  location
      ? Names.STOCK_R
      // supplied location
      : supplied;

    System.out.println("URL " + stockURL);
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    // Create GUI
    displayGUI(mrf);
  }
  /**
   * Create the GUI
   * @param mf The factory to create the objects
   */
  public void displayGUI(MiddleFactory mf) {
    DEBUG.trace("Need to add code");
    //new CustomerGUI(this, mf, 0, 0);
  }
}
