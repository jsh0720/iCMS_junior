$(function() {
	
	// 타이틀러 스크롤
	var scroll_pos = 0;
	$(document).scroll(function() { 
	    scroll_pos = $(this).scrollTop();
	    targetItem = $(".titler-wrap");
	    targetTxt = $(".intro-container");
	    $('#header').removeClass('wt wbg bf')
	    if(scroll_pos > 50) {
	        targetItem.addClass("scroll");
	        targetTxt.addClass("scroll");
	    } else {
	        targetItem.removeClass("scroll");
	        targetTxt.removeClass("scroll");
	    }
	    if(scroll_pos > 150) {
	    	$('#header').addClass('wt wbg bf')
	    }
	});
	
});


// URL 복사
function copyURL() {
	var currentURL = document.location.href;
	var copyText = document.createElement("textarea");
	document.body.appendChild(copyText);
	copyText.value = currentURL;
	copyText.select();
	document.execCommand('copy');
	document.body.removeChild(copyText);		
	alert('URL이 복사되었습니다.');
}

function printPage(){
	window.print();
}