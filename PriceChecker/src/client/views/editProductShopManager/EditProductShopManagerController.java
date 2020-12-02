package client.views.editProductShopManager;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.views.ViewController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.SearchableComboBox;
import shared.util.Product;

import java.util.ArrayList;
/**
 * Class implementing the view controller interface. Used for initializing
 * view components, retrieving data from them and providing functionality
 * for components.
 *
 * @author Hadi
 */
public class EditProductShopManagerController implements ViewController
{
  @FXML
  public TextArea productPrice;
  @FXML
  private TextField productNameTextField;
  @FXML
  private TextArea productDescriptionTextField;
  @FXML
  private TextField newTagTextField;
  @FXML
  private Button addNewTagBtn;
  @FXML
  private Button editProductBtn;
  @FXML
  private SearchableComboBox<String> categoryBox;
  @FXML
  private TextField newCategoryTextField;
  @FXML
  private Button addNewCategoryBtn;
  @FXML
  private Button goBackBtn;
  @FXML
  private CheckListView<String> checkListViewTags;
  @FXML
  private Label errorLabel;


  private ViewHandler viewHandler;
  private EditProductShopManagerViewModel editProductShopManagerViewModel;
  private Product product;
  private ObservableList<String> tags;


  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    editProductShopManagerViewModel = viewModelFactory.getEditProductShopManagerViewModel();

    productNameTextField.setText(product.getProductName());
    productDescriptionTextField.setText(product.getDescription());

    productPrice.setText(String.valueOf(product.getPrice()));

    editProductShopManagerViewModel.loadData();

    categoryBox.setItems(editProductShopManagerViewModel.getAllProductCategories());
    checkListViewTags.setItems(editProductShopManagerViewModel.getAllTags());

    categoryBox.getSelectionModel().select(product.getCategory());
    selectAllTagsForThisProduct();
  }

  @Override public void handleClickMe(ActionEvent actionEvent)
  {
    if(actionEvent.getSource() == goBackBtn)
      viewHandler.openShopManagerView();
    else if(actionEvent.getSource() == addNewCategoryBtn)
    {
      addNewCategory();
    }
    else if(actionEvent.getSource() == addNewTagBtn)
    {
      addNewTag();
    }
    else if(actionEvent.getSource() == editProductBtn)
    {
      editShopProduct();
    }
  }
  private void editShopProduct()
  {
    String productName = productNameTextField.getText();
    String productDescription = productDescriptionTextField.getText();
    String category = categoryBox.selectionModelProperty().get().getSelectedItem();
    String price = productPrice.getText();
    ObservableList<String> tags = checkListViewTags.getCheckModel().getCheckedItems();

    if(!productName.isEmpty() && productName.length() <= 40){
      if(!productDescription.isEmpty() && productDescription.length() <= 200){
         if(!category.isEmpty()){
          if (!price.isEmpty()){
           if(!tags.isEmpty()){

             ArrayList<String> parseTag = new ArrayList<>(tags);
             String response = editProductShopManagerViewModel.editShopProduct(productName, productDescription, category,
                     parseTag, product.getProductId(),Integer.parseInt(price));

             if (response.equals("Product updated."))
               viewHandler.openShopManagerView();
             else
               errorLabel.setText("Product already exists.");
           }
           else{
             errorLabel.setText("Choose at least one product tag.");
          }
          }
          else{
            errorLabel.setText("Set a price for the product.");
          }
        }
        else{
          errorLabel.setText("Choose product category.");
        }
      }
      else{
        errorLabel.setText("Product description is empty or longer than 200 letters.");
      }
    }
    else{
      errorLabel.setText("Product name is empty or longer than 40 letters.");
    }
  }

  private void selectAllTagsForThisProduct()
  {
    for (int i = 0; i < editProductShopManagerViewModel.getAllTags().size(); i++)
    {
      for (int j = 0; j < tags.size(); j++)
      {
        if(tags.get(j).equals(editProductShopManagerViewModel.getAllTags().get(i)))
        {
          checkListViewTags.getCheckModel().check(i);
        }
      }
    }
  }

  public void setProductData(Product productId, ObservableList<String> tags)
  {
    this.product = productId;
    this.tags = tags;
  }

  private void addNewCategory()
  {
    String newCategory = newCategoryTextField.getText();
    if(!newCategory.isEmpty() && newCategory.length() <= 40)
    {
      String response = editProductShopManagerViewModel.addNewCategory(newCategory);
      errorLabel.setText(response);
      Platform.runLater(() -> categoryBox.getSelectionModel().select(product.getCategory()));
      newCategoryTextField.clear();
    } else
      errorLabel.setText("Category name is too long or category field is empty.");
  }

  private void addNewTag()
  {
    String newTag = newTagTextField.getText();
    if(!newTag.isEmpty() && newTag.length() <= 20)
    {
      String response = editProductShopManagerViewModel.addNewTag(newTag);
      errorLabel.setText(response);
      newTagTextField.clear();
      Platform.runLater(this::selectAllTagsForThisProduct);
    } else
      errorLabel.setText("Tag name is too long or tag field is empty.");
  }
}
