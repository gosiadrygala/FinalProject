package client.networking;
import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.util.Product;
import shared.util.ProductList;
import shared.util.ShopPrice;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
/**
 * Class implementing the client interface. Used for requesting data from
 * the RMI server as well as firing events.
 *
 * Providing methods for; starting the client and establishing the connection
 * to the server, as well as retrieving, modifying and validating data.
 *
 * @author Gosia, Piotr, Karlo
 */

public class RMIClient implements Client, ClientCallback {
  private RMIServer rmiServer;
  private PropertyChangeSupport support;
  private String clientUsername;
  private int userid;

  public RMIClient() {
    support = new PropertyChangeSupport(this);
  }

  /**
   * Method used for starting the client, and adding it to the register pool on
   * the server side.
   *
   * @author Gosia, Piotr
   */
  @Override
  public void startClient() {
    try {
      UnicastRemoteObject.exportObject(this, 0);
      Registry registry = LocateRegistry.getRegistry("localhost", 1099);
      rmiServer = (RMIServer) registry.lookup("priceCheckerServer");
    } catch (RemoteException | NotBoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method used for sending a request to validate the login information,
   * send from the model, to the server. Additionally it takes care of adding the client
   * to the listener registry in server.
   *
   * @param username
   * @param password
   * @author Gosia, Piotr
   */
  @Override
  public String validateLogin(String username, String password) {
    try {
      String response = rmiServer.validateLogin(username, password);
      if (response.equals("User with this username does not exist") ||
              response.equals("Wrong credentials") ||
              response.equals("Houston we have a problem someone fucked up the code.")) {
        return response;
      } else {
        rmiServer.registerClient(this);
        return response;
      }
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public ProductList loadProductData() {
    try {
      return rmiServer.loadProductData();
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public ArrayList<ShopPrice> getShopPricesById(int productId) {
    try {
      return rmiServer.getShopPricesById(productId);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public ArrayList<String> getAllTagsById(int productId) {
    try {
      return rmiServer.getAllTagsById(productId);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public ArrayList<String> getAllProductCategories() {
    try {
      return rmiServer.getAllProductCategories();
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public ArrayList<String> getAllTags() {
    try {
      return rmiServer.getAllTags();
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public String addNewProduct(String productName,
                              String productDescription, String category, ArrayList<String> parseTag) {
    try {
      return rmiServer.addNewProduct(productName, productDescription, category, parseTag);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public String addNewCategory(String newCategory) {
    try {
      return rmiServer.addNewCategory(newCategory);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public String addNewTag(String newTag) {
    try {
      return rmiServer.addNewTag(newTag);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public String editProduct(String productName,
                            String productDescription, String category, ArrayList<String> parseTag,
                            int productId) {
    try {
      return rmiServer.editProduct(productName, productDescription, category, parseTag, productId);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override public String editShopProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId, int price,String clientUsername)
  {
    try {
      return rmiServer.editShopProduct(productName, productDescription, category, parseTag, productId,price,clientUsername);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public String deleteProduct(int productId) {
    try {
      return rmiServer.deleteProduct(productId);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override
  public void logOut() {
    try {
      rmiServer.logOut(this);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }


  @Override public void setClientUsername(String username)
  {
    clientUsername = username;
  }

  @Override public String getLoggedInUser()
  {
    return clientUsername;
  }


  @Override public ArrayList<Product> getAllProductsForSpecificManager(
      String username)
  {
    try {
      return rmiServer.getAllProductsForSpecificManager(username);
    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }

  @Override public void update(String eventName, Object newValue)
      throws RemoteException
  {
    support.firePropertyChange(eventName, null, newValue);
  }

  @Override
  public void addListener(String eventName,
                          PropertyChangeListener listener) {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override
  public void removeListener(String eventName, PropertyChangeListener listener) {
    support.removePropertyChangeListener(eventName, listener);
  }

  /**
   * Method used for sending a request to validate the register information,
   * send from the model, to the server. Additionally it takes care of adding the client
   * to the listener registry in server.
   *
   * @param username
   * @param password
   * @param email
   * @param dob
   * @author Dorin, Piotr
   */

  @Override
  public String validateRegister(String username, String email, String password, String dob) {

    try {
      String response = rmiServer.validateRegister(username, email, password, dob);
      if (response.equals("User with this username already exist") ||
              response.equals("Email already used") ||
              response.equals("Houston we have a problem someone fucked up the code.")) {
        return response;
      } else {
        rmiServer.registerClient(this);
        return response;
      }

    } catch (RemoteException e) {
      throw new RuntimeException("Could not contact server");
    }
  }
}
