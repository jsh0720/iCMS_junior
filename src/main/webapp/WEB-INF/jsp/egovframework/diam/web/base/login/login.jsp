<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
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
	
    var form_data = $("#fm").serialize();
    $.ajax({
        url : "<c:out value='${param.root}'/>/web/login.do",
        data : form_data,
        type : 'POST',
        cache : false,
        async : false,
        dataType : 'json',
        success : function(data) {
            if(data.result == "success") {
            	alert(data.notice);
                location.href = "<c:out value='${param.root}'/>/index.do";
            } else if (data.result == "expired") {
            	alert("아래와 같은 이유로 사용자 로그인에 실패하였습니다.\r\n -" + data.notice);
            	location.reload();
            } else {
            	alert("아래와 같은 이유로 사용자 로그인에 실패하였습니다.\r\n -" + data.notice);
            	$("#password").val("");
            }
        }, error : function(request,status,error) {
        	alert("오류가 발생하였습니다. 관리자에게 문의주세요.");
        }
    });
}

$(function(){
	<c:if test="${DiamLoginVO.id ne null && not empty DiamLoginVO.id}">
		alert("이미 로그인 중입니다.");
		document.location.href = "<c:out value='${param.root}'/>/index.do";
	</c:if>
	
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
});
</script>
<div id="mbLogin" class="mb_form">
	<h3>로그인</h3>
	<div class="mb_inner mb_login">
		<form id="fm" name="fm" autocomplete="off" method="post">
			<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
			<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
			<input type="hidden" id="password" name="password"/>
			
			<fieldset class="">
				<legend class="">회원 로그인</legend>
				<div class="form-group id_area">
					<label for=id>아이디</label>
					<input type="text" name="id" id="id" class="form-control" placeholder="아이디를 입력해주세요."/>
				</div>
				<div class="form-group pw_area">
                	<label for="pw">비밀번호</label>
                	<input type="password" id="pw" class="form-control" placeholder="비밀번호" />
				</div>
				<div class="custom-control custom-checkbox ck_area">
            	</div>
			</fieldset>
			<div class="btn_wrap">
				<button type="button" onclick="fnLogin();" class="btn btn_submit">로그인</button>
			</div>
			<div class="lnk_wrap">
				<a href="<c:out value='${param.root}'/>/index.do?contentId=<c:out value='${member_uid }'/>&command=forgot_id">아이디 찾기</a>
				<a href="<c:out value='${param.root}'/>/index.do?contentId=<c:out value='${member_uid }'/>&command=forgot_pw">비밀번호 찾기</a>
				<a href="<c:out value='${param.root}'/>/index.do?contentId=<c:out value='${member_uid }'/>&command=terms">회원가입</a>
			</div>
		</form>
		
	</div>
</div>

