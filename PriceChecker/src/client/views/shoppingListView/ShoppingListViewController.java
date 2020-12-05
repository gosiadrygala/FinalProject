package client.views.shoppingListView;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.views.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.util.Product;

public class ShoppingListViewController implements ViewController
{
  @FXML
  private Button backButton;
  @FXML
  private Button clearShoppingListButton;
  @FXML
  private TableView<Product> shoppingListTable;
  @FXML
  private TableColumn<Product, String> productNameColumn;
  @FXML
  private TableColumn<Product, Integer> quantityColumn;

  private ViewHandler viewHandler;
  private ShoppingListViewViewModel shoppingListViewViewModel;
  private String thisUser;

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    shoppingListViewViewModel = viewModelFactory.getShoppingListViewViewModel();

    shoppingListViewViewModel.loadShoppingList();
    loadTable();
    shoppingListTable.setPlaceholder(new Label("No products in the table."));
  }

  private void loadTable()
  {
    shoppingListTable.getItems().clear();
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    shoppingListTable.setItems(shoppingListViewViewModel.getShoppingList());
  }

  @Override public void handleClickMe(ActionEvent actionEvent)
  {
    if(actionEvent.getSource() == backButton)
      viewHandler.openUserView();
    else if(actionEvent.getSource() == clearShoppingListButton)
    {
      if(!shoppingListTable.getItems().isEmpty())
        shoppingListViewViewModel.clearSL();
    }
  }

  public void setUser(String thisUser)
  {
    this.thisUser = thisUser;
  }
}