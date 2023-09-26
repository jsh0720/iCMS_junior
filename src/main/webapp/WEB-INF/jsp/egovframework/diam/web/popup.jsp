<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
function onClose(dm_id , dm_type , expired) {
	
	var checked = document.querySelector("#chk_expired"+dm_id).checked;
	
	if(expired > -1 && checked) {
		closeCooKie(dm_id, expired);
	}
	if(dm_type == "1") {
		this.close();
	} else {
		document.getElementById('draggable_'+dm_id).style.display = "none";
	}
}

function closeCooKie(popId, expired) {
	setCookie("popup_"+popId , "done" , expired);
}


function setCookie(name, value, expirehours) {
	var todayDate = new Date();
	todayDate.setHours(todayDate.getHours() + expirehours);
	document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toUTCString() + ";"
}
</script>
<html>
<head>
	<title>${popupVO.dm_popup_nm}</title>
	<c:if test="${popupVO.dm_popup_type eq '1'}">
		<link rel="stylesheet" href="<c:url value='${layout_path}/css/reset.css' />"/>
		<link rel="stylesheet" href="<c:url value='${layout_path}/css/common.css' />"/>
		<link rel="stylesheet" href="<c:url value='${layout_path}/css/main.css' />"/>
		<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/bootstrap-4.6.0/css/bootstrap.min.css'/>"/>
	</c:if>
</head>
<body style="margin:0;padding:0;">
	<div class="layer_image_area">
		<c:choose>
			<c:when test="${popupVO.dm_link ne null && popupVO.dm_link ne ''}">
				<a href="${popupVO.dm_link}" target="${popupVO.dm_link_type}">
					<img src="/resources/popup/<c:out value='${popupVO.dm_popup_img}'/>" onerror="this.src='/images/no_image.png'"/>
				</a>
			</c:when>
			<c:otherwise>
				<img src="/resources/popup/<c:out value='${popupVO.dm_popup_img}'/>" onerror="this.src='/images/no_image.png'"/>
			</c:otherwise>
		</c:choose>
		<div class="layer_btns_area layer_popup_ctrl">
			<div class="layer_">
				<div class="custom-control custom-checkbox mr-3">
					<input type="checkbox" name="chk_expired" value="secret" id="chk_expired${popupVO.dm_id}" class="check-cus pop_chk custom-control-input">
					<label class="close_pop_time custom-control-label" for="chk_expired${popupVO.dm_id}"><em></em>${popupVO.dm_popup_expired}시간동안 보지않기</label>
				</div>
			</div>
			<%-- <c:if test="${popupVO.dm_popup_type ne '1'}"> --%>
				<a onclick="onClose('${popupVO.dm_id}', '${popupVO.dm_popup_type}', '${popupVO.dm_popup_expired}');" class="close_pop">닫기</a>
			<%-- </c:if> --%>
		</div>
	</div>
</body>
</html>