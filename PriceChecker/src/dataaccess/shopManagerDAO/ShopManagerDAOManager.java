package dataaccess.shopManagerDAO;

import dataaccess.DatabaseConnection;
import shared.util.Product;
import shared.util.ProductList;
import shared.util.ShopPrice;
import shared.util.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Class used for retrieving necessary information from the database.
 * @author Piotr, Hadi
 */
public class ShopManagerDAOManager implements ShopManagerDAO
{

  private DatabaseConnection databaseConnection;

  public ShopManagerDAOManager() throws SQLException
  {
    databaseConnection = DatabaseConnection.getInstance();
  }

  @Override public ArrayList<Product> getAllProductsForSpecificManager(
      String username) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT product.productid, product.productname, product.productdescription, "
              + "product.categoryname, price.price " + "from price inner join product on product.productid = "
              + "price.productid inner join users on price.userid = users.userid where users.username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Product> products = new ArrayList<>();
      while (resultSet.next())
      {
        int productId2 = resultSet.getInt("productid");
        String name = resultSet.getString("productname");
        String description = resultSet.getString("productdescription");
        String categoryName = resultSet.getString("categoryname");
        int price = resultSet.getInt("price");
        Product product = new Product(productId2, name, description,
            categoryName, price);
        products.add(product);
      }

      return products;
    }
  }

  @Override public ProductList giveAllProductData() throws SQLException
  {
    return null;
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

  @Override public String editShopProduct(String productName,
      String productDescription, String category, ArrayList<String> parseTag,
      int productId, int price,String username) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection()){
      PreparedStatement statement = connection.prepareStatement("SELECT u.userName as shopmanagers, p.productName,"
          + " p.productId, p.categoryname,p.productdescription,"
          + " pr.price, pt.tagname FROM price pr\n"
          + "JOIN product p ON pr.productId = p.productId\n"
          + "JOIN users u ON u.userId = pr.UserId\n"
          + "JOIN productTag pt ON pt.productId = p.productId where u.username = ? and pr.price = ?");
      statement.setString(1, username);
      statement.setInt(2,productId);
      ResultSet resultSet = statement.executeQuery();
      Product product = null;

      while(resultSet.next())
      {
        int productId2 = resultSet.getInt("productid");
        String name = resultSet.getString("productname");
        String description = resultSet.getString("productdescription");
        String categoryName = resultSet.getString("categoryname");
        int price1 = resultSet.getInt("price");
        product = new Product(productId2, name, description, categoryName,price1);
      }

      Product newProduct = new Product(productId, productName, productDescription, category,price);
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
        updateProduct(productId, productName, productDescription, category,price,username);
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
  private void updateProduct(int productId, String productName,
      String productDescription, String category,int price,String username) throws SQLException
  {
    try (Connection connection = databaseConnection.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE product SET productname = ?, productdescription = ?, categoryname = ? WHERE productid = ?; "
              + "Update price set price = ? where userid = (select userid from users where username = ?) and productid = ?");
      statement.setString(1, productName);
      statement.setString(2, productDescription);
      statement.setString(3, category);
      statement.setInt(4,productId);
      statement.setInt(5, price);
      statement.setString(6,username);
      statement.setInt(7,productId);

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
