<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="egovframework.diam.cmm.util.CommonUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String wr_id = request.getParameter("wr_id") != null ? request.getParameter("wr_id") : "";
	String contentId = request.getParameter("contentId") != null ? request.getParameter("contentId") : "";
	String board_id = request.getParameter("board_id") != null ? request.getParameter("board_id") : "";
	String mode = request.getParameter("mode") != null ? request.getParameter("mode") : "";
	
	CommonUtil commonUtil = new CommonUtil();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("contentId", request.getParameter("contentId"));
	param.put("search_value", request.getParameter("search_value"));
	param.put("search_type", request.getParameter("search_type"));
	param.put("search_cate", request.getParameter("search_cate"));
	param.put("page", request.getParameter("page"));
	
	String queryString = commonUtil.convertParam(param);	
%>
<c:choose>
	<c:when test="${!empty CONFIG_INFO.dm_url}">
		<c:choose>
			<c:when test="${fn:contains(CONFIG_INFO.dm_url, '/') }">
				<c:set value="/${fn:split(CONFIG_INFO.dm_url, '/')[1]}" var="root"/>
			</c:when>
			<c:otherwise>
				<c:set value="" var="root"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:set value="" var="root"/>
	</c:otherwise>
</c:choose>
<c:set var="wr_id" value="<%=wr_id%>"/>
<c:set var="contentId" value="<%=contentId%>"/>
<c:set var="board_id" value="<%=board_id%>"/>
<c:set var="mode" value="<%=mode%>"/>
<c:set var="queryString" value="<%=queryString%>"/>

<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js' />"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
function chk_password () {
	var chk_pass = $("#pass").val();
	if (chk_pass != "" && chk_pass != null) {
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
		$("#chk_pass").val(rsa.encrypt(chk_pass));
	} else {
		alert("비밀번호를 입력해주세요.");
		return false;
	}
	
	$.ajax({
        url : "/write/check_password.do",
        dataType : 'json',
        type : 'post',
        data : {"chkpass" : $("#chk_pass").val(), "wr_id" : "<c:out value='${wr_id}'/>", "contentId" : "<c:out value='${contentId}'/>"},
        success : function (data) {
            if (data.result == 'success') {
            	alert(data.notice);
            	var mode = "<c:out value='${mode}'/>";
            	
            	//비밀번호 인증 후 이동하는 함수는 thema/레이아웃폴더명/js/bbs.js에 선언되어 있다.
            	if (mode == "modify") {
            		window.opener.executeModify("<c:out value='${root}'/>","<c:out value='${wr_id}'/>", unescapeHtml("<c:out value='${queryString}' escapeXml='false'/>"));
            		self.close();
   		  		} else if (mode == "delete") {
            		if (confirm("정말 삭제하시겠습니까?")) {
            			window.opener.executeDelete("<c:out value='${root}'/>","<c:out value='${wr_id}'/>", "<c:out value='${board_id}'/>", unescapeHtml("<c:out value='${queryString}' escapeXml='false'/>"));
            		}
            		self.close();
            	} else if (mode == "comment_modify") {
            		self.close();
            		window.opener.executeCommentModify("<c:out value='${wr_id}'/>", "<c:out value='${board_id}'/>");
            	} else {
            		<c:choose>
	            		<c:when test="${queryString ne null && not empty queryString}">
	            			window.opener.location.href = '<c:out value="${root}"/>/index.do?command=view&wr_id=<c:out value="${wr_id}"/>&' + unescapeHtml('<c:out value="${queryString}" escapeXml="false"/>');
	            		</c:when>
	            		<c:otherwise>
	            			window.opener.location.href = '<c:out value="${root}"/>/index.do?command=view&wr_id=<c:out value="${wr_id}"/>&contentId=<c:out value="${contentId}"/>';
	            		</c:otherwise>
            		</c:choose>
	            	self.close();
            	}
            } else if (data.result == 'expired') {
            	alert(data.notice);
            	location.reload();
            } else {
            	alert(data.notice);
            }
            $("#chk_pass").val('');
        }, error : function() {
        	alert("비밀번호 확인정보 전송 중 정보가 누락되었습니다.");
        }
    });
}

function chk_cancel() {
	self.close();
}
</script>

<style>
#write_secret .secret_img {display:table; margin:0 auto 20px auto;}
#write_secret .write_password h1 {font-size:20px; text-align:center;}
#write_secret .write_password p {font-size:16px; text-align:center;}
#write_secret .write_password #pass {display:block; width:80%; margin:0 auto 15px auto;}
#write_secret .write_password .secret_btn {display:table; margin:0 auto;}
#write_secret .write_password .secret_btn a {display:inline-block; font-size:14px; font-weight:300; color:#fff; border:1px solid #c6c6c6; padding:7px 15px; text-decoration:none; margin:0 3px;}
#write_secret .write_password .secret_btn a.confirm {background:#4b449c;}
#write_secret .write_password .secret_btn a.cancel {background:#23272b;}
</style>

<div class="secret_box">
	<div class="secret_wrap" id="write_secret">
		<img src="<c:url value='/images/secret_icon.png'/>" alt="비밀글" class="secret_img"/>
		<div class="write_password">
			<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
			<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
			<h1><label for="chk_pass">비밀번호 확인</label></h1>
			<p>작성자와 관리자만 열람하실 수 있습니다. <br/> 작성자라면 비밀번호를 입력하세요.</p>
			<input type="password" class="form-control" id="pass" placeholder="비밀번호를 입력해주세요." autocomplete="new-password"/>
			<input type="hidden" name="chk_pass" id="chk_pass">
			<div class="secret_btn">
				<a href="javascript:chk_password();" class="confirm">확인</a>
				<a href="javascript:chk_cancel();" class="cancel">닫기</a>
			</div>
		</div>
	</div>
</div>
