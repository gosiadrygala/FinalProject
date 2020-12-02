package client.networking;
import shared.util.Product;
import shared.util.ProductList;
import shared.util.PropertyChangeSubject;
import shared.util.ShopPrice;

import java.util.ArrayList;

/**
 * Interface is used for separating the models from the client implementation as
 * well as providing abstract classes to the RMI client.
 *
 * The interface extends property change subject to fire events
 * to the listeners of this client.
 *
 * @author Gosia, Piotr, Karlo
 */

public interface Client extends PropertyChangeSubject
{
  void startClient();
  String validateLogin(String username, String password);
  ProductList loadProductData();
  ArrayList<ShopPrice> getShopPricesById(int productId);
  ArrayList<String> getAllTagsById(int productId);
  ArrayList<String> getAllProductCategories();
  ArrayList<String> getAllTags();
  String addNewProduct(String productName, String productDescription,
      String category, ArrayList<String> parseTag);
  String addNewCategory(String newCategory);
  String addNewTag(String newTag);
  String editProduct(String productName, String productDescription,
      String category, ArrayList<String> parseTag, int productId);
  String editShopProduct(String productName, String productDescription,
      String category, ArrayList<String> parseTag, int productId,int price,String username);
  String deleteProduct(int productId);
  void logOut();
  String validateRegister(String username, String email, String password, String dob);
  void setClientUsername(String username);
  String getLoggedInUser();
  ArrayList<Product> getAllProductsForSpecificManager(String username);
}
