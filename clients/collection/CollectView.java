package clients.collection;

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

public class CollectView implements Observer {

  /**
   * Name of buttons
   */
  class name {
    public static final String COLLECT = "Collect";
  }

  // Height of window pixels
  private static final int H = 300;
  // Width  of window pixels
  private static final int W = 400;

  private final JLabel theAction = new JLabel();
  private final JLabel theOutputLabel = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtCollect = new JButton(name.COLLECT);

  private OrderProcessing theOrder = null;
  private CollectController cont = null;

  /**
   * Construct the view
   * @param rpc Window in which to construct
   * @param mf Factor to deliver order and stock objects
   * @param x x-coordinate of position of window on screen
   * @param y y-coordinate of position of window on screen
   */
  public CollectView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    try {
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

    // Font f is
    Font f = new Font("Monospaced",Font.PLAIN,12);

    // Check Button
    theBtCollect.setBounds(16, 25+60*0, 80, 40);
    // Call back code
    theBtCollect.addActionListener(e -> {
      cont.doCollect(theInput.getText());
      theInput.setText("");
      theAction.setText("Enter order number");
    });
    // Add to canvas
    cp.add(theBtCollect);

    // Message area
    theAction.setBounds(110, 25 , 270, 20);
    // Blank
    theAction.setText("Enter order number");
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
    theOutputLabel.setText("Orders collected");
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
   * Set the controller active
   * @param c The controller
   */
  public void setController(CollectController c) {
    cont = c;
  }
  /**
   * Update the view
   * @param modelC The observed model
   * @param arg Specific args
   */
  @Override
  public void update(Observable modelC, Object arg) {
    CollectModel model = (CollectModel) modelC;
    String message = (String) arg;
    theAction.setText(message);

    theOutput.setText(model.getResponse());
    // Focus is here
    theInput.requestFocus();
  }
}