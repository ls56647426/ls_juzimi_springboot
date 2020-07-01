var loadingDiv = "<div id='loadingDiv' style='position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%;" +
		"height: 100%; background: white; text-align: center;'><img src='/img/load.gif'" +
		"style = 'margin-top:200px;'></div>";

document.write(loadingDiv);

function closeLoading() {
	$("#loadingDiv").fadeOut("normal", function() {
		$(this).remove();
	});
}

var no;
$.parser.onComplete = function() {
	if (no)
		clearTimeout(no);
	no = setTimeout(closeLoading, 1);
}