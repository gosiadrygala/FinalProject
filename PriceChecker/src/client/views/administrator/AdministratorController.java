package client.views.administrator;
import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.views.ViewController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import shared.util.Product;
import shared.util.ShopPrice;
/**
 * Class implementing the view controller interface. Used for initializing
 * view components, retrieving data from them and providing functionality
 * for components.
 *
 * @author Gosia, Karlo, Hadi
 */

public class AdministratorController implements ViewController
{
  @FXML
  private Button deleteProductBtn;
  @FXML
  private TableView<Product> productTable;
  @FXML
  private TableColumn<Product, Integer> productIdColumn;
  @FXML
  private TableColumn<Product, String> productNameColumn;
  @FXML
  private TableColumn<Product, String> productDescriptionColumn;
  @FXML
  private TableColumn<Product, String> productCategoryColumn;
  @FXML
  private Button addProductBtn;
  @FXML
  private Button editProductBtn;
  @FXML
  private TableView<ShopPrice> shopPriceTable;
  @FXML
  private TableColumn<ShopPrice, String> shopColumn;
  @FXML
  private TableColumn<ShopPrice, Integer> priceColumn;
  @FXML
  private Button logOutBtn;
  @FXML
  private TableView<String> tagListTable;
  @FXML
  private TableColumn<String, String> tagsColumn;
  @FXML
  private Label errorLabel;
  @FXML
  private TextField searchBar;

  private ViewHandler viewHandler;
  private AdministratorViewModel administratorViewModel;

  /**
   * Method used; for initializing the controller, loading the initial
   * data and loading the table with all the products.
   *
   * @author Gosia, Karlo
   */
  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.administratorViewModel = viewModelFactory.getAdministratorViewModel();
    administratorViewModel.loadProductData();
    loadProductTable();
  }

  /**
   * Method used to load the product data into the Product Table
   * @author Gosia, Hadi
   */
  private void loadProductTable()
  {
    productTable.getItems().clear();
    productIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productId"));
    productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String >("description"));
    productCategoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));

    // Initialize the filtered list with an observable list of all products.
    FilteredList<Product> filteredData = new FilteredList<>(
        administratorViewModel.getListOfAllProducts(), p -> true);

    // Set Predicate expression whenever filter changes.
    searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(product -> {
        // If search bar text field is empty, display all products.
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // Compare product name or category or description with the string from the search bar.
        String lowerCaseFilter = newValue.toLowerCase();

        if (product.getProductName().toLowerCase().contains(lowerCaseFilter)) {
          return true; // String matches with product name.
        } else if (product.getCategory().toLowerCase().contains(lowerCaseFilter))
          return true; // String matches with product category.
        else if (product.getDescription().toLowerCase().contains(lowerCaseFilter))
          return true; // String matches with product description.
        return false; // Does not match.
      });
    });

    // Initialize the filtered list with a sorted list.
    SortedList<Product> sortedData = new SortedList<>(filteredData);

    // Order of values in the sorted list is bound to the order of values from the product table
    sortedData.comparatorProperty().bind(productTable.comparatorProperty());

    // Add filtered data to the table.
    productTable.setItems(sortedData);
  }

  /**
   * Method used to load the shop and price data into the ShopPrice Table.
   * Triggered when the row in the product table is pressed.
   * @author Gosia, Karlo, Hadi
   */
  @FXML
  void loadShopPriceTable(MouseEvent event) {
    //Getting the id number of the product by selecting the row
    shopPriceTable.getItems().clear();
    Platform.runLater(()-> {
      TablePosition pos = productTable.getSelectionModel().getSelectedCells().get(0);
      int row = pos.getRow();
      Product item = productTable.getItems().get(row);
      int productId = item.getProductId();

      //get the shop and price table by product id
      ObservableList<ShopPrice> shopPrices = administratorViewModel.getShopPricesById(productId);
      shopPriceTable.getItems().clear();
      shopColumn.setCellValueFactory(new PropertyValueFactory<ShopPrice, String>("shopName"));
      priceColumn.setCellValueFactory(new PropertyValueFactory<ShopPrice, Integer>("price"));

      for (int i = 0; i < shopPrices.size(); i++)
        shopPriceTable.getItems().add(shopPrices.get(i));

      //get all tags assigned to the specific product
      ObservableList<String> tags = administratorViewModel.getAllTagsById(productId);
      tagListTable.getItems().clear();
      tagsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

      for (int i = 0; i < tags.size(); i++)
        tagListTable.getItems().add(tags.get(i));
    });
  }

  /**
   * Method used for handling events from UI
   *
   * @author Gosia, Karlo
   */
  @Override public void handleClickMe(ActionEvent actionEvent)
  {
    if(actionEvent.getSource() == logOutBtn){
      administratorViewModel.logOut();
      viewHandler.openLoginView();
    }
    else if(actionEvent.getSource() == addProductBtn)
    {
      viewHandler.openAddNewProductView();
    }
    else if(actionEvent.getSource() == editProductBtn)
    {
      editProduct();
    }
    else if(actionEvent.getSource() == deleteProductBtn)
    {
      deleteProduct();
    }
  }

  private void editProduct()
  {
    if(productTable.getSelectionModel().getSelectedCells().isEmpty())
    {
      errorLabel.setText("Please first select a product in a table.");
    }
    else
    {
      /*
          Taking the selected row from the table, creating a product object,
          getting all tags assigned to the selected product, a passing it
          to the next view.
       */
      TablePosition pos = productTable.getSelectionModel().getSelectedCells().get(0);
      int row = pos.getRow();
      Product item = productTable.getItems().get(row);
      ObservableList<String> tags = administratorViewModel.getAllTagsById(item.getProductId());
      viewHandler.openEditProductView(item, tags);
    }
  }

  private void deleteProduct()
  {
    if(productTable.getSelectionModel().getSelectedCells().isEmpty())
    {
      errorLabel.setText("Please first select a product in a table.");
    }
    else
    {
      TablePosition pos = productTable.getSelectionModel().getSelectedCells().get(0);
      int row = pos.getRow();
      Product item = productTable.getItems().get(row);
      String response = administratorViewModel.deleteProduct(item.getProductId());
      errorLabel.setText(response);
    }
  }
}

