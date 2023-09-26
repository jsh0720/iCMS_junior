/*******************************************************************************
 * // parameter escape html
 ******************************************************************************/
function unescapeHtml(str) {
	if (str == null) {
		return "";
	}
	return str.replace(/&amp;/g, '&').replace(/&lt;/g, '<').replace(/&gt;/g,
			'>').replace(/&quot;/g, '"').replace(/&#039;/g, "'").replace(
			/&#39;/g, "'");
}

/*******************************************************************************
 * // easyUI format, parse
 ******************************************************************************/
function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

function myparser(s) {
	if (!s)
		return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	} else {
		return new Date();
	}
}

/*******************************************************************************
 * //다음 주소검색
 ******************************************************************************/
function win_zip(frm_name, frm_zip, frm_addr1, frm_addr2, frm_addr3, frm_jibeon) {
	if (typeof daum === 'undefined') {
		alert("다음 우편번호 postcode.v2.js 파일이 로드되지 않았습니다.");
		return false;
	}

	var zip_case = 1; // 0이면 레이어, 1이면 페이지에 끼워 넣기, 2이면 새창

	var complete_fn = function(data) {
		// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

		// 각 주소의 노출 규칙에 따라 주소를 조합한다.
		// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
		var fullAddr = ''; // 최종 주소 변수
		var extraAddr = ''; // 조합형 주소 변수

		// 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
		if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
			fullAddr = data.roadAddress;

		} else { // 사용자가 지번 주소를 선택했을 경우(J)
			fullAddr = data.jibunAddress;
		}

		// 사용자가 선택한 주소가 도로명 타입일때 조합한다.
		if (data.userSelectedType === 'R') {
			// 법정동명이 있을 경우 추가한다.
			if (data.bname !== '') {
				extraAddr += data.bname;
			}
			// 건물명이 있을 경우 추가한다.
			if (data.buildingName !== '') {
				extraAddr += (extraAddr !== '' ? ', ' + data.buildingName
						: data.buildingName);
			}
			// 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
			extraAddr = (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
		}

		// 우편번호와 주소 정보를 해당 필드에 넣고, 커서를 상세주소 필드로 이동한다.
		var of = document[frm_name];

		of[frm_zip].value = data.zonecode;

		of[frm_addr1].value = fullAddr;
		of[frm_addr3].value = extraAddr;

		if (of[frm_jibeon] !== undefined) {
			of[frm_jibeon].value = data.userSelectedType;
		}

		setTimeout(function() {
			of[frm_addr2].focus();
		}, 100);
	};

	switch (zip_case) {
	case 1: // iframe을 이용하여 페이지에 끼워 넣기
		var daum_pape_id = 'daum_juso_page' + frm_zip, element_wrap = document
				.getElementById(daum_pape_id), currentScroll = Math.max(
				document.body.scrollTop, document.documentElement.scrollTop);
		if (element_wrap == null) {
			element_wrap = document.createElement("div");
			element_wrap.setAttribute("id", daum_pape_id);
			element_wrap.style.cssText = 'display:none;border:1px solid;left:0;width:100%;height:300px;margin:5px 0;position:relative;-webkit-overflow-scrolling:touch;';
			element_wrap.innerHTML = '<img src="//i1.daumcdn.net/localimg/localimages/07/postcode/320/close.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-21px;z-index:1" class="close_daum_juso" alt="접기 버튼">';
			jQuery('form[name="' + frm_name + '"]').find(
					'input[name="' + frm_addr1 + '"]').before(element_wrap);
			jQuery("#" + daum_pape_id).off("click", ".close_daum_juso").on(
					"click", ".close_daum_juso", function(e) {
						e.preventDefault();
						jQuery(this).parent().hide();
					});
		}

		new daum.Postcode({
			oncomplete : function(data) {
				complete_fn(data);
				// iframe을 넣은 element를 안보이게 한다.
				element_wrap.style.display = 'none';
				// 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
				document.body.scrollTop = currentScroll;
			},
			// 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분.
			// iframe을 넣은 element의 높이값을 조정한다.
			onresize : function(size) {
				element_wrap.style.height = size.height + "px";
			},
			maxSuggestItems : 10,
			width : '100%',
			height : '100%'
		}).embed(element_wrap);

		// iframe을 넣은 element를 보이게 한다.
		element_wrap.style.display = 'block';
		break;
	case 2: // 새창으로 띄우기
		new daum.Postcode({
			oncomplete : function(data) {
				complete_fn(data);
			}
		}).open();
		break;
	default: // iframe을 이용하여 레이어 띄우기
		var rayer_id = 'daum_juso_rayer' + frm_zip, element_layer = document
				.getElementById(rayer_id);
		if (element_layer == null) {
			element_layer = document.createElement("div");
			element_layer.setAttribute("id", rayer_id);
			element_layer.style.cssText = 'display:none;border:5px solid;position:fixed;width:300px;height:460px;left:50%;margin-left:-155px;top:50%;margin-top:-235px;overflow:hidden;-webkit-overflow-scrolling:touch;z-index:10000';
			element_layer.innerHTML = '<img src="//i1.daumcdn.net/localimg/localimages/07/postcode/320/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" class="close_daum_juso" alt="닫기 버튼">';
			document.body.appendChild(element_layer);
			jQuery("#" + rayer_id).off("click", ".close_daum_juso").on("click",
					".close_daum_juso", function(e) {
						e.preventDefault();
						jQuery(this).parent().hide();
					});
		}

		new daum.Postcode({
			oncomplete : function(data) {
				complete_fn(data);
				// iframe을 넣은 element를 안보이게 한다.
				element_layer.style.display = 'none';
			},
			maxSuggestItems : 10,
			width : '100%',
			height : '100%'
		}).embed(element_layer);

		// iframe을 넣은 element를 보이게 한다.
		element_layer.style.display = 'block';
	}
}

