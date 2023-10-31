package clients.collection;

/**
 * The Collection Controller
 * @author M A Smith (c) June 2014
 */

public class CollectController {
  // The interface to the model object
  private CollectModel model;
  private CollectView view;

  /**
   * Constructor
   * @param model The model
   * @param view  The view from which the interaction came
   */
  public CollectController(CollectModel model, CollectView view) {
    this.view = view;
    this.model = model;
  }
  /**
   * Collect interaction from view
   * @param orderNum The order collected
   */
  public void doCollect(String orderNum) {
    model.doCollect(orderNum);
  }
}


