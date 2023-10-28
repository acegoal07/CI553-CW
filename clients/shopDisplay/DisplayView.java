package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The visual display seen by customers (Change to graphical version)
 * Change to a graphical display
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class DisplayView extends Canvas implements Observer {

  // Height of window
  private int H = 300;
  // Width of window
  private int W = 400;
  private String textToDisplay = "";
  private DisplayController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public DisplayView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    // Content Pane
    Container cp = rpc.getContentPane();
    // Root Window
    Container rootWindow = (Container) rpc;
    // Border N E S W CENTER
    cp.setLayout(new BorderLayout());
    // Size of Window
    rootWindow.setSize(W, H);
    // Position on screen
    rootWindow.setLocation(x, y);
    // Add to root window
    rootWindow.add(this, BorderLayout.CENTER);

    // Make visible
    rootWindow.setVisible(true);
  }
  /**
   * Set the controller
   * @param c The controller object
   */
  public void setController(DisplayController c) {
    cont = c;
  }
  /**
   * Called to update the display in the shop
   */
  @Override
  public void update(Observable aModelOfDisplay, Object arg) {
    // Code to update the graphical display with the current
    //  state of the system
    //  Orders awaiting processing
    //  Orders being picked in the 'warehouse.
    //  Orders awaiting collection
    try {
      Map<String, List<Integer>> res = ((DisplayModel) aModelOfDisplay).getOrderState();
      textToDisplay =
        "  Orders in system" + "\n" +
        "  Waiting        : " + listOfOrders(res, "Waiting") +
        "\n"  +
        "  Being picked   : " + listOfOrders(res, "BeingPicked") +
        "\n"  +
        "  To Be Collected: " + listOfOrders(res, "ToBeCollected");
    } catch (OrderException err) {
      textToDisplay = "\n" + "** Communication Failure **";
    }
    // Draw graphically
    repaint();
  }
  /**
   * Update the display
   */
  @Override
  public void update(Graphics g) {
    // Draw information on screen
    drawScreen((Graphics2D) g);
  }
  /**
   * Redraw the screen double buffered
   * @param g Graphics context
   */
  @Override
  public void paint(Graphics g) {
    // Draw information on screen
    drawScreen((Graphics2D) g);
  }

  // Alternate Dimension
  private Dimension theAD;
  // Alternate Image
  private BufferedImage theAI;
  // Alternate Graphics
  private Graphics2D theAG;

  /**
   * Redraw the screen double buffered
   * @param g Graphics context
   */
  public void drawScreen(Graphics2D g) {
    // Size of image
    Dimension d = getSize();

    if (
      (theAG == null) ||
      (d.width  != theAD.width) ||
      (d.height != theAD.height)
    ) {
      theAD = d;
      theAI = (BufferedImage) createImage(d.width, d.height);
      theAG = theAI.createGraphics();
    }
    // draw
    drawActualScreen(theAG);
    g.drawImage(theAI, 0, 0, this);
  }
  /**
   * Redraw the screen
   * @param g Graphics context
   */
  public void drawActualScreen(Graphics2D g) {
    // Paint Colour
    g.setPaint(Color.white);
    // Current size
    W = getWidth();
    H = getHeight();

    g.setFont(new Font("Monospaced", Font.BOLD, 24));
    g.fill(new Rectangle2D.Double(0, 0, W, H));

    // Draw state of system on display
    String[] lines = textToDisplay.split("\n");
    g.setPaint( Color.black );
    for (int i=0; i<lines.length; i++) {
      g.drawString(lines[i], 0, 50 + 50*i);
    }
  }
  /**
   * Return a string of order numbers
   * @param map Contains the current state of the system
   * @param key The key of the list requested
   * @return As a string a list of order numbers.
   */
  private static StringBuilder listOfOrders(Map<String, List<Integer> > map, String key) {
    StringBuilder res = new StringBuilder();
    if (map.containsKey(key)) {
      List<Integer> orders = map.get(key);
      for (Integer i : orders) {
        res.append(" " + i);
      }
    } else {
      res.append("-No key-");
    }
    return res;
  }
}
