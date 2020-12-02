package server.networking.servermodel.shopManagerServerModel;

import shared.util.Product;
import shared.util.PropertyChangeSubject;

import java.util.ArrayList;

public interface ShopManagerServerModel extends PropertyChangeSubject
{
  ArrayList<Product> getAllProductsForSpecificManager(String username);
}
