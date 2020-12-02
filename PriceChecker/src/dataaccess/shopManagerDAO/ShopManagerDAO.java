package dataaccess.shopManagerDAO;

import shared.util.Product;
import shared.util.ProductList;
import shared.util.ShopPrice;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 * An interface separating the data access objects from server models.
 * @author Hadi
 */
public interface ShopManagerDAO
{
  ArrayList<Product> getAllProductsForSpecificManager(String username) throws
      SQLException;
  ProductList giveAllProductData() throws SQLException;
  ArrayList<String> getAllTagsById(int productId) throws SQLException;
  String editShopProduct(String productName, String productDescription, String category, ArrayList<String> parseTag,
      int productId,int price,String username) throws SQLException;
}
