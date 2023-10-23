package clients.cashier;

/**
 * The Cashier Controller
 * @author M A Smith (c) June 2014
 */

public class CashierController {
  private CashierModel model;
  private CashierView  view;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CashierController(CashierModel model, CashierView view) {
    this.view  = view;
    this.model = model;
  }
  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck(String pn, int pq) {
    model.doCheck(pn, pq);
  }
  /**
   * Buy interaction from view
   */
  public void doBuy() {
    model.doBuy();
  }
  /**
   * Cancel interaction from view
   */
  public void doCancel() {
    model.doCancel();
  }
  /**
   * Bought interaction from view
   */
  public void doBought() {
    model.doBought();
  }
}
