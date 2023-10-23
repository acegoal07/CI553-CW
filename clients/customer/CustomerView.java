package clients.customer;

import catalogue.Basket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CustomerView implements Observer
{
  /**
   * Name of buttons
   */
  class Name {
    public static final String CHECK = "Check";
    public static final String CLEAR = "Clear";
  }

  // Height of window pixels
  private static final int H = 300;
  // Width  of window pixels
  private static final int W = 400;

  // The components of the window
  private final JLabel theAction = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtCheck = new JButton(Name.CHECK);
  private final JButton theBtClear = new JButton(Name.CLEAR);
  private final JSpinner theQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

  // The picture of the product
  private Picture thePicture = new Picture(80,80);

  // The model of the customer
  private StockReader theStock;
  private CustomerController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    try {
      // Database Access
      theStock = mf.makeStockReader();
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

    // Check button
    theBtCheck.setBounds(16, 25+60*0, 80, 40);
    // Call back code
    theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText(), (int) theQuantity.getValue()));
    // Add to canvas
    cp.add(theBtCheck);

    // Clear button
    theBtClear.setBounds(16, 25+60*1, 80, 40);
    // Call back code
    theBtClear.addActionListener(e -> {
      cont.doClear();
      theInput.setText("");
    });
      // Add to canvas
    cp.add(theBtClear);

    // Message area
    theAction.setBounds(110, 25 , 270, 20);
    // Blank
    theAction.setText("");
    // Add to canvas
    cp.add(theAction);

    // Input Area
    theInput.setBounds(110, 50, 222, 40);
    // Blank
    theInput.setText("");
    // Add to canvas
    cp.add(theInput);

    // Amount spinner
    theQuantity.setBounds(330, 50, 50, 40);
    // Add to canvas
    cp.add(theQuantity);

    // Scrolling pane
    theSP.setBounds(110, 100, 270, 160);
    // Blank
    theOutput.setText("");
    // Uses font
    theOutput.setFont(new Font("Monospaced",Font.PLAIN,12));
    // Add to canvas
    cp.add(theSP);
    // In TextArea
    theSP.getViewport().add(theOutput);

    // Picture area
    thePicture.setBounds(16, 25+60*2, 80, 80);
    // Add to canvas
    cp.add(thePicture);
    thePicture.clear();

    // Make visible
    rootWindow.setVisible(true);
    // Focus is here
    theInput.requestFocus();
  }
  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */
  public void setController(CustomerController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC The observed model
   * @param arg Specific args
   */
  public void update(Observable modelC, Object arg) {
    CustomerModel model = (CustomerModel) modelC;
    String message = (String) arg;
    theAction.setText(message);
    // Image of product
    ImageIcon image = model.getPicture();
    if (image == null) {
      // Clear picture
      thePicture.clear();
    } else {
      // Display picture
      thePicture.set(image);
    }
    theOutput.setText(model.getBasket().getDetails());
    // Focus is here
    theInput.requestFocus();
  }
}