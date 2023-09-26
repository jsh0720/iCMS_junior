
var popupArr = new Array();
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width=450,height=400,left=200,top=200";

function passwordPopup(domain, wr_id, contentId, board_id, queryString, mode) {
	var tmpPopup = window.open(domain+'/write/secret.do?wr_id='+wr_id+'&contentId='+contentId +'&board_id=' +board_id+'&'+unescapeHtml(queryString)+'&mode='+mode, mode + "_passcheck", option);
	popupArr.push(tmpPopup);
}

function executeDelete(domain,wr_id, board_id, queryString) {
	location.href = domain+"/write/delete_write.do?wr_id="+wr_id+"&board_id="+board_id+"&"+unescapeHtml(queryString);
}

function executeModify(domain,wr_id, queryString) {
	location.href = domain+"/index.do?command=modify&wr_id="+wr_id+"&"+unescapeHtml(queryString);
}

function executeCommentModify(wr_id, board_id) {
	var tmpPopup = window.open('/write/comment_form.do?type=modify&wr_id='+wr_id+'&board_id='+board_id, "modify_comment_form", option);
	
	if (tmpPopup == null || typeof(tmpPopup) == "undefined" || (tmpPopup == null && tmpPopup.outerWidth == 0) || (tmpPopup != null && tmpPopup.outerHeight == 0) || tmpPopup.test == "undefined") {
		alert("팝업 차단 기능이 설정되어있습니다\n\n차단 기능을 해제(팝업허용) 한 후 다시 이용해 주십시오.\n\n만약 팝업 차단 기능을 해제하지 않으면\n정상적인 기능사용을 하실 수 없습니다.");
	} else {
		popupArr.push(tmpPopup);
	}
}
