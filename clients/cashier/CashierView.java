package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


/**
 * View of the model
 * @author  M A Smith (c) June 2014
 */
public class CashierView implements Observer {
  // Height of window pixels
  private static final int H = 300;
  // Width  of window pixels
  private static final int W = 400;

  /**
   * Name of buttons
   */
  class name {
    public static final String CHECK = "Check";
    public static final String BUY = "Buy";
    public static final String BOUGHT = "Bought";
    public static final String CANCEL = "Cancel";
  }

  private final JLabel theAction = new JLabel();
  private final JLabel theQuantityLabel = new JLabel();
  private final JLabel theOutputLabel = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtCheck = new JButton(name.CHECK);
  private final JButton theBtBuy = new JButton(name.BUY);
  private final JButton theBtCancel = new JButton(name.CANCEL);
  private final JButton theBtBought= new JButton(name.BOUGHT);
  private final JSpinner theQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

  private StockReadWriter theStock;
  private OrderProcessing theOrder;
  private CashierController cont;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    try {
      // Database access
      theStock = mf.makeStockReadWriter();
      // Process order
      theOrder = mf.makeOrderProcessing();
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage() );
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
    theBtCheck.setBounds(16, 25+60*0, 80, 40);
    // Call back code
    theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText(), (int) theQuantity.getValue()));
    // Add to canvas
    cp.add(theBtCheck);

    // Buy button
    theBtBuy.setBounds(16, 25+60*1, 80, 40);
    // Call back code
    theBtBuy.addActionListener(e -> {
      cont.doBuy();
      theInput.setText("");
      theQuantity.setValue(1);
    });
    // Add to canvas
    cp.add(theBtBuy);
    
    // Bought Button
    theBtBought.setBounds(16, 25+60*2, 80, 40);
    // Call back code
    theBtBought.addActionListener(e -> cont.doBought());
    // Add to canvas
    cp.add(theBtBought);

    // Cancel Button
    theBtCancel.setBounds(16, 25+60*3, 80, 40);
    // Call back code
    theBtCancel.addActionListener(e -> cont.doCancel());
    // Add to canvas
    cp.add(theBtCancel);

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

    // Amount label 
    theQuantityLabel.setText("Amount");
    // Set Location
    theQuantityLabel.setBounds(330, 15, 50, 40);
    // Added to canvas
    cp.add(theQuantityLabel);
    // Amount spinner
    theQuantity.setBounds(330, 50, 50, 40);
    // Add to canvas
    cp.add(theQuantity);

    // Scrolling pane label
    theOutputLabel.setBounds(110, 100, 270, 20);
    // Set text
    theOutputLabel.setText("Basket");
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
   * @param c The controller
   */
  public void setController(CashierController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC The observed model
   * @param arg Specific args
   */
  @Override
  public void update(Observable modelC, Object arg) {
    CashierModel model = (CashierModel) modelC;
    String message = (String) arg;
    theAction.setText(message);
    Basket basket = model.getBasket();
    if (basket == null) {
      theOutput.setText("Customers order");
    } else {
      theOutput.setText(basket.getDetails());
    }
    // Focus is here
    theInput.requestFocus();
  }
}