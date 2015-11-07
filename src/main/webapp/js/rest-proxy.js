var URL_BASE = "ws/api/v1";
var SERVER_URL = "http://localhost:10110/";

function GetCategoryList(callback, context) {
	$.ajax(SERVER_URL + URL_BASE + "/getCategoryList", {
    		dataType: 'jsonp',
    		success: function(data) {
	    	  console.log(data);
	    	  callback(data, context);
	      }
      });
	callback(["Hunger", "Children", "Veterans"], context);
}