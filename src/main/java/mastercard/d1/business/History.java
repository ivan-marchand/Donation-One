package mastercard.d1.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import mastercard.d1.db.SqliteDBConnect;

public class History {

	public Integer id;
	Integer id_user;
	Integer id_ch;
	String amount;
	String dateTime;
	
	String charityName;
	String userName;
		
	public static void createRecord(History iRecord){
		
		//Record record = null;
		
		Connection con = SqliteDBConnect.getHistoryConnection();
		
		PreparedStatement stmt = null;
	    String query = "insert into history (id_user, id_ch , amount , dateTime) values (?,?,?,?)";
	    try {
	        stmt = con.prepareStatement(query);
	        stmt.setInt(1,iRecord.id_user);
	        stmt.setInt(2,iRecord.id_ch);
	        stmt.setString(3,iRecord.amount);
	        stmt.setString(4,iRecord.dateTime);
	        
        	int res = stmt.executeUpdate();
	        	
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
	    		
	}
	
	public static Vector<History> getTransactionList(){
		
		Vector<History> aHistoryList = new Vector<History>();
		
		Connection con = SqliteDBConnect.getHistoryConnection();
		
		PreparedStatement stmt = null;
	    String query = "select * from history";
	    try {
	    	
	        stmt = con.prepareStatement(query);	        
	        ResultSet res = stmt.executeQuery();
        	
        	while (res.next()) {
        		History aHistory = new History();
        		
        		aHistory.id_user = res.getInt("id_user");
        		aHistory.id_ch = res.getInt("id_ch");
        		aHistory.amount = res.getString("amount");
        		aHistory.dateTime = res.getString("dateTime");
        		
        		Charity aCharity = Charity.retrieveCharityById(aHistory.id_ch);
        		aHistory.charityName = aCharity.name;
        		
        		if (aHistory.id_user == -1)
        			aHistory.userName = "Anonymous";
        		else {
        			User aUser = User.retrieveUser(aHistory.id_user);
            		aHistory.userName = aUser.name;
        		}
        		
        		aHistoryList.add(aHistory);
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
		
		return aHistoryList;
	}
		
}
