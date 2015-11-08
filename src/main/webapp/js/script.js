var $categoryDisplayOrder = ["Children", "Hunger", "Worldwide", "Veteran"];
var $charityMap = {};
var $user = {};

// Truncate long text
function truncate(text, maxTextSize) {
	$(".expand_text").on("click", function() {
		$(this).parent().find(".extra_text").show();
		$(this).hide();
	});
	if (text.length > maxTextSize) {
		return $("<div/>").html("<p>" + text.substring(0, maxTextSize)  + "<a href=\"javascript:void(0)\" class=\"expand_text\">....</a><span class=\"extra_text\" hidden>" + text.substring(maxTextSize) + "</span></p>").html();
	} else {
		return $("<div/>").html("<p>" + text + "</p>").html();
	}
}

// Update sidebar with cat list
function DisplayCattegoryList(categoryList, context) {
    var $categoryList = _.template($("#category_list_template").html())({list: categoryList});
    $("#category_list").html($categoryList);

    // Register filter button
    $(".category_filter").on("click", function() {
    	$charityMap = {};
    	GetCharityList(DisplayCharityList, {category: $(this).attr("category"), display_full_list: true});
    });

};

function UpdateCharityListDisplay(category, display_full_list) {
	// Update charity list
	if (category in $charityMap) {
		// Limit nb of charity displayed by row to 3
		var $colNb = 0;
		var $charityListToDisplay = $charityMap[category].slice(0, 3);
		while ($colNb * 3 < $charityMap[category].length) {
			$("#charity_list").append(_.template($("#charity_list_template").html())({list: $charityListToDisplay, category: category, display_category: $colNb == 0 ? true : false}));
			$colNb += 1;
			if (!display_full_list) {
				break;
			}
			$charityListToDisplay = $charityMap[category].slice($colNb * 3, $colNb * 3 + 3);
		}
	}
}

function DisplayCharityList(charityList, context) {
	$charityMap[context.category] = charityList;
    
    // Rebuild display
    $("#charity_list").empty();
    
    // Display only one category ?
    if (context.display_full_list) {
		UpdateCharityListDisplay(context.category, context.display_full_list);
    } else {
    	for (var i in $categoryDisplayOrder) {
    		UpdateCharityListDisplay($categoryDisplayOrder[i], context.display_full_list);
    	}
	}

    // Register filter button
    $(".category_filter").on("click", function() {
    	$charityMap = {};
    	GetCharityList(DisplayCharityList, {category: $(this).attr("category"), display_full_list: true});
    });

    // Register donate button
    $(".donate_button").on("click", function() {
    	OpenDonateDialog($(this));
    });
};

// Open payment dialog box
function OpenDonateDialog(btn) {
	$("#modal_charity_name").html(btn.attr("charity_name"));
	var $charityId = btn.attr("charity_id");
	$("#payment_button").attr("charity_id", $charityId);
	
	// Logged in ?
	$("#cvc").val("");
	if (!_.isEmpty($user)) {
		console.log($user);
		$("#ccn").val($user.ccn);
		$("#exp_month").val($user.expiry_month);
		$("#exp_year").val($user.expiry_year);
		// Default amount defined ?
		if ($charityId in $user.links) {
			$("#donation_amount").val($user.links[$charityId].amount);
		} else {
			$("#donation_amount").val("");
		}
	} else {
		$("#ccn").val("");
		$("#exp_month").val("");
		$("#exp_year").val("");
		$("#donation_amount").val("");
	}

	// Show dialog
	$("#payment_modal").modal();
}

// Display all charity with category selected by default
function DisplayFullCharityList() {
	// Charity List
	$charityMap = {};
    for (var i in $categoryDisplayOrder) {
    	GetCharityList(DisplayCharityList, {category: $categoryDisplayOrder[i], display_full_list: false});
    }
}

