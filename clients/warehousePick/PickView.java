package clients.warehousePick;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class PickView implements Observer {

  /**
   * Button labels
   */
  class name {
    private static final String PICKED = "Picked";
  }

  // Height of window pixels
  private static final int H = 300;
  // Width of window pixels
  private static final int W = 400;

  private final JLabel theAction = new JLabel();
  private final JLabel theOutLabel = new JLabel();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtPicked = new JButton(name.PICKED);

  private OrderProcessing theOrder;

  private PickController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public PickView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    try {
      // Process order
      theOrder = mf.makeOrderProcessing();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
    // Content Pane
    Container cp = rpc.getContentPane();
    // Root Window
    Container rootWindow = (Container) rpc;
    // No layout manager
    cp.setLayout(null);
    // Size of Window
    rootWindow.setSize(W, H);
    rootWindow.setLocation(x, y);

    // Check Button
    theBtPicked.setBounds(16, 25+60*0, 80, 40);
    // Call back code
    theBtPicked.addActionListener(e -> cont.doPick());
    // Add to canvas
    cp.add(theBtPicked);

    // Message area
    theAction.setBounds(110, 25 , 270, 20);
    // Blank
    theAction.setText("Order details will appear here");
    // Add to canvas
    cp.add(theAction);

    // Scrolling pane
    theSP.setBounds(110, 55, 270, 205);
    // Blank
    theOutput.setText("");
    // Uses font
    theOutput.setFont(new Font("Monospaced",Font.PLAIN,12));
    // Add to canvas
    cp.add(theSP);
    // In TextArea
    theSP.getViewport().add(theOutput, theAction);
    // Make visible
    rootWindow.setVisible(true);
  }
  /**
   * Set the controller
   * @param c The controller object
   */
  public void setController(PickController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args
   */
  @Override
  public void update(Observable modelC, Object arg) {
    PickModel model = (PickModel) modelC;
    String message = (String) arg;
    theAction.setText(message);

    Basket basket =  model.getBasket();
    if (basket != null) {
      theOutput.setText(basket.getDetails());
    } else {
      theOutput.setText("");
    }
  }
}