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
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/validator.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/easyui/jquery.easyui.min.js'/>"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script type="text/javascript">
function fnLogin() {
	var id = $("#id").val();
	var password = $("#pw").val();
		
	if ((id != "" && id != null) && (password != "" && password != null)) {
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
		$("#password").val(rsa.encrypt(password));
	} else {
		alert("아이디/비밀번호를 모두 입력해주세요.");
		return;
	}
	
    var param = $("#fm").serialize();
    $.ajax({
        url : "/adm/member_login.do",
        data : param,
        type : 'POST',
        cache : false,
        async : false,
        dataType : 'json',
        success : function(data) {
            if(data.result == "success") {
            	alert(data.notice);
                location.href = "/adm/main.do";
            } else if (data.result == "change") {
            	alert(data.notice);
            	location.href = "/adm/change_password.do";
            } else if (data.result == "expired") {
            	alert("아래와 같은 이유로 사용자 로그인에 실패하였습니다.\r\n -" + data.notice);
            	location.reload();
            } else if (data.result == "already") {
            	alert(data.notice);
            	location.reload();
            } else {
            	alert("아래와 같은 이유로 사용자 로그인에 실패하였습니다.\r\n -" + data.notice);
            	$("#password").val("");
            }
        }, error : function(request,status,error) {
        	alert(request.responseJSON.notice);
        }
    });
}

$(function(){
    $("#id").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            fnLogin();
        }
    });
    
    $("#pw").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            fnLogin();
        }
    });
    
    if (window.opener != null) {
    	self.close();
    	window.opener.location.href = '/adm/login.do';    	
    } else {
    	if (window !== top) {
        	window.parent.location.href = '/adm/login.do';
    	} else {
    		if (window.name != null && typeof window.name != "undefined" && window.name != '') {
    			self.close();
            	window.opener.location.href = '/adm/login.do';
    		}
    	}
    }
});
</script>
</head>
<body>
	<div class="login">
	    <div class="logo"></div>
	    <div class="login_form" data-options="region:'center', border:false" >
	        <h2><b>CMS ADMINISTRATOR</b></h2>
	        <form id="fm" method="post" autocomplete="off">
	            <input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
				<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
				<input type="hidden" id="password" name="password"/>				 
	            <div class="id_pw">
	                <dl>
	                    <dt>아이디</dt>
	                    <dd><input type="text" name="id" id="id" onkeyup="setEngPattern(this);" placeholder="아이디를 입력해주세요." maxlength="20"></dd>
	                </dl>
	                <dl>
	                    <dt>비밀번호</dt>
	                    <dd><input type="password" name="pw" id="pw" placeholder="비밀번호를 입력해주세요."/></dd>
	                </dl>
	            </div>
	            <div class="submit">
	                <a href="javascript:;" class="easyui-linkbutton" onclick="fnLogin()">로그인</a>
	            </div>	
	            <div class="login_text">
	                <dl>
	                    <dt>이용권한을 받으신분에 한하여 이용이 제한되오니, 관계자외 접근을 금합니다.</dt>
	                </dl>
	            </div>
	        </form>
	    </div>
	</div>		
</body>
</html>