// Display search result
function DisplaySearchResult(data, use_location, context) {
	$charityMap = {};
	if (use_location) {
		var $charityList = [];
		for (var i in data) {
			var item = data[i];
			console.log(item);
			var $charity = item.charity;
			$charity["address"] = item.address;
			$charityList.push($charity);
		}
		$charityMap["Search Result"] = $charityList;
	} else {
		$charityMap["Search Result"] = data;
	}
	$("#charity_list").empty();
	UpdateCharityListDisplay("Search Result", true);

    // Register donate button
    $(".donate_button").on("click", function() {
    	OpenDonateDialog($(this));
    });
}

// Display success/failure dialog
function DisplaySuccessDialog(alert) {
	$("#dialog_modal_title").html("Success");
	$("#dialog_alert").removeClass("alert-warning").addClass("alert-success");
	$("#dialog_alert").html(alert);
	$("#dialog_modal").modal();
}
function DisplayFailureDialog(alert) {
	$("#dialog_modal_title").html("Failure");
	$("#dialog_alert").removeClass("alert-success").addClass("alert-warning");
	$("#dialog_alert").html(alert);
	$("#dialog_modal").modal();
}

// Login user
function Login(data) {
	// Logged In?
	if (data.length && data.length > 0 && data[0] != null) {
		$user = data[0];
		// Display user name in navbar
		$("#logged_in").show();
		$("#logged_out").hide();
		$("#user_name").html($user.name);
		$.cookie("email", $user.email);
		
		// Update Zip code search box
		$("#zip_code").val($user.zipcode);
	} else {
		DisplayFailureDialog("Login failed, please try again");
	}
	// Hide login modal
	$("#login_modal").modal('hide');
};

$(function() {
	
	// Category List side bar
    GetCategoryList(DisplayCattegoryList, {});

    // Display all charity with category selected by default
    DisplayFullCharityList();
    $(".navbar-brand").on("click", function() {
    	DisplayFullCharityList();
    });
    
    // Login
    $("#login").on("click", function() {
    	$("#login_modal").modal();
    });
    $("#login_button").on("click", function() {
    	RetrieveUser(Login, $("#email_address").val(), {});
    });
    // Already logged in ?
    if ($.cookie("email") && _.isEmpty($user)) {
    	RetrieveUser(Login, $.cookie("email"), {});
    }
    
    // Logout
    $("#logout").on("click", function() {
    	// Hide user name
    	$("#logged_in").hide();
    	$("#logged_out").show();
    	$.removeCookie("email");
    	$user = {};
    });

    // User profile
    $("#user_name").on("click", function() {
    	// Email / Zip code
    	$("#user_email").val($user.email);
    	$("#user_zip_code").val($user.zipcode);
    	
    	// display charity preferences
    	console.log($user);
    	$("#charity_pref_table").html(_.template($("#charity_pref_template").html())({links: $user.links}));
    	$("#user_profile_modal").modal();
    });

    // Search
    $("#charity_search").val("");
    $("#zip_code").val("");
    $("#charity_search_button").on("click", function() {
    	SearchCharity(DisplaySearchResult, {text: $("#charity_search").val()}, {});
    });
    $("#charity_search").on("keydown", function(e) {
    	if(e.which == 13) {
    		SearchCharity(DisplaySearchResult, {text: $("#charity_search").val()}, {});
    	}
    });

    $("#zip_code_button").on("click", function() {
        $("#charity_search").val("");
    	SearchCharityByZipCode(DisplaySearchResult, {zipcode: $("#zip_code").val()}, {});
    });
    $("#zip_code").on("keydown", function(e) {
    	if(e.which == 13) {
    	    $("#charity_search").val("");
    		SearchCharityByZipCode(DisplaySearchResult, {zipcode: $("#zip_code").val()}, {});
    	}
    });

    // Payment
    $("#payment_button").on("click", function() {
    	data = {
    	id: $(this).attr("charity_id"),
        amount: $("#donation_amount").val(),
        cardNumber: $("#ccn").val(),
        cardExpiryMonth: $("#exp_month").val(),
        cardExpiryYear: $("#exp_year").val(),
        cardCvc: $("#cvc").val()
    	};
    	if ($user.email) {
    		data["email"] = $user.email;
    	}
    	ProcessPayment(data, {});
    });
    
    // Avoid submit when enter is hit
    $(window).keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
      });
});