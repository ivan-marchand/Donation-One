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
		data: {category: context.category},
		dataType: 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});

};

function SearchCharity(callback, context) {
	$.ajax(SERVER_URL + URL_BASE + "/searchCharity", {
		data: {text: context.text},
		dataType: 'jsonp',
		success : function(data) {
			callback(data, context);
		}
	});
	
};

function ProcessPayment(data, context) {
	$.ajax(SERVER_URL + URL_BASE + "/processPayment", {
		type: "POST",
		 headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		 	},
		data: JSON.stringify(data),
		dataType: 'json',
		contentType: "application/json",
		success : function(data) {
			$("#payment_modal").modal('hide');
			if (data.result == "OK") {
				$("#payment_result_modal_title").html("Success");
				$("#payment_result_alert").removeClass("alert-warning").addClass("alert-success");
				$("#payment_result_alert").html("Payment successfully processed");
			} else {
				$("#payment_result_modal_title").html("Failure");
				$("#payment_result_alert").removeClass("alert-success").addClass("alert-warning");
				$("#payment_result_alert").html("Unable to process payment");
			}
			$("#payment_result_modal").modal();
		},
		error: function() {
			$("#payment_modal").modal('hide');
			$("#payment_result_modal_title").html("Failure");
			$("#payment_result_alert").removeClass("alert-success").addClass("alert-warning");
			$("#payment_result_alert").html("Unable to process payment");
			$("#payment_result_modal").modal();
		}
	});
};