package mastercard.d1.business;

import java.util.Vector;

public class Category {

	String val;
	String code;
	
	public Category(String val, String code){
		this.val = val;
		this.code = code;
	}
	
	public static Category[] returnCategoryList(){
		Category[] res = { new Category("VETERAN","1020")};
		return res;
	}
	
}
