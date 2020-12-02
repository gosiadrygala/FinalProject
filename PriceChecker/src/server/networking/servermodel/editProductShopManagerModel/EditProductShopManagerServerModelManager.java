package server.networking.servermodel.editProductShopManagerModel;

import dataaccess.shopManagerDAO.ShopManagerDAO;
import shared.util.EventType;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class used for implementing the methods from EditProductShopManagerServerModel
 * @author Hadi
 */

public class EditProductShopManagerServerModelManager implements EditProductShopManagerServerModel
{
  private ShopManagerDAO shopManagerDAO;
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  public EditProductShopManagerServerModelManager(ShopManagerDAO shopManagerDAO)
  {
    this.shopManagerDAO = shopManagerDAO;
  }

  @Override public String editShopProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId,int price,String username)
  {
    try
    {
      String product = shopManagerDAO.editShopProduct(productName, productDescription, category, parseTag, productId,price,
          username);
      if(product.equals("Product updated."))
      {
        support.firePropertyChange(EventType.EDIT_SHOP_MANAGER_PRODUCT.name(), null, shopManagerDAO.getAllProductsForSpecificManager(username));
      }
      return product;
    }
    catch (SQLException throwables)
    {
      throwables.printStackTrace();
    }
    return null;
  }

  @Override public void addListener(String eventName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override public void removeListener(String eventName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(eventName, listener);
  }
}
