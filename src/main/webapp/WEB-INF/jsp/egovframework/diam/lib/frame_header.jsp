<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.*" %>
<%@ page import="egovframework.diam.cmm.util.AdminMenuAssist" %>
<%@ page import="egovframework.diam.cmm.model.LoginVO" %>
<%@ page import="egovframework.diam.biz.model.admin.Dm_access_admin_menu_vo" %>
<%
	AdminMenuAssist assist = new AdminMenuAssist(request);
	LoginVO loginVO = (LoginVO) request.getAttribute("DiamLoginVO");
	
	List<Dm_access_admin_menu_vo> rootChildList = assist.getMenuList("1", loginVO.getDm_level());
	//out.print(rootChildList.size());
%>
<!DOCTYPE html>
<html>
<head>
<title>디자인아이엠 CMSFramework</title>
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/themes/bootstrap/easyui.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/themes/icon.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/easyui/jquery-easyui-texteditor/texteditor.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/admin.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/diam/tui.chart/dist/tui-chart.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/grid.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/main.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/reset.css' />">
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<%-- <script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/jquery-1.10.2.js' />"></script> --%>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery-easyui-texteditor/jquery.texteditor.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery.waypoints.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery.nicescroll.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery.counterup.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/validator.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/tui.chart/dist/tui-chart-all.min.js' />"></script>
</head>
<script type="text/javascript">
var index = 0;
function addTab(nm, src, close, icon, page) {
	// 탭 중복 열림 방지 로직 start
	var tabs = $(".tabs").find("li");
	var titles = new Array();
	$.each(tabs, function(i, obj){
		var title = $(obj).find(".tabs-title").text();
		titles.push(title);
	});
	
	if (titles.indexOf(nm) > -1) {
		$.each(tabs, function(i, obj){
			if ($(obj).find(".tabs-title").text() == nm) {
				$(obj).trigger("click");
				return false;
			}
		});
		return;
	}
	// end
	
	index+=1;
	
	var nDocumentWidth = $(window).width();
	var nDocumentHeight = $(window).height()-100;
	var nScrollWidth = 0, nScrollHeight = 0;
	
	$('#m_frame').tabs('add',{
	    id: index,
	    title: nm,
	    content: '<div data-options="closable:true,fit:true" class="wrap-tabs-content"><iframe id="tab_'+ index +'" name="tab_'+ index +'" scrolling="auto" frameborder="0"  src="' + src + '" style="width:100%;height:' + nDocumentHeight +'px"></iframe></div>',
	    closable: close,
	    iconCls: icon,
	    fit:true
	});
	
}
	
function closeTab(idx) {
    var tab = $('#m_frame').tabs('getSelected');
    if (tab) {
        if (!idx) idx = $("#m_frame").tabs('getTabIndex', tab);
        $("#m_frame").tabs('close', idx);
    }
}
		
function fnLogout() {
    if(confirm("로그아웃 하시겠습니까?")) {
    	$.ajax({
    		url : '/adm/member_logout.do',
    		type : 'POST',
    		dataType : 'json',
    		async : false,
    		success : function(data) {
    			if (data.result == 'success') {
    				alert(data.notice);
    				location.href='/adm/main.do';
    			} else {
    				alert(data.notice);
    			}
    		}, error : function(request, status, error) {
    			alert(data.notice);
    		}    		
    	});
    }
}
	 
document.onkeydown = trapRefresh;
function trapRefresh() {
    if (event.keyCode == 116) {
        event.keyCode = 0;
        event.cancelBubble = true;
        event.returnValue = false;
        var p = $('#m_frame').tabs('getTab', 0);
        p.panel('refresh');
    }
}

var expired = "${requestScope.DiamLoginVO.expired}" || "";
var min = "";
var sec = "";
var timer = null;
var sp = $("#timer");

timer = setInterval(newTimer, 1000);

function newTimer() {
	if (expired != "" && expired != null) {
		if (expired > 0) {
			--expired;
			
			min = parseInt(expired / 60);
			sec = expired % 60;
			if (min < 10) {
				min = "0" + min;
			}
			if (sec < 10) {
				sec = "0" + sec;
			}
			
			$("#timer").text(min+":"+sec);
			
			if (min == "00" && sec == "00") {
				$("#timer").css("color", "red");
			}
			
		} else {
			clearInterval(timer);
		}
	}
}

function resetTimer() {
	clearInterval(timer);
	expired = "${requestScope.DiamLoginVO.expired}" || "";
	timer = setInterval(newTimer, 1000);
}
 
</script>
<body>
<!-- <body oncontextmenu='return false' onselectstart='return false' ondragstart='return false'> -->
<div class="easyui-layout" fit="true">
    <div class="sidebar" data-options="region:'west',split:false,border:true,hideCollapsedContent:false,collapsed:false">
        <div class="easyui-accordion" data-options="multiple:false,border:false">
        	<% for (int i=0 ; i < rootChildList.size() ; i++) {	%>
        		<div title="<%=rootChildList.get(i).getDm_title()%>" data-options="collapsed:true">
        			<%	List<Dm_access_admin_menu_vo> childList = assist.getMenuList(rootChildList.get(i).getDm_id(), loginVO.getDm_level());
        				for (int j=0 ; j < childList.size() ; j++) {
        					if (!"".equals(childList.get(j).getDm_link_url())) {
        			%>
		        				<p class="child" onclick="javascript:addTab('<%=childList.get(j).getDm_title()%>', '<%=childList.get(j).getDm_link_url()%>', true, '', '')">
		        					- <%=childList.get(j).getDm_title()%>
		        				</p>
        			<% 		} else { %>
        						<p> - <%=childList.get(j).getDm_title()%></p>
        			<% 		} 
        				}
        			%>
        		</div>        
        	<% } %>
        </div>
    </div>
    <div class="header" data-options="region:'north'">
        <div class="logo">
            <a href="/adm/main.do">
            	<img src="<c:url value='/images/icms_wt.png' />"/>
            </a>
        </div>
        <div class="mnb">
        	<div class="user" id="timerDiv">
        		<span id="timer" style="letter-spacing: 0.025em;">30:00</span>
        	</div>
            <div class="user">
                <a href="javascript:fnLogout();">
                	<span><c:out value='${DiamLoginVO.getName()}'/> 님</span> (<span><c:out value='${DiamLoginVO.getId()}'/></span>) <em>로그아웃</em>
                </a>
            </div>
            <div class="site">
            	<c:choose>
            		<c:when test="${main_domain_url ne null && not empty main_domain_url}">
            			<a class="homp" href="//<c:out value='${main_domain_url}'/>" target="_blank">내사이트</a>
            		</c:when>
            		<c:otherwise>
            			<a class="homp" href="javascript:alert('메인도메인으로 등록된 도메인이 없습니다.');">내사이트</a>
            		</c:otherwise>
            	</c:choose>                
                <a class="css" href="/resources/manual/user_manual.pdf" target="_blank">메뉴얼 다운로드</a>
            </div>
        </div>
    </div>