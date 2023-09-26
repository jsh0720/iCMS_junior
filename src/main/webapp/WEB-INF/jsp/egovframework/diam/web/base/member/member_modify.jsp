<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:choose>
	<c:when test='${pageContext.request.scheme eq "https"}'>
		<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
	</c:when>
	<c:when test='${pageContext.request.scheme eq "http"}'>
		<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	</c:when>
</c:choose>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>

<script>

function checkForm() {	
	
	var dm_password = $("#dm_pw").val();
   	if (dm_password != "" && dm_password != null) {
   		var rsa = new RSAKey();
   		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
   		$("#dm_password").val(rsa.encrypt(dm_password));
   	} else {
   		$("input[name='chk_pw_flag']").val('1');
   	}

   	if ($.trim($("#dm_name").val()) == ''){
        alert('이름을 입력해주세요.');
        $("#dm_name").focus();
        return false;
    }
   	
   	<c:if test="${memberConfigVO.dm_require_nick eq '1' }">
	   	if ($.trim($("#dm_nick").val()) == "") {
	        alert('닉네임을 입력해주세요.');
	        $("#dm_nick").focus();
	        return false;
	    }
	   	
	   	if ($("input[name='chk_nick_flag']").val() == 0) {
	        alert('닉네임 중복확인을 해주세요.');
	        $("#confirm_nick").focus();
	        return false;
	    }
   	</c:if>
   	
   	<c:if test="${memberConfigVO.dm_require_sex eq '1' }">
	   	if (!$('input[name="dm_sex"]').is(':checked')) {
	        alert('성별을 선택해주세요.');
	        $('input[name="dm_sex"]:eq(0)').focus();
	        return false;
	    }
   	</c:if>
   	
   	<c:if test="${memberConfigVO.dm_require_birth eq '1' }">
	   	if ($("#dm_birth_date").val() == '') {
	        alert('생년월일을 입력해주세요.');
	        $('#dm_birth_date').focus();
	        return false;
	    }
	</c:if>
	
	<c:if test="${memberConfigVO.dm_require_email eq '1' }">
	    if ($("input[name='chk_email_flag']").val() == 0) {
	        alert('이메일 중복확인을 해주세요.');
	        $("#confirm_email").focus();
	        return false;
	    }
	</c:if>
	
	<c:if test="${memberConfigVO.dm_require_homepage eq '1' }">
		if ($.trim($("#dm_homepage").val()) == "") {
	        alert('홈페이지를 입력해주세요.');
	        $("#dm_homepage").focus();
	        return false;
	    }
	</c:if>
	
	if ($.trim($("#dm_hp1").val()) == '') {
        alert('휴대폰번호를 입력해주세요.');
        $("#dm_hp1").focus();
        return false;
    }
    if ($.trim($("#dm_hp2").val()) == '') {
        alert('휴대폰번호를 입력해주세요.');
        $("#dm_hp2").focus();
        return false;
    }
    if ($.trim($("#dm_hp3").val()) == '') {
        alert('휴대폰번호를 입력해주세요.');
        $("#dm_hp3").focus();
        return false;
    }
	
	<c:if test="${memberConfigVO.dm_require_tel eq '1' }">
		if ($.trim($("#dm_tel1").val()) == '') {
	        alert('전화번호를 입력해주세요.');
	        $("#dm_tel1").focus();
	        return false;
	    }
	    if ($.trim($("#dm_tel2").val()) == '') {
	        alert('전화번호를 입력해주세요.');
	        $("#dm_tel2").focus();
	        return false;
	    }
	    if ($.trim($("#dm_tel3").val()) == '') {
	        alert('전화번호를 입력해주세요.');
	        $("#dm_tel3").focus();
	        return false;
	    }
	</c:if>
	
	<c:if test="${memberConfigVO.dm_require_addr eq '1' }">
		if ($.trim($("#dm_zip").val()) == '') {
	        alert('우편번호를 입력해주세요.');
	        $("#dm_zip").focus();
	        return false;
	    }
	    if ($.trim($("#dm_addr1").val()) == ''){
	        alert('주소를 입력해주세요.');
	        $("#dm_addr1").focus();
	        return false;
	    }
	</c:if>
	
	<c:if test="${memberConfigVO.dm_require_introduce eq '1' }">
		if($("#dm_about_me").val() == '') {
			alert('자기소개를 입력해주세요.');
			$("#dm_about_me").focus();
			return false;
		}
	</c:if>

	<c:if test="${memberConfigVO.dm_require_recom eq '1' }">
		if($("#dm_recommend").val() == '') {
			alert('추천인을 입력해주세요.');
			$("#dm_recommend").focus();
			return false;
		}
	</c:if>
	
	var email = "";
    var emailRegExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]+$/i;

	if ($('#dm_email1').val() == "" || $('#dm_email1').val() == null) {
		alert("이메일 아이디를 입력해주세요");
		$("#dm_email1").focus();
		return false;
	}
	if ($('#dm_email2').val() == "" || $('#dm_email2').val() == null) {
		alert("이메일 주소를 입력해주세요");
		$("#dm_email2").focus();
		return false;
	}
	
	email = $('#dm_email1').val() + "@" + $('#dm_email2').val();
	if (!emailRegExp.test(email)) {
		alert("이메일을 형식에 맞게 입력해주세요. \n예) test@email.me");
        return false;
	}

    var dm_hp = '';
    if ($('#dm_hp1').length && $('#dm_hp2').length && $('#dm_hp3').length) {
        dm_hp = $('#dm_hp1').val() + "-" + $('#dm_hp2').val() + '-' + $('#dm_hp3').val();
    }
    var dm_tel = '';
    if ($('#dm_tel1').length && $('#dm_tel2').length && $('#dm_tel3').length) {
        dm_tel = $('#dm_tel1').val() + "-"+$('#dm_tel2').val() + '-' + $('#dm_tel3').val();
    }
    
    $('#memberVO').append(    	
		'<input type="hidden" name="dm_email" value="' + email + '" />' + 
		'<input type="hidden" name="dm_hp" value="' + dm_hp + '" />' + 
		'<input type="hidden" name="dm_tel" value="' + dm_tel + '" />'
    );
}

