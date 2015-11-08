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

	protected static final Logger LOGGER = Logger.getLogger(SqliteDBConnect.class.getName());
	
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
		//LOGGER.debug("Attaching server to DB file:"+localdir);
		
		if (connection==null) {
		    try
		    {
		      // create a database connection
		      connection = DriverManager.getConnection("jdbc:sqlite:"+localdir+File.separator+".."+File.separator+"mastercard.sqlite");
		      
		      Statement statement = connection.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	
		      statement.executeUpdate("create table if not exists categories (id INTEGER PRIMARY KEY, val string) ");
		      statement.executeUpdate("create table if not exists charities (id INTEGER PRIMARY KEY, name string, logopath string, description string, privatekey string, publickey string)");
		      
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
	
	public static Connection userconnection;
	
	public static Connection getUserConnection(){
		
		final String localdir = System.getProperty("user.dir");
		//LOGGER.debug("Attaching server to DB file:"+localdir);
		
		if (userconnection==null) {
		    try
		    {
		      // create a database connection
		      userconnection = DriverManager.getConnection("jdbc:sqlite:"+localdir+File.separator+".."+File.separator+"mastercard_user.sqlite");
		      
		      Statement statement = userconnection.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	
		      statement.executeUpdate("create table if not exists users (id INTEGER PRIMARY KEY, name string, email string, ccn string, expiry_month string, expiry_year string, cvc string, zipcode string) ");
		      statement.executeUpdate("create table if not exists charitas (id_user INTEGER, id_ch INTEGER,  amount REAL, oneclick REAL)");
		      
		      statement.close();
		      
		      userconnection.commit();
		      
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
	    
	    return userconnection;
		
	}
	
	
	public static Connection historyconnection;
	
	public static Connection getHistoryConnection(){
		
		final String localdir = System.getProperty("user.dir");
		//LOGGER.debug("Attaching server to DB file:"+localdir);
		
		if (historyconnection==null) {
		    try
		    {
		      // create a database connection
		    	historyconnection = DriverManager.getConnection("jdbc:sqlite:"+localdir+File.separator+".."+File.separator+"mastercard_history.sqlite");
		      
		      Statement statement = historyconnection.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	
		      statement.executeUpdate("create table if not exists history (id INTEGER PRIMARY KEY, id_user INTEGER, id_ch INTEGER, amount TEXT, dateTime TEXT) ");
		      // statement.executeUpdate("create table if not exists charitas (id_user INTEGER, id_ch INTEGER,  amount REAL, oneclick REAL)");
		      
		      statement.close();
		      
		      historyconnection.commit();
		      
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
	    
	    return historyconnection;
		
	}
}
