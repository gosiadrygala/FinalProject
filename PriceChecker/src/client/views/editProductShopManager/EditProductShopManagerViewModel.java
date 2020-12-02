package client.views.editProductShopManager;

import client.clientmodel.editProductShopManagerModel.EditProductShopManagerModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.util.EventType;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * Class used for pushing the request from controller class and sending back the responses.
 * @author Hadi
 */
public class EditProductShopManagerViewModel
{
  private EditProductShopManagerModel editProductShopManagerModel;
  private ObservableList<String> allProductCategories;
  private ObservableList<String> allTags;

  public EditProductShopManagerViewModel(EditProductShopManagerModel editProductShopManagerModel)
  {
    this.editProductShopManagerModel = editProductShopManagerModel;
    allProductCategories = FXCollections.observableArrayList();
    allTags = FXCollections.observableArrayList();
    editProductShopManagerModel.addListener(EventType.NEW_CATEGORY.name(), this::newCategory);
    editProductShopManagerModel.addListener(EventType.NEW_TAG.name(), this::newTag);
  }

  private void newTag(PropertyChangeEvent event)
  {
    Platform.runLater(() -> {
      //allTags.clear();
      ArrayList<String> newTags = (ArrayList<String>) event.getNewValue();
      allTags.setAll(newTags);
      //for (String newTag : newTags)
      //allTags.add(newTag);
    });
  }

  private void newCategory(PropertyChangeEvent event)
  {
    Platform.runLater(() -> {
      //allProductCategories.clear();
      ArrayList<String> newCategories = (ArrayList<String>) event.getNewValue();
      allProductCategories.setAll(newCategories);
      //for (String newCategory : newCategories)
      //allProductCategories.add(newCategory);
    });
  }

  public ObservableList<String> getAllProductCategories()
  {
    return allProductCategories;
  }

  public ObservableList<String> getAllTags()
  {
    return allTags;
  }

  public void loadData()
  {
    allProductCategories.setAll(editProductShopManagerModel.getAllProductCategories());
    allTags.setAll(editProductShopManagerModel.getAllTags());
  }

  public String addNewCategory(String newCategory)
  {
    return editProductShopManagerModel.addNewCategory(newCategory);
  }

  public String addNewTag(String newTag)
  {
    return editProductShopManagerModel.addNewTag(newTag);
  }

  public String editShopProduct(String productName, String productDescription,
      String category, ArrayList<String> parseTag, int productId,int price)
  {
    return editProductShopManagerModel.editShopProduct(productName, productDescription, category,
        parseTag, productId,price);
  }

}
