package dataaccess.administratorDAO;

import dataaccess.DatabaseConnection;
import shared.util.Product;
import shared.util.ProductList;
import shared.util.ShopPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class used for retrieving necessary information from the database.
 * @author Gosia, Hadi
 */
public class AdministratorDAOManager implements AdministratorDAO
{

  private DatabaseConnection databaseConnection;

  public AdministratorDAOManager() throws SQLException
  {
    databaseConnection = DatabaseConnection.getInstance();
  }
  /**
   * Method retrieving data about all products stored in the database.
   * @author Gosia, Hadi
   */
  @Override public ProductList giveAllProductData() throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");
      ResultSet resultSet = statement.executeQuery();
      ProductList productList = new ProductList();
      while(resultSet.next()){
        int productId = resultSet.getInt("productid");
        String productName = resultSet.getString("productname");
        String productDescription = resultSet.getString("productdescription");
        String categoryName = resultSet.getString("categoryname");
        Product product = new Product( productId,productName, productDescription, categoryName);
        productList.addProduct(product);
      }
      return productList;
    }
  }
  /**
   * Method retrieving data about the shop offering the specified product, as well as,
   * the price the shop is offering.
   * @param productId - the specific product the method will retrieve data for.
   * @author Gosia, Hadi
   */
  @Override public ArrayList<ShopPrice> getShopPricesTableById(int productId)
      throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement(
          "SELECT users.username, price.price, price.productid FROM users INNER JOIN price ON users.userid = price.userid WHERE price.productid = ?");
      statement.setInt(1, productId);
      ResultSet resultSet = statement.executeQuery();
     ArrayList<ShopPrice> shopPrices = new ArrayList<>();
      while(resultSet.next()){
        String shopManagers = resultSet.getString("username");
        int price = resultSet.getInt("price");
        int id = resultSet.getInt("productid");
        ShopPrice shopPrice = new ShopPrice(shopManagers, price);
        shopPrices.add(shopPrice);
      }
      return shopPrices;
    }
  }

  @Override public ArrayList<String> getAllTagsById(int productId)
      throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement("select * from producttag where productid =?");
      statement.setInt(1, productId);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<String> tags = new ArrayList<>();
      while(resultSet.next()){
        int id = resultSet.getInt("productid");
        String tagname = resultSet.getString("tagname");
        tags.add(tagname);
      }
      return tags;
    }
  }

  @Override public String editProduct(String productName, String productDescription, String category, ArrayList<String> parseTag,
      int productId)
      throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM product where productid = ?");
      statement.setInt(1, productId);
      ResultSet resultSet = statement.executeQuery();
      Product product = null;
      while(resultSet.next())
      {
        int productId2 = resultSet.getInt("productid");
        String name = resultSet.getString("productname");
        String description = resultSet.getString("productdescription");
        String categoryName = resultSet.getString("categoryname");
        product = new Product(productId2, name, description, categoryName);
      }

      Product newProduct = new Product(productId, productName, productDescription, category);
      ArrayList<String> allTagsById2 = getAllTagsById(productId);
      Collections.sort(allTagsById2);
      Collections.sort(parseTag);
      if(newProduct.equals(product))
      {
        if(allTagsById2.equals(parseTag))
          return "Specified product already exists";
        else
        {
          updateTags(parseTag, productId);
          return "Product updated.";
        }
      }
      else
      {
        updateProduct(productId, productName, productDescription, category);
        if(allTagsById2.equals(parseTag))
          return "Product updated.";
        else
        {
          updateTags(parseTag, productId);
          return "Product updated.";
        }
      }
    }
  }

  @Override public String deleteProduct(int productId) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE productid = ?");
      statement.setInt(1, productId);
      statement.execute();
      return "Product deleted.";
    }
  }

  private void updateProduct(int productId, String productName,
      String productDescription, String category) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE product SET productname = ?, productdescription = ?, categoryname = ? WHERE productid = ?");
      statement.setString(1, productName);
      statement.setString(2, productDescription);
      statement.setString(3, category);
      statement.setInt(4, productId);
      statement.executeUpdate();
    }
  }

  private void updateTags(ArrayList<String> parseTag, int productId) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement("DELETE FROM producttag WHERE productid =?");
      statement.setInt(1, productId);
      statement.execute();

      for (String tag : parseTag)
      {
        PreparedStatement statement2 = connection.prepareStatement("INSERT INTO producttag (productid, tagname) VALUES (?,?)");
        statement2.setInt(1, productId);
        statement2.setString(2, tag);
        statement2.executeUpdate();
      }
    }
  }
}