function leaveMember() {
	if (confirm("정말로 회원탈퇴하시겠습니까?")) {
		location.href= "<c:out value='${param.root }'/>/web/leave_member.do?dm_no=<c:out value='${memberVO.dm_no}'/>";	
	}	
}

$(function(){
	<c:if test="${DiamLoginVO.id eq null || empty DiamLoginVO.id}">
		alert("로그인이 필요한 서비스입니다. 로그인 후 진행해주세요.");
		document.location.href = "<c:out value='${param.root }'/>/index.do";
	</c:if>
	
	<c:if test="${memberVO.dm_nick ne '' || memberVO.dm_nick ne null}">
		$('#dm_nick').addClass('is-valid').prop('readonly', true);
	    $('#confirm_nick').prop('disabled', true);
	    $('input[name="chk_nick_flag"]').val(1);
	    $('#confirm_nick').hide();
	    $('#confirm_nick_reset').show();
	</c:if>
	
	<c:if test="${memberVO.dm_email ne '' || memberVO.dm_email ne null }">
		$('#dm_email1,#dm_email2').addClass('is-valid').prop('readonly', true);
	    $('#confirm_email').prop('disabled', true);
	    $('#dm_email3').hide();
	    $('input[name="chk_email_flag"]').val(1);
	    $('#confirm_email').hide();
	    $('#confirm_email_reset').show();
	</c:if>
	
	$('#confirm_nick_reset').on('click', function () {
        $('#dm_nick').removeClass('is-valid').prop('readonly', false);
        $('#confirm_nick').prop('disabled', false);
        $('input[name="chk_nick_flag"]').val(0);
        $('#confirm_nick').show();
        $('#confirm_nick_reset').hide();
        $('#dm_nick').focus();
    });
	
	$('#confirm_nick').on('click', function () {
        if ($.trim($('#dm_nick').val()) == '') {
            alert("닉네임을 입력해주세요.");
            return false;
        }
        
        if($('#dm_nick').val() == '<c:out value="${memberVO.dm_nick}" />'){
        	$('#dm_nick').addClass('is-valid').prop('readonly', true);
    	    $('#confirm_nick').prop('disabled', true);
    	    $('input[name="chk_nick_flag"]').val(1);
    	    $('#confirm_nick').hide();
    	    $('#confirm_nick_reset').show();
    	    return false;
        }

        $.ajax({
            url: "/web/dupChk.do",
            data: {dm_nick: $("#dm_nick").val(), command:"nick", dm_no: $("#dm_no").val()},
            type: 'POST',
            cache: false,
            async: false,
            dataType: 'json',
            success: function(data) {
                if (data.result == 'success') {
                    alert(data.notice);
                    $('#dm_nick').addClass('is-valid').prop('readonly', true);
                    $('#confirm_nick').prop('disabled', true);
                    $('input[name="chk_nick_flag"]').val(1);
                    $('#confirm_nick').hide();
                    $('#confirm_nick_reset').show();
                } else if (data.result == 'dup') {
                    alert(data.notice);
                    $('input[name="chk_nick_flag"]').val(0);
                } else {
                    alert("사용자 닉네임 중복확인에 실패하였습니다.", "닉네임 중복확인 실패");
                    $('input[name="chk_nick_flag"]').val(0);
                }
            },
            error: function(request, status, error) {
	            alert("status : " + request.status + "\n" + "message:" + request.responseJSON.notice);
	        }
        });
    });
	
	$('#confirm_email_reset').on('click', function () {
        $('#dm_email1, #dm_email2').removeClass('is-valid').prop('readonly', false);
        $('#dm_email3').show();
        $('#confirm_email').prop('disabled', false);
        $('input[name="chk_email_flag"]').val(0);
        $('#confirm_email').show();
        $('#confirm_email_reset').hide();
        $('#dm_email1').focus();
    });
	
	$("#dm_pw, #dm_password_confirm").keyup(function(){
        var pwd1=$("#dm_pw").val();
        var pwd2=$("#dm_password_confirm").val();
        $("input[name='chk_pw_flag']").val(0);

        if (pwd1 != "" || pwd2 != ""){
            if (pwd1 == pwd2){
                $("input[name='chk_pw_flag']").val(1);
            }else {
                $("input[name='chk_pw_flag']").val(0);
            }
        }
    });
	
	$('#confirm_email').on('click', function () {
        if ($.trim($('#dm_email1').val()) == '' || $.trim($('#dm_email2').val()) == '') {
            alert("이메일을 입력해주세요.");
            return false;
        }
        var email = $.trim($('#dm_email1').val()) + '@' + $.trim($('#dm_email2').val());
        
        if(email == '<c:out value="${memberVO.dm_email}" />'){
        	$('#dm_email1,#dm_email2').addClass('is-valid').prop('readonly', true);
            $('#confirm_email').prop('disabled', true);
            $('#dm_email3').hide();
            $('input[name="chk_email_flag"]').val(1);
            $('#confirm_email').hide();
            $('#confirm_email_reset').show();
            return false;
        }
        
        var emailRegExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]+$/i;
        if (email.match(emailRegExp) === null) {
            alert("이메일을 형식에 맞게 입력해주세요. \n예) test@email.me");
            return false;
        }

        $.ajax({
            url: "/web/dupChk.do",
            data: {dm_email: $('#dm_email1').val() + '@' + $('#dm_email2').val(), command:"email"},
            type: 'POST',
            cache: false,
            async: false,
            dataType: 'json',
            success: function(data) {
                if (data.result == 'success') {
                    alert(data.notice);
                    $('#dm_email1,#dm_email2').addClass('is-valid').prop('readonly', true);
                    $('#confirm_email').prop('disabled', true);
                    $('#dm_email3').hide();
                    $('input[name="chk_email_flag"]').val(1);
                    $('#confirm_email').hide();
                    $('#confirm_email_reset').show();
                } else if (data.result == 'dup') {
                    alert(data.notice);
                    $('input[name="chk_email_flag"]').val(0);
                } else {
                    alert("아래와 같은 이유로 사용자 이메일 중복확인에 실패하였습니다.\n[ " + data.notice + " ]\n관리자에게 문의하세요.", "이메일 중복확인 실패");
                    $('input[name="chk_email_flag"]').val(0);
                }
            },
            error: function(xhr, status, error) {
            }
        });
    });
    
	$("#dm_birth_date").datepicker({
        dateFormat: "yy-mm-dd", // 텍스트 필드에 입력되는 날짜 형식.
        yearRange: 'c-100:c',
        showButtonPanel: true
    });
	
	$('#dm_email3').on('change', function() {
        $('#dm_email2').val($(this).val());
        $('#dm_email1').focus();
    });
	
	$("#fnSave").on('click', function(){
		if (confirm("입력한 정보로 회원정보를 수정하시겠습니까?")) {
			$("#memberVO").submit();
			$("#dm_password").val("");
		}
	});
	
});
</script>

