package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class BackDoorView implements Observer {
  
  /**
   * Name of buttons
   */
  class name {
    public static final String RESTOCK = "Add";
    public static final String CLEAR = "Clear";
    public static final String QUERY = "Query";
  }

  // Height of window pixels
  private static final int H = 300;
  // Width  of window pixels
  private static final int W = 400;

  private final JLabel theAction = new JLabel();
  private final JLabel theQuantityLabel = new JLabel();
  private final JLabel theOutputLabel = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtClear = new JButton(name.CLEAR);
  private final JButton theBtRStock = new JButton(name.RESTOCK);
  private final JButton theBtQuery = new JButton(name.QUERY);
  private final JSpinner theQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));

  private StockReadWriter theStock;
  private BackDoorController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    try {
      // Database access
      theStock = mf.makeStockReadWriter();
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

    // Font f is
    Font f = new Font("Monospaced",Font.PLAIN,12);

    // Buy button
    theBtQuery.setBounds(16, 25+60*0, 80, 40);
    // Call back code
    theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
    // Add to canvas
    cp.add(theBtQuery);

    // Check Button
    theBtRStock.setBounds(16, 25+60*1, 80, 40);
    // Call back code
    theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), (int) theQuantity.getValue()));
    // Add to canvas
    cp.add(theBtRStock);

    // Buy button
    theBtClear.setBounds(16, 25+60*2, 80, 40);
    // Call back code
    theBtClear.addActionListener(e -> cont.doClear());
    // Add to canvas
    cp.add(theBtClear);

    // Message area
    theAction.setBounds(110, 25 , 270, 20);
    // Blank
    theAction.setText("Enter Product ID");
    // Add to canvas
    cp.add(theAction);

    // Input Area
    theInput.setBounds(110, 50, 222, 40);
    // Blank
    theInput.setText("");
    // Add to canvas
    cp.add(theInput);

    // Amount label 
    theQuantityLabel.setText("Amount");
    // Set Location
    theQuantityLabel.setBounds(330, 15, 50, 40);
    // Added to canvas
    cp.add(theQuantityLabel);
    // Quantity input
    theQuantity.setBounds(330, 50, 50, 40);
    // Add to canvas
    cp.add(theQuantity);

    // Scrolling pane label
    theOutputLabel.setBounds(110, 100, 270, 20);
    // Set text
    theOutputLabel.setText("Restock details");
    // Display
    cp.add(theOutputLabel);
    // Scrolling pane
    theSP.setBounds(110, 120, 270, 130);
    // Blank
    theOutput.setText("");
    // Uses font
    theOutput.setFont(f);
    // Add to canvas
    cp.add(theSP);
    // In TextArea
    theSP.getViewport().add(theOutput);
    // Make visible
    rootWindow.setVisible(true);
    // Focus is here
    theInput.requestFocus();
  }
  /**
   * Set the controller
   * @param c The controller object
   */
  public void setController(BackDoorController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC The observed model
   * @param arg Specific args
   */
  @Override
  public void update(Observable modelC, Object arg) {
    BackDoorModel model = (BackDoorModel) modelC;
    String message = (String) arg;
    theAction.setText(message);

    theOutput.setText(model.getBasket().getDetails());
    theInput.requestFocus();
  }
}