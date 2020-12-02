package client.clientmodel.shopManagerModel;


import shared.util.Product;
import shared.util.PropertyChangeSubject;

import java.util.ArrayList;

public interface ShopManagerModel extends PropertyChangeSubject
{
  String getLoggedInUser();
  ArrayList<Product> getAllProductsForSpecificManager(String username);
  ArrayList<String> getAllTagsById(int productId);
  void logOut();
}
