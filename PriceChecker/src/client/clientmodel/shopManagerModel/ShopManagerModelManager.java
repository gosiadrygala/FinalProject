package client.clientmodel.shopManagerModel;

import client.networking.Client;
import shared.util.Product;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ShopManagerModelManager implements ShopManagerModel
{
  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  private Client client;

  public ShopManagerModelManager(Client client)
  {
    this.client = client;
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

  @Override public String getLoggedInUser()
  {
    return client.getLoggedInUser();
  }

  @Override public ArrayList<Product> getAllProductsForSpecificManager(
      String username)
  {
    return client.getAllProductsForSpecificManager(username);
  }

  @Override public ArrayList<String> getAllTagsById(int productId)
  {
    return client.getAllTagsById(productId);
  }

  @Override public void logOut()
  {
    client.logOut();
  }
}
