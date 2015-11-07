package mastercard.d1.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import mastercard.d1.db.SqliteDBConnect;

public class Category {

	String val;
	
	public Category(String val){
		this.val = val;
	}
	
	public static Category[] returnCategoryList(){
		
		Vector<Category> cats = new Vector<Category>();
		
		Connection con = SqliteDBConnect.getConnection();
		
		Statement stmt = null;
	    String query = "select val " +
	                   "from categories";
	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	cats.add(new Category(rs.getString("val"))) ;	
	        }
	    } catch (SQLException e ) {
	        System.err.println(e);
	    } finally {
	        if (stmt != null) { try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
	    }
		
		return cats.toArray(new Category[cats.size()]);
	}
	
}
