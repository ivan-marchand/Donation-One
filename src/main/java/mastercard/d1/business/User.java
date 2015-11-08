package mastercard.d1.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import mastercard.d1.db.SqliteDBConnect;

public class User {

	int id;
	String name;
	String email;
	transient String ccn;
	transient String expiry_month;
	transient String expiry_year;
	transient String cvc;
	String zipcode;
	
	public static class Link {
		Integer id_charity;
		float amount;
		float oneclick;
		Charity charity;
	}
	
	HashMap<Integer, Link> links;
	
	public static User retrieveUser(String email){
	
		User result = null;
		
		Connection con = SqliteDBConnect.getUserConnection();
		
		PreparedStatement stmt = null;
	    String query = "select id, name, email, ccn , expiry_month , expiry_year , cvc, zipcode  " +
	                   "from users where email = ?";
	    try {
	        stmt = con.prepareStatement(query);
	        stmt.setString(1, email);
	        
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	
	        	result = new User();
	        	
	        	result.id = rs.getInt("id");
	        	result.name = rs.getString("name");
	        	result.email = rs.getString("email");
	        	result.ccn = rs.getString("ccn");
	        	result.expiry_month = rs.getString("expiry_month");
	        	result.expiry_year = rs.getString("expiry_year");
	        	result.cvc = rs.getString("cvc");
	        	result.zipcode = rs.getString("zipcode");
	        	
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
	    
	    if (result!=null) {
	    	retrieveCharityListForUser(result);
	    
	    	retrieveCharityFromLinks(result.links);
	    }		
	    
		return result;		
		
	}
	
	public static HashMap<Integer, Link> retrieveCharityListForUser(User user){
		
		HashMap<Integer,Link> links = new HashMap<Integer,Link>();
		Connection con = SqliteDBConnect.getUserConnection();
		
		PreparedStatement stmt = null;
	    String query = "select id_ch, amount, oneclick " +
	                   "from charitas where id_user = ?";
	    try {
	        stmt = con.prepareStatement(query);
	        stmt.setInt(1, user.id);
	        
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	
	        	Link lnk = new Link();
	        	
	        	lnk.id_charity = rs.getInt("id_ch");
	        	lnk.amount = rs.getFloat("amount");
	        	lnk.oneclick = rs.getFloat("oneclick");
	        	
	        	links.put(lnk.id_charity, lnk);
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
	    
	    user.links = links;
	    
	    return links;
		
	}
	
	public static void retrieveCharityFromLinks(HashMap<Integer,Link> links){
		
		for (Link link:links.values()){
			retrieveCharityFromLink(link);
		}
		
	}
	
	public static void retrieveCharityFromLink(Link link){
		
		link.charity = Charity.retrieveCharityById(link.id_charity);
		
	}
	
}
