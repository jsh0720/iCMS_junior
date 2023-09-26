<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>CMS</title>
<meta http-equiv="Content-Type" charset="UTF-8">
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery-easyui-texteditor/jquery.texteditor.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/colorpicker/colorpicker.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/tui.chart/dist/tui-chart-all.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/validator.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/se2/js/HuskyEZCreator.js' />"></script>

<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/themes/bootstrap/easyui.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/themes/icon.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/jquery-easyui-texteditor/texteditor.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/colorpicker/colorpicker.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/tui.chart/dist/tui-chart.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/page.css' />">
<script>
//var lib_url = '';
document.onkeydown = trapRefresh;
function trapRefresh() {
    if (event.keyCode == 116)
    {
        event.keyCode = 0;
        event.cancelBubble = true;
        event.returnValue = false;
        document.location.reload();
    }
}

$(function(){
 	$(".easyui-combobox").combobox({
		onLoadError: function(response){
			if (response.status == "303") {
				location.replace("/adm/login.do");
			} else {
 				$.messager.alert("경고", response.responseText, "warning");
			}
        }
	});
});

$(document).ajaxStart(function(){
	$("#wheel").show();
});

$(document).ajaxSend(function(){
	if (!window.opener) {
		window.parent.resetTimer();
	} else {
		opener.parent.resetTimer();
	}
});

$(document).ajaxStop(function(){
	$("#wheel").hide();
});

</script>
</head>
<style>
    #wheel {width: 100%;height: 100%;top: 0;left: 0;position: fixed;background:rgba(255,255,255,0.8);z-index:99;text-align: center; display: none;}
    #wheel >i{position: absolute;top: 50%;left: 50%; z-index: 100; transform: translate(-50%,-50%); font-size:3rem}
</style>
<body>
<div id="wheel">
    <i class="fa fa-spinner fa-pulse fa-fw"></i>
</div>