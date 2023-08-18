/*
import java.date;
 * This sample shows how to list all the names from the EMP table
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

class Employee
{
  public static void main (String args [])
       throws SQLException
  {
    // Load the Oracle JDBC driver
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

    // Connect to the database
    // You can put a database name after the @ sign in the connection URL.
    Connection conn =
      DriverManager.getConnection ("jdbc:oracle:oci8:@", "scott", "tiger");

    // Create a Statement
    Statement stmt = conn.createStatement ();

    String sql_stmt = "select"
        + " last_name || ',' || first_name"
        // + "|| ' ' || middle_initial"
        + " as ENAME"
        + " from EMP"
        + " order by last_name, first_name";

    // Select the ENAME column from the EMP table
    ResultSet rset = stmt.executeQuery (sql_stmt);

    // Iterate through the result and print the employee names
    while (rset.next ())
      System.out.println (rset.getString (1));

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Close the connection
    conn.close();   
  }
}
