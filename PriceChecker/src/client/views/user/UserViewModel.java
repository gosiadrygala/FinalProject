package client.views.user;

import client.clientmodel.userModel.UserModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import shared.util.EventType;
import shared.util.Product;
import shared.util.ProductList;

import java.beans.PropertyChangeEvent;

public class UserViewModel
{
  private UserModel userModel;
  private ObservableList<Product> listOfAllProducts;

  public UserViewModel(UserModel userModel)
  {
    this.userModel = userModel;
    listOfAllProducts = FXCollections.observableArrayList();
    userModel.addListener(EventType.NEW_PRODUCT.name(), this::newProduct);
    userModel.addListener(EventType.DELETED_PRODUCT.name(), this::newProduct);
    userModel.addListener(EventType.NEW_CATEGORY.name(), this::newProduct);
  }


  private void newProduct(PropertyChangeEvent propertyChangeEvent)
  {
    Platform.runLater(()-> {
      ProductList productList = (ProductList) propertyChangeEvent.getNewValue();
      listOfAllProducts.setAll(productList.getProducts());
    });
  }


  public void loadProductData()
  {
    listOfAllProducts.setAll(userModel.loadProductData().getProducts());
  }

  public ObservableList<Product> getListOfAllProducts(){
    return listOfAllProducts;
  }

  public void logOut()
  {
    userModel.logOut();
  }

  public String getLoggedInUser()
  {
    return userModel.getLoggedInUser();
  }
}
