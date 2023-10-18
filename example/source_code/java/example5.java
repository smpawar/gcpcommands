/*
 * This example shows how to stream data from the database
 */

import java.sql.*;
import java.io.*;

class StreamExample
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

    // It's faster when you don't commit automatically
    conn.setAutoCommit (false);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create the example table
    try
    {
      stmt.execute ("drop table streamexample");
    }
    catch (SQLException e)
    {
      // An exception would be raised if the table did not exist
      // We just ignore it
    }

    // Create the table
    stmt.execute ("create table streamexample 
                   (NAME varchar2 (256), DATA long)");

    // Let's insert some data into it.  We'll put the source code
    // for this very test in the database.
    File file = new File ("StreamExample.java");
    InputStream is = new FileInputStream ("StreamExample.java");
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into streamexample 
                              (data, name) values (?, ?)");
    pstmt.setAsciiStream (1, is, (int)file.length ());
    pstmt.setString (2, "StreamExample");
    pstmt.execute ();

    // Do a query to get the row with NAME 'StreamExample'
    ResultSet rset = 
      stmt.executeQuery ("select DATA from streamexample where
                          NAME='StreamExample'");
    
    // Get the first row
    if (rset.next ())
    {
      // Get the data as a Stream from Oracle to the client
      InputStream gif_data = rset.getAsciiStream (1);

      // Open a file to store the gif data
      FileOutputStream os = new FileOutputStream ("example.out");
      
      // Loop, reading from the gif stream and writing to the file
      int c;
      while ((c = gif_data.read ()) != -1)
        os.write (c);

      // Close the file
      os.close ();
    }
  
    // Close all the resources
    if (rset != null)
      rset.close();
    
    if (stmt != null)
      stmt.close();
    
    if (pstmt != null)
      pstmt.close();

    if (conn != null)
      conn.close();
  }
}
