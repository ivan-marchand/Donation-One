function displayTrxList(data, context) {
	$("#trx_list").html(_.template($("#trx_list_template").html())({list: data.reverse().slice(0, 100)}));
}


var data = [{timestamp: "2015-11-08 09:39:15", user: "test", amount: "2", charity: "EDF"}];

function updateTrxList() {
	GetTrxList(displayTrxList, {});
}

window.setInterval(function(){
	updateTrxList();
	}, 3000);

$(function() {
	updateTrxList();
});