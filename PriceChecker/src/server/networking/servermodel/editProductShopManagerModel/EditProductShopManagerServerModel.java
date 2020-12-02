package server.networking.servermodel.editProductShopManagerModel;

import shared.util.PropertyChangeSubject;

import java.util.ArrayList;
/**
 * Class used for separating the Edit Product-Shop Manager from the server.
 * @author Hadi
 */
public interface EditProductShopManagerServerModel extends PropertyChangeSubject
{
  String editShopProduct(String productName, String productDescription, String category, ArrayList<String> parseTag,
      int productId,int price,String username);
}
