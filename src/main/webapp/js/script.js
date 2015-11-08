var $categoryDisplayOrder = ["Children", "Hunger", "Worldwide", "Veteran"];
var $charityMap = {};

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
    	$("#modal_charity_name").html($(this).attr("charity_name"));
    	$("#payment_modal").modal();
    });
};

// Display all charity with category selected by default
function DisplayFullCharityList() {
	// Charity List
	$charityMap = {};
    for (var i in $categoryDisplayOrder) {
    	GetCharityList(DisplayCharityList, {category: $categoryDisplayOrder[i], display_full_list: false});
    }
}

$(function() {
	// Category List side bar
    GetCategoryList(DisplayCattegoryList, {});

    // Display all charity with category selected by default
    DisplayFullCharityList();
    $(".navbar-brand").on("click", function() {
    	DisplayFullCharityList();
    });
});