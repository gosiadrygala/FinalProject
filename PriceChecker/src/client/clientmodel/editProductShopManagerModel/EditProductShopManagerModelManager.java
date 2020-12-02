package client.clientmodel.editProductShopManagerModel;

import client.networking.Client;
import javafx.event.Event;
import shared.util.EventType;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
/**
 * Class implementing the model interface. Used for requesting data from
 * the client as well as listening for events and passing them forward.
 *
 * Providing methods for; getting all categories, getting all product tags,
 * adding new categories, adding new tags and editing a product from the shop managers shop.
 *
 * @author Hadi
 */
public class EditProductShopManagerModelManager implements EditProductShopManagerModel
{
  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  private Client client;
  public EditProductShopManagerModelManager(Client client)
  {
    this.client = client;
    client.addListener(EventType.NEW_CATEGORY.name(), evt -> support.firePropertyChange(evt));
    client.addListener(EventType.NEW_TAG.name(), evt -> support.firePropertyChange(evt));
  }
    @Override public void addListener(String eventName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(eventName, listener);
  }

  @Override public void removeListener(String eventName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(eventName,listener);
  }

  @Override public ArrayList<String> getAllProductCategories()
  {
    return client.getAllProductCategories();
  }

  @Override public ArrayList<String> getAllTags()
  {
    return client.getAllTags();
  }


  @Override public String addNewCategory(String newCategory)
  {
    return client.addNewCategory(newCategory);
  }

  @Override public String addNewTag(String newTag)
  {
    return client.addNewTag(newTag);
  }

  @Override public String editShopProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId,int price)
  {
    return client.editShopProduct(productName,productDescription,category,parseTag,productId,price,
        client.getLoggedInUser());
  }
}
