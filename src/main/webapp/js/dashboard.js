function displayTrxList(data, context) {
	$("#trx_list").html(_.template($("#trx_list_template").html())({list: data.slice(0, 100)}));
}


var data = [{timestamp: "2015-11-08 09:39:15", user: "test", amount: "2", charity: "EDF"}];

function updateTrxList() {
	displayTrxList(data, {});
}

window.setInterval(function(){
	updateTrxList();
	data.push({timestamp: "2015-11-08 09:39:15", user: "test", amount: "2", charity: "EDF"});
	}, 3000);

$(function() {
	updateTrxList();
});