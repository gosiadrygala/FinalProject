package client.clientmodel.addNewProductAdministratorModel;
import shared.util.PropertyChangeSubject;
import java.util.ArrayList;

/**
 * Interface is used for separating the view model from the model as
 * well as providing methods to the model manager.
 *
 * The interface extends property change subject to listen and fire events
 * to the listeners of this model.
 *
 * @author Gosia, Karlo
 */

public interface AddNewProductAdminModel extends PropertyChangeSubject
{
  ArrayList<String> getAllProductCategories();
  ArrayList<String> getAllTags();
  String addNewProduct(String productName, String productDescription, String category, ArrayList<String> parseTag);
  String addNewCategory(String newCategory);
  String addNewTag(String newTag);
}
