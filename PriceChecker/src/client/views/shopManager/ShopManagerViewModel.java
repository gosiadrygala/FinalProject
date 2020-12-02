package client.views.shopManager;

import client.clientmodel.shopManagerModel.ShopManagerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.util.Product;

import java.util.ArrayList;
/**
 * Class used for pushing the request from controller class and sending back the responses.
 * @author Hadi
 */

public class ShopManagerViewModel
{
  private ShopManagerModel shopManagerModel;
  private ObservableList<Product> listOfAllProductsForSpecificManager;
  private ObservableList<String> tagsForSpecificProduct;

  public ShopManagerViewModel(ShopManagerModel shopManagerModel)
  {
    this.shopManagerModel = shopManagerModel;
    listOfAllProductsForSpecificManager = FXCollections.observableArrayList();
    tagsForSpecificProduct = FXCollections.observableArrayList();
  }

  public String getLoggedInUser()
  {
    return shopManagerModel.getLoggedInUser();
  }

  public void loadProductData(String username)
  {
    ArrayList<Product> products = shopManagerModel.getAllProductsForSpecificManager(username);
    listOfAllProductsForSpecificManager.setAll(products);
  }

  public ObservableList<Product> getListOfAllProductsForSpecificManager()
  {
    return listOfAllProductsForSpecificManager;
  }

  public ObservableList<String> getAllTagsById(int productId)
  {
    ArrayList<String> tags = shopManagerModel.getAllTagsById(productId);
    tagsForSpecificProduct.setAll(tags);
    return tagsForSpecificProduct;
  }

  public void logOut()
  {
    shopManagerModel.logOut();
  }
}
