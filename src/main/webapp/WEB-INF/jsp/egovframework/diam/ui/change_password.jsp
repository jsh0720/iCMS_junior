<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<title>디자인아이엠 CMS</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/reset.css'/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/logins.css'/>"/>

<%-- <script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/jquery-1.4.3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/jquery-1.10.2.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery.easyui.min.js'/>"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
function fnChange() {
	var password = $("#dm_password").val();
	if (password != "" && password != null) {
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
		$("#dm_password").val(rsa.encrypt(password));
	}
	
	var param = $("#fm").serialize();
	$.ajax({
        url: '/adm/set_change_password.do',
        dataType:"json",
        type:"post",
        data:param,
        success: function(response){
        	alert(response.notice);
        	location.reload();        	
        }, error:function(request,status,error) {
        	alert("오류가 발생하였습니다. 관리자에게 문의주세요.");
        	location.reload();
        }
    });	
}

function fnUnchange() {
	location.href = "/adm/pass_change_password.do";
}

$(function(){
	$("#dm_password").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            fnChange();
        }
    });
});
</script>
</head>
<body>
	<div class="login">
	    <div class="logo"></div>
	    <div class="login_form" data-options="region:'center', border:false" >
	        <h2><b>비밀번호 변경</b></h2>
	        <form id="fm" method="post">
	            <input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
				<input type="hidden" id="RSAExponent" value="${RSAExponent}"/> 
	            <div class="change_password">
	            	<dl>
	            		<dt>변경비밀번호</dt>
	            		<dd><input type="password" name="dm_password" id="dm_password" placeholder="변경하실 비밀번호를 입력해주세요." autocomplete="new-password"/></dd>
	            	</dl>	            	
	            </div>
	            <div class="login_text">
	                <dl>
	                    <dt>※비밀번호는 영어 대/소문자,숫자,특수문자를 1개이상 포함하여 8자 이상 입력해주세요.</dt>
	                </dl>
	            </div>
	            <div class="change_button">
	            	<a href="javascript:;" class="easyui-linkbutton change" onclick="fnChange()">변경하기</a>
	            	<a href="javascript:;" class="easyui-linkbutton unchange" onclick="fnUnchange()">다음에 변경하기</a>
	            </div>
	        </form>
	    </div>
	</div>
</body>
</html>
