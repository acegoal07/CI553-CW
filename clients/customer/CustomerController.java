package clients.customer;

/**
 * The Customer Controller
 * @author M A Smith (c) June 2014
 */

public class CustomerController {
  // The interface to the model
  private CustomerModel model;
  private CustomerView view;

  /**
   * Constructor
   * @param model The model
   * @param view  The view from which the interaction came
   */
  public CustomerController(CustomerModel model, CustomerView view) {
    this.view = view;
    this.model = model;
  }
  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck(String pn, int amount) {
    model.doCheck(pn, amount);
  }
  /**
   * Clear interaction from view
   */
  public void doClear() {
    model.doClear();
  }
}