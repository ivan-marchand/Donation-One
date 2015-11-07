package mastercard.d1.business;

import java.util.Vector;

public class Charity {

	String name;
	String logoPath;
	Vector<String> categories;
	String description;
	String public_key;
	
	public static Vector<Charity> retrieveCharityByCategoryAndZipcode(String iCategory, String iZipcode){
		
		Vector<Charity> charities = new Vector<Charity>();
		
		Charity charity = new Charity();
		charity.name = "Veteran - Avon Hill";
		charity.logoPath = "static/images/AvonHillVeteran.gif";
		charity.categories = new Vector<String>();
		charity.categories.add("VETERAN");
		charity.public_key = "ABCDEF";
		
		charities.add(charity);
		
		return charities;
		
	}
	
}
