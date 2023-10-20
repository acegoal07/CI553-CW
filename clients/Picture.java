package clients;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A class to display a picture in a client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class Picture extends Canvas {
  private static final long serialVersionUID = 1;
  private int width = 260;
  private int height = 260;
  private Image thePicture = null;

  /**
   * Create a picture
   */
  public Picture() {
    setSize( width, height );
  }
  /**
   * Create a picture of a given size
   * @param aWidth The Given width
   * @param aHeight The given height
   */
  public Picture(int aWidth, int aHeight) {
    width = aWidth;
    height= aHeight;
    setSize( width, height );
  }
  /**
   * Set the picture to be displayed
   * @param ic The picture
   */
  public void set( ImageIcon ic ) {
    // Image to be drawn
    thePicture = ic.getImage();
    repaint();
  }
  /**
   * Force a repaint of the picture
   */
  public void clear() {
    // clear picture
    thePicture = null;
    // Force repaint
    repaint();
  }
  /**
   * When 'Window' is first shown or damaged
   */
  public void paint( Graphics g ) {
    drawImage( (Graphics2D) g );
  }
  /**
   * Called by repaint
   */
  public void update(Graphics g) {
    // Draw picture
    drawImage((Graphics2D) g);
  }
  /**
   * Draw the picture
   * First set the area to white and then
   *  draw the image
   * @param g Graphics context
   */
  public void drawImage(Graphics2D g) {
    setSize( width, height );
    g.setPaint( Color.white );
    g.fill( new Rectangle2D.Double( 0, 0, width, height ) );
    if (thePicture != null) {
      g.drawImage(thePicture, 0, 0, null);
    }
  }
}
