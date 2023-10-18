import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLServerConnection {

  public static void main(String[] args) {
    // Connection information
    String serverName = "localhost";
    String databaseName = "myDatabase";
    String username = "sa";
    String password = "your_password";

    // Create connection
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(
          "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";user=" + username + ";password=" + password);
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    // Create table
    Statement statement = null;
    try {
      statement = connection.createStatement();
      statement.execute(
          "CREATE TABLE IF NOT EXISTS myTable (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY(id))");
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    // Insert 1 million records
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement("INSERT INTO myTable (name) VALUES (?)");
      for (int i = 0; i < 1000000; i++) {
        preparedStatement.setString(1, "Record " + i);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    // Select 1000 records
    statement = null;
    try {
      statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM myTable LIMIT 1000");
      while (resultSet.next()) {
        System.out.println(resultSet.getInt("id") + " - " + resultSet.getString("name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    // Close connection
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

# end of file
