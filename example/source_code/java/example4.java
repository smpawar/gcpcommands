/* 
 * This sample demonstrate basic LOB support.
 */

import java.sql.*;
import java.io.*;
import java.util.*;
import oracle.jdbc.driver.*;

//needed for new CLOB and BLOB classes
import oracle.sql.*;

public class LobExample
{
  public static void main (String args [])
       throws Exception
  {
    // Register the Oracle JDBC driver
    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

    // Connect to the database
    // You can put a database name after the @ sign in the connection
URL.
    Connection conn =
      DriverManager.getConnection ("jdbc:oracle:oci8:@", "scott",
"tiger");

    // Need to set auto commit off to update LOBs
    conn.setAutoCommit (false);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("drop table basic_lob_table");
    }
    catch (SQLException e)
    {
      // An exception could be raised here if the table did not exist
already.
    }

    // Create a table containing a BLOB and a CLOB
    stmt.execute ("create table basic_lob_table (x varchar2 (30), b
blob, c clob)");
    
    // Populate the table
    stmt.execute ("insert into basic_lob_table values ('one',
'010101010101010101010101010101', 'onetwothreefour')");
    stmt.execute ("insert into basic_lob_table values ('two',
'0202020202020202020202020202', 'twothreefourfivesix')");

    // commit set up
    conn.commit();

    System.out.println ("Dumping lobs");

    // Select the lobs 
    // note that the FOR UPDATE clause is needed for updating LOBs  
    ResultSet rset = stmt.executeQuery ("select * from basic_lob_table
for update");
    while (rset.next ())
    {
      // Get the lobs
      BLOB blob = ((OracleResultSet)rset).getBLOB (2);
      CLOB clob = ((OracleResultSet)rset).getCLOB (3);

      // Print the lob contents
      dumpBlob (conn, blob);
      dumpClob (conn, clob);

      // Change the lob contents
      fillClob (conn, clob, 2000);
      fillBlob (conn, blob, 4000);

    }
    // You could rollback the changes made by fillClob() and fillBlob()
    // by issuing a rollback here
    // conn.rollback();

    System.out.println ("Dumping lobs again");

    // No need to have FOR UPDATE clause just to do selects
    rset = stmt.executeQuery ("select * from basic_lob_table");
    while (rset.next ())
    {
      // Get the lobs
      BLOB blob = ((OracleResultSet)rset).getBLOB (2);
      CLOB clob = ((OracleResultSet)rset).getCLOB (3);

      // Print the lobs contents
      dumpBlob (conn, blob);
      dumpClob (conn, clob);
    }
    // Close all resources
    rset.close();
    stmt.close();
    conn.close(); 
  }

  // Utility function to dump Clob contents
  static void dumpClob (Connection conn, CLOB clob)
    throws Exception
  {
    // get character stream to retrieve clob data
    Reader instream = clob.getCharacterStream();

    // create temporary buffer for read
    char[] buffer = new char[10];

    // length of characters read
    int length = 0;

    // fetch data  
    while ((length = instream.read(buffer)) != -1)
    {
      System.out.print("Read " + length + " chars: ");

      for (int i=0; i<length; i++)
        System.out.print(buffer[i]);
      System.out.println();
    }

    // Close input stream
    instream.close();
  }

  // Utility function to dump Blob contents
  static void dumpBlob (Connection conn, BLOB blob)
    throws Exception
  {
    // Get binary output stream to retrieve blob data
    InputStream instream = blob.getBinaryStream();

    // Create temporary buffer for read
    byte[] buffer = new byte[10];

    // length of bytes read
    int length = 0;

    // Fetch data  
    while ((length = instream.read(buffer)) != -1)
    {
      System.out.print("Read " + length + " bytes: ");

      for (int i=0; i<length; i++)
        System.out.print(buffer[i]+" ");
      System.out.println();
    }

    // Close input stream
    instream.close();
  }

  // Utility function to put data in a Clob
  static void fillClob (Connection conn, CLOB clob, long length)
    throws Exception
  {
    Writer outstream = clob.getCharacterOutputStream();

    int i = 0;
    int chunk = 10;

    while (i < length)
    {
      outstream.write(i + "hello world", 0, chunk);

      i += chunk;
      if (length - i < chunk)
      chunk = (int) length - i;
    }
    outstream.close();
  }

  // Utility function to put data in a Blob
  static void fillBlob (Connection conn, BLOB blob, long length)
    throws Exception
  {
    OutputStream outstream = blob.getBinaryOutputStream();

    int i = 0;
    int chunk = 10;

    byte [] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    while (i < length)
    {
      data [0] = (byte)i;
      outstream.write(data, 0, chunk);

      i += chunk;
      if (length - i < chunk)
      chunk = (int) length - i;
    }
    outstream.close();
  }
}
