var URL_BASE = "ws/api/v1";
var SERVER_URL = "http://localhost:10110/";

function GetCategoryList(callback, context) {
	$.ajax(SERVER_URL + URL_BASE + "/getCategoryList", {
		dataType : 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});
};

function GetCharityList(callback, context) {
	$.ajax(SERVER_URL + URL_BASE + "/getCharityList", {
		dataType : 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};