/*******************************************************************************
 * // 관리자페이지 검색창 검색 시 시작날짜 > 종료날짜 일시 return
 ******************************************************************************/

function searchDateCheck(startDate, endDate) {
	var checkResult = true;
	if ((startDate != '' && typeof startDate != 'undefined')
			&& (endDate != '' && typeof endDate != 'undefined')) {
		var start = new Date(startDate);
		var end = new Date(endDate);
		if (start > end) {
			checkResult = false;
		}
	}
	return checkResult;
}

/*******************************************************************************
 * // 관리자페이지 검색창 날짜설정 함수
 ******************************************************************************/
DateSearch = function() {
	// DateSearch.form = document.getElementById('shop_list');
	DateSearch.date = new Date();
	// 올해
	DateSearch.date.curYear = DateSearch.date.getFullYear();
	// 이번달
	DateSearch.date.curMonth = DateSearch.date.getMonth();
	// 오늘
	DateSearch.date.curDate = DateSearch.date.getDate();
	// 요일
	DateSearch.date.curDay = DateSearch.date.getDay();
	// 오늘 YYYY-mm-dd
	DateSearch.getToday = function() {
		var fullDate = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		$("#search_start_date").datebox('setValue', fullDate);
		$("#search_end_date").datebox('setValue', fullDate);
	};
	// 7일전 YYYY-mm-dd
	DateSearch.getNextSevenDays = function() {
		var sevenDaysLater = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate - 6);
		var nextSevenYear = sevenDaysLater.getFullYear();
		var nextSevenMonth = sevenDaysLater.getMonth();
		var nextSevenDate = sevenDaysLater.getDate();
		// 오늘
		$("#search_start_date").datebox(
				'setValue',
				DateSearch.makeFullDate(nextSevenYear, nextSevenMonth,
						nextSevenDate));
		// 7일뒤
		$("#search_end_date").datebox(
				'setValue',
				DateSearch.makeFullDate(DateSearch.date.curYear,
						DateSearch.date.curMonth, DateSearch.date.curDate));
	};
	// 15일전 YYYY-mm-dd
	DateSearch.getNextFiftheenDays = function() {
		var fifteenDaysLater = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate - 14);
		var nextFifteenYear = fifteenDaysLater.getFullYear();
		var nextFifteenMonth = fifteenDaysLater.getMonth();
		var nextFifteenDate = fifteenDaysLater.getDate();
		// 오늘
		$("#search_start_date").datebox(
				'setValue',
				DateSearch.makeFullDate(nextFifteenYear, nextFifteenMonth,
						nextFifteenDate));
		// 15일뒤
		$("#search_end_date").datebox(
				'setValue',
				DateSearch.makeFullDate(DateSearch.date.curYear,
						DateSearch.date.curMonth, DateSearch.date.curDate));
	};
	// 이번주 YYYY-mm-dd
	DateSearch.getThisWeek = function() {
		var startOfWeek = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate
						- DateSearch.date.curDay);
		var startYear = startOfWeek.getFullYear();
		var startMonth = startOfWeek.getMonth();
		var startDate = startOfWeek.getDate();
		var endOfWeek = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate
						+ (6 - DateSearch.date.curDay));
		var endYear = endOfWeek.getFullYear();
		var endMonth = endOfWeek.getMonth();
		var endDate = endOfWeek.getDate();
		// 이번주 월요일
		$("#search_start_date").datebox('setValue',
				DateSearch.makeFullDate(startYear, startMonth, startDate));
		// 이번주 일요일
		$("#search_end_date").datebox('setValue',
				DateSearch.makeFullDate(endYear, endMonth, endDate));
	};
	// 이번달 YYYY-mm-dd
	DateSearch.getThisMonth = function() {
		// 달의 첫째 날
		DateSearch.date.startOfMonth = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth, 1).getDate();
		// 달의 마지막 날
		DateSearch.date.endOfMonth = new Date(DateSearch.date.curYear,
				DateSearch.date.curMonth + 1, 0).getDate();
		// 이번주 월요일
		$("#search_start_date")
				.datebox(
						'setValue',
						DateSearch.makeFullDate(DateSearch.date.curYear,
								DateSearch.date.curMonth,
								DateSearch.date.startOfMonth));
		// 이번주 일요일
		$("#search_end_date").datebox(
				'setValue',
				DateSearch.makeFullDate(DateSearch.date.curYear,
						DateSearch.date.curMonth, DateSearch.date.endOfMonth));
	};
	// 1개월 전 YYYY-mm-dd
	DateSearch.getMonthAgo = function() {
		var fullDate = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		var month_ago = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth - 1, DateSearch.date.curDate);
		$("#search_start_date").datebox('setValue', month_ago);
		$("#search_end_date").datebox('setValue', fullDate);
	};
	// 3개월 전 YYYY-mm-dd
	DateSearch.getThreeMonthAgo = function() {
		var fullDate = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		var month_ago = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth - 3, DateSearch.date.curDate);
		$("#search_start_date").datebox('setValue', month_ago);
		$("#search_end_date").datebox('setValue', fullDate);
	};
	// 6개월 전 YYYY-mm-dd
	DateSearch.getSixMonthAgo = function() {
		var fullDate = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		var month_ago = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth - 6, DateSearch.date.curDate);
		$("#search_start_date").datebox('setValue', month_ago);
		$("#search_end_date").datebox('setValue', fullDate);
	};
	// 12개월 전 YYYY-mm-dd
	DateSearch.getTwelveMonthAgo = function() {
		var fullDate = DateSearch.makeFullDate(DateSearch.date.curYear,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		var month_ago = DateSearch.makeFullDate(DateSearch.date.curYear - 1,
				DateSearch.date.curMonth, DateSearch.date.curDate);
		$("#search_start_date").datebox('setValue', month_ago);
		$("#search_end_date").datebox('setValue', fullDate);
	};
	// 전체
	DateSearch.resetDate = function() {
		$("#search_start_date").datebox('setValue', '');
		$("#search_end_date").datebox('setValue', '');
	};
	// YYYY-mm-dd형식 변환
	DateSearch.makeFullDate = function(requestYear, requestMonth, requestDate) {
		requestMonth = requestMonth + 1;
		if (requestMonth < 10) {
			requestMonth = '0' + requestMonth;
		}
		if (requestDate < 10) {
			requestDate = '0' + requestDate;
		}
		DateSearch.date.fullDate = requestYear + '-' + requestMonth + '-'
				+ requestDate;

		return DateSearch.date.fullDate;
	}
}
DateSearch();







