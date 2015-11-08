// Update sidebar with cat list
function DisplayCattegoryList(categoryList, context) {
    var $categoryList = _.template($("#category_list_template").html())({list: categoryList});
    $("#category_list").html($categoryList);

    // Register filter button
    $(".category_filter").on("click", function() {
    	GetCharityList(DisplayCharityList, {category: $(this).attr("category"), redisplay: true});
    });

};

//Update sidebar with cat list
function DisplayCharityList(charityList, context) {
    var $charityList = _.template($("#charity_list_template").html())({list: charityList, category: context.category});
    // Page must be refreshed ?
    if (context.redisplay) {
    	$("#charity_list").html($charityList);
    } else {
    	$("#charity_list").append($charityList);
    }

    // Register filter button
    $(".category_filter").on("click", function() {
    	GetCharityList(DisplayCharityList, {category: $(this).attr("category"), redisplay: true});
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
	$("#charity_list").empty();
	GetCharityList(DisplayCharityList, {category: "Children"});
	GetCharityList(DisplayCharityList, {category: "Veterans"});
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