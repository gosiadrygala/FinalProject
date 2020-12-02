package server.networking;


import server.networking.servermodel.addNewProductAdminServerModel.AddNewProductAdminServerModel;
import server.networking.servermodel.administratorServerModel.AdministratorServerModel;
import server.networking.servermodel.editProductAdminServerModel.EditProductAdminServerModel;
import server.networking.servermodel.editProductShopManagerModel.EditProductShopManagerServerModel;
import server.networking.servermodel.loginRegisterServerModel.LoginRegisterServerModel;
import server.networking.servermodel.shopManagerServerModel.ShopManagerServerModel;
import shared.networking.ClientCallback;
import shared.networking.RMIServer;
import shared.util.EventType;
import shared.util.Product;
import shared.util.ProductList;
import shared.util.ShopPrice;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used for receiving, analyzing the request send from cient, as well as, sending the
 * back to the client.
 * @author Gosia, Piotr
 */

public class RMIServerManager implements RMIServer
{
  private LoginRegisterServerModel loginRegisterServerModel;
  private AdministratorServerModel administratorServerModel;
  private AddNewProductAdminServerModel addNewProductAdminServerModel;
  private EditProductAdminServerModel editProductAdminServerModel;
  private EditProductShopManagerServerModel editProductShopManagerServerModel;
  private ShopManagerServerModel shopManagerServerModel;
  private Map<ClientCallback, PropertyChangeListener> listeners = new HashMap<>();

  public RMIServerManager(LoginRegisterServerModel loginRegisterServerModel,
      AdministratorServerModel administratorServerModel,
      AddNewProductAdminServerModel addNewProductAdminServerModel,
      EditProductAdminServerModel editProductAdminServerModel,
      ShopManagerServerModel shopManagerServerModel, EditProductShopManagerServerModel editProductShopManagerServerModel)
      throws RemoteException
  {
    UnicastRemoteObject.exportObject(this, 0);
    this.loginRegisterServerModel = loginRegisterServerModel;
    this.administratorServerModel = administratorServerModel;
    this.addNewProductAdminServerModel = addNewProductAdminServerModel;
    this.editProductAdminServerModel = editProductAdminServerModel;
    this.shopManagerServerModel = shopManagerServerModel;
    this.editProductShopManagerServerModel = editProductShopManagerServerModel;
  }

  public void startServer() throws RemoteException, AlreadyBoundException
  {
    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("priceCheckerServer", this);

  }

  @Override public ProductList loadProductData() throws RemoteException
  {
    return administratorServerModel.loadProductData();
  }

  @Override public ArrayList<ShopPrice> getShopPricesById(int productId)
      throws RemoteException
  {
    return administratorServerModel.getShopPricesById(productId);
  }

  @Override public ArrayList<String> getAllTagsById(int productId)
      throws RemoteException
  {
    return administratorServerModel.getAllTagsById(productId);
  }

  @Override public ArrayList<String> getAllProductCategories()
      throws RemoteException
  {
    return addNewProductAdminServerModel.getAllProductCategories();
  }

  @Override public ArrayList<String> getAllTags() throws RemoteException
  {
    return addNewProductAdminServerModel.getAllTags();
  }

  @Override public String addNewProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag)
      throws RemoteException
  {
    return addNewProductAdminServerModel.addNewProduct(productName, productDescription, category, parseTag);
  }

  @Override public String addNewCategory(String newCategory)
      throws RemoteException
  {
    return addNewProductAdminServerModel.addNewCategory(newCategory);
  }

  @Override public String addNewTag(String newTag) throws RemoteException
  {
    return addNewProductAdminServerModel.addNewTag(newTag);
  }

  @Override public String editProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId)
      throws RemoteException
  {
    return editProductAdminServerModel.editProduct(productName, productDescription, category, parseTag, productId);
  }

  @Override public String deleteProduct(int productId) throws RemoteException
  {
    return administratorServerModel.deleteProduct(productId);
  }

  @Override public void logOut(ClientCallback client) throws RemoteException
  {
    listeners.remove(client);
  }

  @Override public String validateLogin(String username, String password)
      throws RemoteException
  {
    return loginRegisterServerModel.validateLogin(username, password);
  }

  @Override
  public String validateRegister(String username, String email, String password, String dob) throws RemoteException {
    return loginRegisterServerModel.validateRegister(username,email,password,dob);
  }

  @Override public ArrayList<Product> getAllProductsForSpecificManager(
      String username) throws RemoteException
  {
    return shopManagerServerModel.getAllProductsForSpecificManager(username);
  }

  @Override public String editShopProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId, int price,String username)
      throws RemoteException
  {
    return editProductShopManagerServerModel.editShopProduct(productName, productDescription,
        category, parseTag, productId,price,username);
  }


  /**
   * A method used for saving the client references into the pool of listeners.
   * @author Gosia
   */

  @Override public void registerClient(ClientCallback client) throws RemoteException
  {
    PropertyChangeListener listener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        try {
          client.update(evt.getPropertyName(), evt.getNewValue());
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    };
    listeners.put(client, listener);
    addNewProductAdminServerModel.addListener(EventType.NEW_PRODUCT.name(), listener);
    addNewProductAdminServerModel.addListener(EventType.NEW_CATEGORY.name(), listener);
    addNewProductAdminServerModel.addListener(EventType.NEW_TAG.name(), listener);
    editProductAdminServerModel.addListener(EventType.NEW_PRODUCT.name(), listener);
    administratorServerModel.addListener(EventType.DELETED_PRODUCT.name(), listener);
  }
}
