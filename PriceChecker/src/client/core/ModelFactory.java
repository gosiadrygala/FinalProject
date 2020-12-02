package client.core;
import client.clientmodel.addNewProductAdministratorModel.AddNewProductAdminModel;
import client.clientmodel.addNewProductAdministratorModel.AddNewProductAdminModelManager;
import client.clientmodel.administratorModel.AdministratorModel;
import client.clientmodel.administratorModel.AdministratorModelManager;
import client.clientmodel.editProductAdministratorModel.EditProductAdministratorModel;
import client.clientmodel.editProductAdministratorModel.EditProductAdministratorModelManager;
import client.clientmodel.editProductShopManagerModel.EditProductShopManagerModel;
import client.clientmodel.editProductShopManagerModel.EditProductShopManagerModelManager;
import client.clientmodel.loginRegisterModel.LoginRegisterModel;
import client.clientmodel.loginRegisterModel.LoginRegisterModelManager;
import client.clientmodel.shopManagerModel.ShopManagerModel;
import client.clientmodel.shopManagerModel.ShopManagerModelManager;

/**
 * Class used for lazy instantiation of model instances and passing
 * the instance of a client to the models.
 *
 * @author Gosia, Piotr, Karlo
 */

public class ModelFactory
{
  private LoginRegisterModel loginRegisterModel;
  private AdministratorModel administratorModel;
  private AddNewProductAdminModel addNewProductAdminModel;
  private EditProductAdministratorModel editProductAdministratorModel;
  private EditProductShopManagerModel editProductShopManagerModel;
  private ShopManagerModel shopManagerModel;
  private ClientFactory clientFactory;

  public ModelFactory(ClientFactory clientFactory){
    this.clientFactory = clientFactory;
  }

  public LoginRegisterModel getLoginRegisterModel(){
    if(loginRegisterModel == null){
      loginRegisterModel = new LoginRegisterModelManager(clientFactory.getClient());
    }
    return loginRegisterModel;
  }

  public AdministratorModel getAdministratorModel(){
    if(administratorModel == null){
      administratorModel = new AdministratorModelManager(clientFactory.getClient());
    }
    return administratorModel;
  }

  public AddNewProductAdminModel getAddNewProductAdminModel()
  {
    if(addNewProductAdminModel == null){
      addNewProductAdminModel = new AddNewProductAdminModelManager(clientFactory.getClient());
    }
    return addNewProductAdminModel;
  }

  public EditProductAdministratorModel getEditProductAdministratorModel()
  {
    if(editProductAdministratorModel == null){
      editProductAdministratorModel = new EditProductAdministratorModelManager(clientFactory.getClient());
    }
    return editProductAdministratorModel;
  }

  public EditProductShopManagerModel getEditProductShopManagerModel()
  {
    if (editProductShopManagerModel == null){
      editProductShopManagerModel = new EditProductShopManagerModelManager(clientFactory.getClient());
    }
    return editProductShopManagerModel;
  }

  public ShopManagerModel getShopManagerModel()
  {
    if(shopManagerModel == null){
      shopManagerModel = new ShopManagerModelManager(clientFactory.getClient());
    }
    return shopManagerModel;
  }
}
