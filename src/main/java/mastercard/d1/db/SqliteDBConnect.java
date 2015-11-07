package mastercard.d1.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mastercard.d1.restjson.services.Services;

import org.apache.log4j.Logger;

public class SqliteDBConnect {

	protected static final Logger LOGGER = Logger.getLogger(Services.class.getName());
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection connection;
	
	public static Connection getConnection(){
		
		final String localdir = System.getProperty("user.dir");
		LOGGER.debug("Attaching server to DB file:"+localdir);
		
		if (connection==null) {
		    try
		    {
		      // create a database connection
		      connection = DriverManager.getConnection("jdbc:sqlite:"+localdir+File.separator+".."+File.separator+"mastercard.sqlite");
		      
		      Statement statement = connection.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	
		      statement.executeUpdate("create table categories if not exists (id INTEGER PRIMARY KEY, val string) ");
		      statement.executeUpdate("create table charities if not exists(id INTEGER PRIMARY KEY, name string, logopath string, privatekey string, publickey string)");
		      statement.executeUpdate("create table charities_cat_lists if not exists (id_ch INTEGER, id_cat INTEGER)");
		      
		      statement.close();
		      
		      connection.commit();
		      
		    }
		    catch(SQLException e)
		    {
		      // if the error message is "out of memory", 
		      // it probably means no database file is found
		      System.err.println(e.getMessage());
		    }
		    finally
		    {
		       
		    }
		}
	    
	    return connection;
		
	}
	
}
