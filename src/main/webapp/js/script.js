// Update sidebar with cat list
function DisplayCattegoryList(categoryList) {
    var $categoryList = _.template($("#category_list_template").html())({list: categoryList});
    $("#category_list").html($categoryList);
}

$(function() {
    GetCategoryList(DisplayCattegoryList);
});