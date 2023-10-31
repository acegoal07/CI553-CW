package clients.catalog;

import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the catalog view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CatalogView implements Observer
{
  /**
   * Name of buttons
   */
  class Name {
    public static final String SEARCH = "Search";
    public static final String CLEAR = "Clear";
  }

  // Height of window pixels
  private static final int H = 300;
  // Width  of window pixels
  private static final int W = 400;

  // The components of the window
  private final JLabel theAction = new JLabel();
  private final JLabel theOutputLabel = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtCheck = new JButton(Name.SEARCH);
  private final JButton theBtClear = new JButton(Name.CLEAR);

  // The model of the customer
  private StockReader theStock;
  private CatalogController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public CatalogView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
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
    theBtCheck.addActionListener(e -> {
      cont.doCheckByName(theInput.getText());
      theInput.setText("");
    });
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
    theAction.setText("Enter search term");
    // Add to canvas
    cp.add(theAction);

    // Input Area
    theInput.setBounds(110, 50, 270, 40);
    // Blank
    theInput.setText("");
    // Add to canvas
    cp.add(theInput);

    // Scrolling pane label
    theOutputLabel.setBounds(110, 100, 270, 20);
    // Set text
    theOutputLabel.setText("Search results");
    // Display
    cp.add(theOutputLabel);
    // Scrolling pane
    theSP.setBounds(110, 120, 270, 130);
    // Blank
    theOutput.setText("");
    // Uses font
    theOutput.setFont(new Font("Monospaced",Font.PLAIN,12));
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
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */
  public void setController(CatalogController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC The observed model
   * @param arg Specific args
   */
  public void update(Observable modelC, Object arg) {
    CatalogModel model = (CatalogModel) modelC;
    String message = (String) arg;
    theAction.setText(message);
    theOutput.setText(model.getBasket().getDetails());
    // Focus is here
    theInput.requestFocus();
  }
}