<div id="mbRegis" class="mb_join" >
	<h3>회원가입</h3>
	<div class="mb_inner mb_regis">
		<form id="memberVO" name="memberVO" method="post" action="<c:out value='${param.root }'/>/web/set_member.do" onsubmit="return checkForm();" autocomplete="off">
			<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
			<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
			<input type="hidden" name="type" value="update"/>
			<input type="hidden" name="dm_level" value="<c:out value="${memberVO.dm_level }" />"/>
			
	        <input type="hidden" name="chk_pw_flag" id="chk_pw_flag" value="0" />
	        <input type="hidden" name="chk_nick_flag" id="chk_nick_flag" value="0" />
	        <input type="hidden" name="chk_hp_flag" id="chk_hp_flag" value="0" />
	        <input type="hidden" name="chk_email_flag" id="chk_email_flag" value="0" />
	        <input type="hidden" name="dm_password" id="dm_password" />
	        <input type="hidden" name="dm_no" id="dm_no" value="<c:out value="${memberVO.dm_no }" />" />
				
			<p aria-hidden="true" class="mb15"><span class="required">*</span>는 필수 입력 항목입니다.</p>
			<div class="form-group">
				<label for="dm_id">아이디 <span class="required">필수</span></label>

				<input type="text" name="dm_id" id="dm_id" class="form-control" maxlength="20" value="<c:out value="${memberVO.dm_id }"/>" readonly/>
		
				<small class="form-text">
					<span class="text-muted">아이디는 변경하실 수 없습니다.</span>
                	<span id="confirm_id_msg"></span>
				</small>
			</div>
			<div class="form-row">
				<div class="col">
					<div class="form-group">
						<label for="dm_pw">비밀번호<span class="required">필수</span></label>
						<input type="password" id="dm_pw" class="form-control" maxlength="20" />
						<small class="form-text text-muted">영문 대/소문자,숫자,특수문자를 1개이상 포함하여 8~20자 입력해주세요.</small>
					</div>
				</div>
				<div class="col">
					<div class="form-group">
						<label for="dm_password_confirm">비밀번호 확인<span class="required">필수</span></label>
						<input type="password" id="dm_password_confirm" class="form-control" maxlength="20" />
						<small class="form-text text-muted">비밀번호를 다시 입력해주세요.</small>
					</div>
				</div>
			</div>

			
			<div class="form-group">
				<label for="dm_name">이름 <span class="required">필수</span></label>
				<input type="text" name="dm_name" id="dm_name" class="form-control" maxlength="20" value="<c:out value="${memberVO.dm_name }"/>" onkeyup="setNamePattern(this);">
				<small class="form-text"><span class="text-muted">한글, 영문으로 1자 이상 20자 이하로 입력해주세요.</span></small>
			</div>
			
			<c:if test="${memberConfigVO.dm_use_nick eq '1' }">
				<div class=form-group>
					<label for="dm_nick">닉네임<c:if test="${memberConfigVO.dm_require_nick eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-row align-items-center">
						<div class="col">
							<input type="text" name="dm_nick" id="dm_nick" class="form-control" maxlength="20" value="<c:out value="${memberVO.dm_nick }"/>"/>
						</div>
						<div class="col-auto">
							<button type="button" class="form-control btn btn_inline" id="confirm_nick">닉네임 중복체크</button>
							<button type="button" class="form-control btn btn_inline" id="confirm_nick_reset" style="display: none;">다시 작성</button>
						</div>
					</div>
					<small class="form-text"><span class="text-muted">한글, 영문으로 1자 이상 20자 이하로 입력해주세요.</span></small>
				</div>
			</c:if>
			<c:if test="${memberConfigVO.dm_use_sex eq '1' }">
				<div class="form-group">
					<label for="dm_sex_M">성별<c:if test="${memberConfigVO.dm_require_sex eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-inline">
						<div class="custom-control custom-radio mr-3">
							<input type="radio" name="dm_sex" id="dm_sex_M" class="custom-control-input" value="M" <c:if test="${memberVO.dm_sex eq 'M' }">checked</c:if> />
							<label for="dm_sex_M" class="custom-control-label">남자</label>
						</div>
						<div class="custom-control custom-radio mr-3">
							<input type="radio" name="dm_sex" id="dm_sex_F" class="custom-control-input" value="F" <c:if test="${memberVO.dm_sex eq 'F' }">checked</c:if> />
							<label for="dm_sex_F" class="custom-control-label">여자</label>
						</div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_birth eq '1' }">
				<div class="form-group">
					<label for="dm_birth_date">생년월일<c:if test="${memberConfigVO.dm_require_birth eq '1' }"><span class="required">필수</span></c:if></label>
					<input type="text" name="dm_birth_date" id="dm_birth_date" class="form-control" autocomplete="off" value="<c:out value="${memberVO.dm_birth_date }"/>" />
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_email eq '1' }">
				<div class="form-group">
					<label for="dm_email1">이메일<c:if test="${memberConfigVO.dm_require_email eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-row align-items-center">
						<div class="col">
							<label for="dm_email1" class="sr-only">메일 아이디</label>
							<input type="text" id="dm_email1" class="form-control" value="${fn:split(memberVO.dm_email, '@')[0] }"/>
						</div>
						<div class="col-auto">@</div>
						<div class="col">
							<label for="dm_email2" class="sr-only">메일주소</label>
							<input type="text" id="dm_email2" class="form-control" value="${fn:split(memberVO.dm_email, '@')[1] }"/>
						</div>
						<div class="col-auto pl-0">
							<select id="dm_email3" class="form-control custom-select">
								<option value="">직접입력</option>
								<option value="naver.com">naver.com</option>
								<option value="daum.net">daum.net</option>
								<option value="hanmail.net">hanmail.net</option>
								<option value="gmaill.com">gmaill.com</option>
								<option value="hotmail.com">hotmail.com</option>
								<option value="outlook.com">outlook.com</option>
								<option value="live.com">live.com</option>
								<option value="nate.com">nate.com</option>
								<option value="yahoo.com">yahoo.com</option>
								<option value="empas.com">empas.com</option>
								<option value="korea.com">korea.com</option>
								<option value="dreamwiz.com">dreamwiz.com</option>
							</select>					
						</div>
						<div class="col-12 col-sm-auto mt-1 mt-sm-0">
                    		<button type="button" class="form-control btn btn-block btn_inline" id="confirm_email">이메일 중복체크</button>
                    		<button type="button" class="form-control btn btn_inline" id="confirm_email_reset" style="display: none;">다시 작성</button>
                		</div>
					</div>
					<div class="custom-control custom-checkbox mt-2">
                		<input type="checkbox" name="dm_mailling" id="dm_mailling" class="custom-control-input" value="1" <c:if test="${memberVO.dm_mailling eq '1' }">checked</c:if> />
                		<label for="dm_mailling" class="custom-control-label">메일 수신 (회원에 관련된 중요 메일은 수신여부와 상관없이 발송됩니다.)</label>
            		</div>
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_homepage eq '1' }">
				<div class="form-group">
					<label for="dm_homepage">홈페이지<c:if test="${memberConfigVO.dm_require_homepage eq '1' }"><span class="required">필수</span></c:if></label>
            		<input type="text" name="dm_homepage" id="dm_homepage" class="form-control" placeholder="http://" value="<c:out value="${memberVO.dm_homepage }"/>" />
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_hp eq '1' }">
				<div class="form-group">
					<label for="dm_hp1">휴대전화<c:if test="${memberConfigVO.dm_require_hp eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-row align-items-center">
						<div class="col">
							<label for="dm_hp1" class="sr-only">휴대전화번호 앞자리</label>
							<c:set var="hp1" value="${fn:split(memberVO.dm_hp, '-')[0] }"></c:set>
							<select id="dm_hp1" class="form-control custom-select">
								<option value="" <c:if test="${hp1 eq '' || hp1 eq null}">selected</c:if>>선택</option>
								<option value="010" <c:if test="${hp1 eq '010' }">selected</c:if>>010</option>
								<option value="011" <c:if test="${hp1 eq '011' }">selected</c:if>>011</option>
								<option value="016" <c:if test="${hp1 eq '016' }">selected</c:if>>016</option>
								<option value="017" <c:if test="${hp1 eq '017' }">selected</c:if>>017</option>
								<option value="018" <c:if test="${hp1 eq '018' }">selected</c:if>>018</option>
								<option value="019" <c:if test="${hp1 eq '019' }">selected</c:if>>019</option>
								
							</select>
						</div>
						<div class="col-auto">-</div>
						<div class="col">
							<label for="dm_hp2" class="sr-only">휴대전화번호 중간자리</label>
                    		<input type="text" id="dm_hp2" class="form-control" maxlength="4" value="${fn:split(memberVO.dm_hp, '-')[1] }" />
                		</div>
						<div class="col-auto">-</div>
		                <div class="col">
		                    <label for="dm_hp3" class="sr-only">휴대전화번호 뒷자리</label>
		                    <input type="text" id="dm_hp3" class="form-control" maxlength="4" value="${fn:split(memberVO.dm_hp, '-')[2] }" />
		                </div>
					</div>
					<div class="custom-control custom-checkbox mt-2">
                		<input type="checkbox" name="dm_sms" id="dm_sms" class="custom-control-input" value="1" />
                		<label for="dm_sms" class="custom-control-label">문자 수신 (회원에 관련된 중요 문자는 수신여부와 상관없이 발송됩니다.)</label>
            		</div>
					
				</div>
			</c:if>
			
			
			<c:if test="${memberConfigVO.dm_use_tel eq '1' }">
				<div class="form-group">
					<label for="dm_tel1">전화번호<c:if test="${memberConfigVO.dm_require_tel eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-row align-items-center">
						<div class="col">
							<label for="dm_tel1" class="sr-only">전화번호 앞자리</label>
							<c:set var="tel1" value="${fn:split(memberVO.dm_tel, '-')[0] }"></c:set>
							<select id="dm_tel1" class="form-control custom-select">
								<option value="" <c:if test="${tel1 eq '' || tel1 eq null}">selected</c:if>>지역번호</option>
								<option value="02" <c:if test="${tel1 eq '02'}">selected</c:if>>02</option>
								<option value="031" <c:if test="${tel1 eq '031'}">selected</c:if>>031</option>
								<option value="032" <c:if test="${tel1 eq '032'}">selected</c:if>>032</option>
								<option value="033" <c:if test="${tel1 eq '033'}">selected</c:if>>033</option>
								<option value="041" <c:if test="${tel1 eq '041'}">selected</c:if>>041</option>
								<option value="042" <c:if test="${tel1 eq '042'}">selected</c:if>>042</option>
								<option value="043" <c:if test="${tel1 eq '043'}">selected</c:if>>043</option>
								<option value="044" <c:if test="${tel1 eq '044'}">selected</c:if>>044</option>
								<option value="051" <c:if test="${tel1 eq '051'}">selected</c:if>>051</option>
								<option value="052" <c:if test="${tel1 eq '052'}">selected</c:if>>052</option>
								<option value="053" <c:if test="${tel1 eq '053'}">selected</c:if>>053</option>
								<option value="054" <c:if test="${tel1 eq '054'}">selected</c:if>>054</option>
								<option value="055" <c:if test="${tel1 eq '055'}">selected</c:if>>055</option>
								<option value="061" <c:if test="${tel1 eq '061'}">selected</c:if>>061</option>
								<option value="062" <c:if test="${tel1 eq '062'}">selected</c:if>>062</option>
								<option value="063" <c:if test="${tel1 eq '063'}">selected</c:if>>063</option>
								<option value="064" <c:if test="${tel1 eq '064'}">selected</c:if>>064</option>
								<option value="070" <c:if test="${tel1 eq '070'}">selected</c:if>>070</option>
								<option value="0502" <c:if test="${tel1 eq '0502'}">selected</c:if>>0502</option>
								<option value="0504" <c:if test="${tel1 eq '0504'}">selected</c:if>>0504</option>
								<option value="0505" <c:if test="${tel1 eq '0505'}">selected</c:if>>0505</option>
								<option value="0506" <c:if test="${tel1 eq '0506'}">selected</c:if>>0506</option>
								<option value="0303" <c:if test="${tel1 eq '0303'}">selected</c:if>>0303</option>
							</select>
						</div>
						<div class="col-auto">-</div>
						<div class="col">
							<label for="dm_tel2" class="sr-only">전화번호 중간자리</label>
                    		<input type="text" id="dm_tel2" class="form-control" maxlength="4" value="${fn:split(memberVO.dm_tel, '-')[1] }" />
                		</div>
						<div class="col-auto">-</div>
		                <div class="col">
		                    <label for="dm_tel3" class="sr-only">전화번호 뒷자리</label>
		                    <input type="text" id="dm_tel3" class="form-control" maxlength="4" value="${fn:split(memberVO.dm_tel, '-')[2] }" />
		                </div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_addr eq '1' }">
				<div class="form-group">
					<label for="dm_zip">주소<c:if test="${memberConfigVO.dm_require_addr eq '1' }"><span class="required">필수</span></c:if></label>
					<div class="form-row align-items-center">
						<div class="col-auto">
							<label for="dm_zip" class="sr-only">우편번호</label>
							<input type="text" name="dm_zip" id="dm_zip" class="form-control" size="6" maxlength="6" placeholder="우편번호" value="<c:out value="${memberVO.dm_zip }"/>" readonly />
						</div>
						<div class="col-auto">
							<button type="button" class="btn btn_inline dm_zipcode" onclick="win_zip('memberVO', 'dm_zip', 'dm_addr1', 'dm_addr2', 'dm_addr3', 'dm_addr_jibeon');">주소 검색</button>
						</div>
					</div>
					
					<label for="dm_addr1" class="sr-only">기본 주소</label>
		            <input type="text" name="dm_addr1" id="dm_addr1" class="form-control dm_addr mt-1" value="<c:out value="${memberVO.dm_addr1 }"/>" placeholder="기본주소" />
		            <label for="dm_addr2" class="sr-only">상세 주소</label>
		            <input type="text" name="dm_addr2" id="dm_addr2" class="form-control dm_addr mt-1" value="<c:out value="${memberVO.dm_addr2 }"/>" placeholder="상세주소" />
		            <label for="dm_addr3" class="sr-only">참고 주소</label>
		            <input type="text" name="dm_addr3" id="dm_addr3" class="form-control dm_addr mt-1" value="<c:out value="${memberVO.dm_addr3 }"/>" placeholder="참고항목" readonly />
		            <input type="hidden" name="dm_addr_jibeon" id="dm_addr_jibeon" value="<c:out value="${memberVO.dm_addr_jibeon }"/>">
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_introduce eq '1' }">
				<div class="form-group">
					<label for="dm_profile">자기소개<c:if test="${memberConfigVO.dm_require_introduce eq '1' }"><span class="required">필수</span></c:if></label>
					<textarea name="dm_about_me" id="dm_about_me" class="form-control" rows="5">${memberVO.dm_about_me }</textarea>
				</div>
			</c:if>
			
			<c:if test="${memberConfigVO.dm_use_recom eq '1' }">
				<div class="form-group">
					<label for="dm_recommend">추천인<c:if test="${memberConfigVO.dm_require_recom eq '1' }"><span class="required">필수</span></c:if></label>
					<input type="text" name="dm_recommend" id="dm_recommend" class="form-control" maxlength="20" value="<c:out value="${memberVO.dm_recommend }"/>" readonly />
					<small class="form-text"><span class="text-muted">추천인은 변경하실 수 없습니다.</span></small>
				</div>
			</c:if>
			
			<div class="btn_wrap">
				<button type="button" class="btn btn_submit" id="fnSave">정보수정</button>
				<c:if test="${!DiamLoginVO.is_admin }">
					<button type="button" class="btn btn_revoke" onclick="leaveMember();">회원탈퇴</button>				
				</c:if>
				
			</div>
		</form>
	</div>
</div>
