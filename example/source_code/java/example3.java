/*
 * This sample shows how to call a PL/SQL stored procedure using the SQL92
 * syntax.  See also the other sample PLSQL.java.
 */

import java.sql.*;
import java.io.*;

class PLSQLExample
{
  public static void main (String args [])
       throws SQLException, IOException
  {
    // Load the driver
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

    // Connect to the database
    // You can put a database name after the @ sign in the connection URL.
    Connection conn =
      DriverManager.getConnection ("jdbc:oracle:oci8:@", "scott", "tiger");

    // Create a statement
    Statement stmt = conn.createStatement ();

    // Create the stored function
    stmt.execute ("create or replace function RAISESAL (name CHAR, raise NUMBER)
                 return NUMBER is begin return raise + 100000; end;");

    // Close the statement
    stmt.close();

    // Prepare to call the stored procedure RAISESAL.
    // This sample uses the SQL92 syntax
    CallableStatement cstmt = conn.prepareCall ("{? = call RAISESAL (?, ?)}");

    // Declare that the first ? is a return value of type Int
    cstmt.registerOutParameter (1, Types.INTEGER);

    // We want to raise LESLIE's salary by 20,000
    cstmt.setString (2, "LESLIE");  // The name argument is the second ?
    cstmt.setInt (3, 20000);        // The raise argument is the third ?
  
    // Do the raise
    cstmt.execute ();

    // Get the new salary back
    int new_salary = cstmt.getInt (1);

    System.out.println ("The new salary is: " + new_salary);

    // Close the statement
    cstmt.close();

    // Close the connection
    conn.close();
  }
}

