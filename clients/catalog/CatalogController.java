package clients.catalog;

/**
 * The catalog Controller
 * @author M A Smith (c) June 2014
 */

public class CatalogController {
  // The interface to the model
  private CatalogModel model;
  private CatalogView view;

  /**
   * Constructor
   * @param model The model
   * @param view  The view from which the interaction came
   */
  public CatalogController(CatalogModel model, CatalogView view) {
    this.view = view;
    this.model = model;
  }
  /**
   * Check interaction from view
   * @param pn Product name
   */
  public void doCheckByName(String pn) {
    model.doCheckByName(pn);
  }
  /**
   * Clear interaction from view
   */
  public void doClear() {
    model.doClear();
  }
}