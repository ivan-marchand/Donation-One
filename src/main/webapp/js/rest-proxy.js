//var URL_BASE = "ws/api/v1";
//var SERVER_URL = "http://localhost:10110/";
var URL_BASE = "ws/api/v1";
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
		data : {
			category : context.category
		},
		dataType : 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};

function SearchCharity(callback, search, context) {
	$.ajax(SERVER_URL + URL_BASE + "/searchCharity", {
		data : search,
		dataType : 'jsonp',
		success : function(data) {
			callback(data, false, context);
		}
	});

};

function SearchCharityByZipCode(callback, search, context) {
	$.ajax(SERVER_URL + URL_BASE + "/placesRetrieve", {
		data : search,
		dataType : 'jsonp',
		success : function(data) {
			callback(data, true, context);
		}
	});

};

function RetrieveUser(callback, email, context) {
	$.ajax(SERVER_URL + URL_BASE + "/retrieveUser", {
		data : {
			email : email
		},
		dataType : 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};

function GetTrxList(callback, context) {
	$.ajax(SERVER_URL + URL_BASE + "/getTrxList", {
		dataType : 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};

function ProcessPayment(data, context) {
	$.ajax(SERVER_URL + URL_BASE + "/processPayment", {
		type : "POST",
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		data : JSON.stringify(data),
		dataType : 'json',
		contentType : "application/json",
		success : function(data) {
			$("#payment_modal").modal('hide');
			if (data.result == "OK") {
				DisplaySuccessDialog("Payment successfully processed");
			} else {
				DisplayFailureDialog("Unable to process payment");
			}
		},
		error : function() {
			$("#payment_modal").modal('hide');
			DisplayFailureDialog("Unable to process payment");
		}
	});
};