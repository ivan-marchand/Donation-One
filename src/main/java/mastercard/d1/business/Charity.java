package mastercard.d1.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import mastercard.d1.db.SqliteDBConnect;

public class Charity {

	Integer id;
	String name;
	String logoPath;
	Vector<String> categories;
	String description;
	String public_key;
	String private_key;
	
	public static Vector<Charity> retrieveCharityByCategoryAndZipcode(String iCategory, String iZipcode){
		
		Vector<Charity> charities = new Vector<Charity>();
		
		Connection con = SqliteDBConnect.getConnection();
		
		Statement stmt = null;
	    String query = "select id, name, logopath, description, privatekey, publickey " +
	                   "from charities";
	    try {
	        stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	        	
	        	Charity charity = new Charity();
	        	charity.id = rs.getInt("id");
	        	charity.name = rs.getString("name");
	        	charity.description = rs.getString("description");
	        	charity.logoPath = rs.getString("logopath");
	        	charity.public_key = rs.getString("publickey");
	        	charity.private_key = rs.getString("privatekey");
	        	
	        	charities.add(charity) ;	
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
	    
	    for (Charity charity:charities){
	    	retrieveCategoryForCharity(charity);
	    }
		
		return charities;
		
	}
	
	public static void retrieveCategoryForCharity(Charity iCharity){
		
		iCharity.categories = new Vector<String>();
		
		Connection con = SqliteDBConnect.getConnection();
		
		PreparedStatement stmt = null;
	    String query = "select c1.val   " +
	                   "from categories c1, charities_cat_lists l1 "+
	                   "where l1.id_ch = ? and l1.id_cat = c1.id ";
	    
	    try {
	    	
	    	stmt = con.prepareStatement(query);
	    	stmt.setInt(1, iCharity.id);
	       
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	iCharity.categories.addElement(rs.getString("val"));
	        }
	        
	    } catch (SQLException e ) {
	    	e.printStackTrace();
	        System.err.println(e);
	    } finally {
	        if (stmt != null) { try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
	    }
		
	}
	
}
