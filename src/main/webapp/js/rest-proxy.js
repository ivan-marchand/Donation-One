var URL_BASE = "api/v1";
//var SERVER_URL = "http://localhost:10110/";
var SERVER_URL = "";

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
		data: {category: context.category},
		dataType: 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};
