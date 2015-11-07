package mastercard.d1.business;

public class Category {

	String val;
	
	public Category(String val){
		this.val = val;
	}
	
	public static Category[] returnCategoryList(){
		Category[] res = { new Category("VETERAN")};
		return res;
	}
	
}
