package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class using singleton pattern to establish the connection to the database.
 * @author Gosia, Piotr
 */

public class DatabaseConnection
{
  private static DatabaseConnection instance;
  private String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=pricechecker"; //TODO add current schema
  private String username = "postgres";
  private String password = "7637"; //to be changed


  private DatabaseConnection() throws SQLException{
    DriverManager.registerDriver(new org.postgresql.Driver());
  }


  public static synchronized DatabaseConnection getInstance() throws
      SQLException
  {
    if(instance == null){
      instance = new DatabaseConnection();
    }
    return instance;
  }

  public Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(url, username, password);
  }
}
