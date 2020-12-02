package server.networking.servermodel.shopManagerServerModel;

import dataaccess.shopManagerDAO.ShopManagerDAO;
import shared.util.Product;

import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopManagerServerModelManager implements ShopManagerServerModel
{
  private ShopManagerDAO shopManagerDAO;
  public ShopManagerServerModelManager(ShopManagerDAO shopManagerDAO)
  {
    this.shopManagerDAO = shopManagerDAO;
  }

  @Override public ArrayList<Product> getAllProductsForSpecificManager(
      String username)
  {
    try
    {
      return shopManagerDAO.getAllProductsForSpecificManager(username);
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

  }

  @Override public void removeListener(String eventName,
      PropertyChangeListener listener)
  {

  }
}
