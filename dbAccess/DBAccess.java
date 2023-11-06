package dbAccess;

/**
 * Implements generic management of a database.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DBAccess {
  /**
   * Load the driver for the database
   * @throws Exception if driver not found
   */
  public void loadDriver() throws Exception {
    throw new RuntimeException("No driver");
  }
  /**
   * Return the URL of the database
   * @return URL
   */
  public String urlOfDatabase() {
    return "";
  }
  /**
   * Return the username for the database
   * @return username
   */
  public String username() {
    return "";
  }
  /**
   * Return the password for the database
   * @return password
   */
  public String password() {
    return "";
  }
}
