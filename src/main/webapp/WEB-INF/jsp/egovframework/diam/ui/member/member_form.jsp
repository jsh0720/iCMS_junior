<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_no = request.getParameter("dm_no") != null ? request.getParameter("dm_no") : "";
	String type = "";
	if (!"".equals(dm_no) && dm_no != null) {
		type = "update";
	} else {
		type = "insert";
	}
%>
<c:set var="dm_no" value="<%=dm_no%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />
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
function fnSave() {
	if ($("#dm_id").attr("readonly") != "readonly") {
		$.messager.alert("경고", "아이디 중복 확인 후 진행해주세요.", "warning");
		$("#dm_id").focus();
		return;
	}
	
	if ($.trim($("#dm_name").val()) == "") {
		$.messager.alert("경고", "이름을 입력해주세요.", "warning");
		return;
	}
	
	if ($("#dm_nick").val() != "") {
		if ($("#dm_nick").attr("readonly") != "readonly") {
			$.messager.alert("경고", "닉네임 중복 확인 후 진행해주세요.", "warning");
			$("#dm_nick").focus();
			return;
		}
	}
	
	var email = $("#dm_email").val();
	var emailChk = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i; //이메일 정규식
	var dotCount = email.match(/\./g);
	if (!emailChk.test(email) || dotCount == null || dotCount.length > 2) {
		$.messager.alert("경고", "이메일 형식이 잘못됐습니다.", "warning");
		$("#dm_email").focus();
		return;
	}
	
	var hp = $("#dm_hp").val();
	if(hp == '' || hp == null){
		$.messager.alert("경고", "휴대폰번호를 입력해주세요.", "warning");
		return;
	}
	
	var password = $("#pw").val();
	if (password != "" && password != null) {
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
		$("#dm_password").val(rsa.encrypt(password));
	}   
	
	$(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: '/adm/set_member.do',
        dataType:"json",
        type:"post",
        data:param,
        success: function(response){
        	if(response.result == "success") {
        		window.opener.$.messager.alert('알림', response.notice, 'info');
            	window.opener.$("#dg").datagrid('reload');
                self.close();
            } else {
            	$.messager.alert('경고', response.notice, 'warning');
         		$("#dm_password").val("");
				$(".btnWrap").show();
            }  
        }, error:function(request,status,error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
	        	$.messager.alert('경고',request.responseJSON.notice,'warning');
	        	$("#dm_password").val("");
	        	$(".btnWrap").show();
			}
        }
    });
}

function confirm_id(){
	var id = $('#dm_id').val();
	if ($.trim(id) == '') {
		$.messager.alert('경고',"아이디를 입력해주세요.","warning");
        return false;
    }
	if(id.length < 5){
		$.messager.alert('경고',"아이디는 최소 5글자 이상 입력해주세요.","warning");
		return;
	}
	
    $.ajax({
        url: "/web/dupChk.do",
        data: {dm_id: id, command:"id"},
        type: 'POST',
        cache: false,
        async: false,
        dataType: 'json',
        success: function(data) {
            if (data.result == 'success') {
            	$("#dm_id").attr("readonly", true);
            	$("#confirm_id").toggle();
            	$("#chkAfterID").toggle();
            	$.messager.alert('경고', data.notice, 'info');
            } else {
            	$.messager.alert('경고', data.notice, 'warning');
            } 
        }, error : function(request, status, error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
        }
    });
}
function confirm_nick(){
	if ($.trim($('#dm_nick').val()) == '') {
		$.messager.alert('경고',"닉네임을 입력해주세요." ,"warning");
        return false;
    }
    $.ajax({
        url: "/web/dupChk.do",
        data: {dm_nick: $("#dm_nick").val(), command:"nick", dm_no:$("#dm_no").val()},
        type: 'POST',
        cache: false,
        async: false,
        dataType: 'json',
        success: function(data) {
            if (data.result == 'success') {
            	$("#dm_nick").attr("readonly", true);
            	$("#confirm_nick").toggle();
            	$("#chkAfterNick").toggle();
            	$.messager.alert('경고', data.notice, 'info');
            } else {
            	$.messager.alert('경고', data.notice, 'warning');
            }
        }, error: function(request, status, error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
        }
    });
}

$(document).ready(function(){
	
	var dm_no = "<c:out value='${dm_no}'/>";
	if (dm_no != "") {
	    $.ajax({
	        url : "/adm/get_member.do",
	        type : "POST",
	        cache : false,
	        async : true,
	        dataType : "json",
	        data : "dm_no="+dm_no,
	        success : function (data) {
	        	if(data.result == "success") {
	                $("#dm_name").val(data.rows.dm_name);
	                $("#dm_nick").val(data.rows.dm_nick);
	                if ($("#dm_nick").val() != "") {
						$("#dm_nick").attr("readonly", true);
						$("#dm_nick").siblings("button").toggle();
	                }
	                $("#dm_id").val(data.rows.dm_id);
	                $("#dm_email").val(data.rows.dm_email);
	                $("#dm_hp").val(data.rows.dm_hp);
	                $("#dm_tel").val(data.rows.dm_tel);
	                $("#dm_zip").val(data.rows.dm_zip);
	                $("#dm_addr1").val(data.rows.dm_addr1);
	                $("#dm_addr2").val(unescapeHtml(data.rows.dm_addr2));
	                $("#dm_addr3").val(unescapeHtml(data.rows.dm_addr3));
	                $("#dm_addr_jibeon").val(data.rows.dm_addr_jibeon);
	                $('#dm_level').combobox('reload', '/adm/select_code.do?dm_code_group=1002&start_index=0&cut_index=5&selected='+data.rows.dm_level);
	                $("#dm_id_text").text(data.rows.dm_id);
	                $("#dm_birth_date").datebox('setValue', data.rows.dm_birth_date);
	                if(data.rows.dm_sex == "M"){
	                	$("#dm_sex_man").prop("checked", true);
	                } else if(data.rows.dm_sex == "F"){
	                	$("#dm_sex_woman").prop("checked", true);
	                }
	                $("#dm_about_me").text(unescapeHtml(data.rows.dm_about_me));
	                $("#dm_homepage").val(unescapeHtml(data.rows.dm_homepage));
	            } else {
	            	window.opener.$.messager.alert('경고', data.notice, 'warning');
				    self.close();
				}
	        }, error:function(request,status,error) {
	        	if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	window.opener.$.messager.alert('경고',request.responseJSON.notice,'warning');
		        	self.close();
				}
	        }
	    });
	}

	$("#fnSave").off().on('click', function () {
		if(confirm("정말로 저장하시겠습니까?")){
	    	fnSave();
		}
	});

	$("#fnCancel").off().on('click', function () {
		if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
	    	self.close();
		}
	});
	
	$("#confirm_id").off().on('click', function () {
		confirm_id();
	});
	$("#confirm_nick").off().on('click', function () {
		confirm_nick();
	});
});

function refresh(ele) {
	$(ele).toggle();
	$(ele).siblings("button").toggle();
	$(ele).siblings("input").attr("readonly", false);
}
</script>
<div class="easyui-layout">
    <div id="form_wrap">
    	<form id="fm" name="fm" method="post" autocomplete="off">
	    	<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
			<input type="hidden" id="RSAExponent" value="${RSAExponent}"/> 
	    	<input type="hidden" name="type" id="type" value="<c:out value='${type}'/>"/>
	    	<input type="hidden" name="dm_password" id="dm_password">
	    	<c:if test='${type eq "update"}'>
	    		<input type="hidden" name="dm_no" id="dm_no" value="<c:out value='${dm_no}'/>"/>
	    		<input type="hidden" name="dm_id" id="dm_id" readonly="readonly" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>    	
	        <div class="title">
	            <h1>일반회원정보관리</h1>
	            <div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>일반회원정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
                    <c:choose>
                    	<c:when test="${type eq 'insert'}">
                    		<dt>아이디<span class="required_value">*</span></dt>
                    		<dd>
	                    		<input name="dm_id" type="text" id="dm_id" value="<c:out value='${dm_id}'/>" autocomplete="off" class="wd90" onkeyup="setEngPattern(this);">
	                    		<button type="button" class="btn" id="confirm_id">중복체크</button>
	                    		<button type="button" class="btn" id="chkAfterID" onclick="refresh(this);" style="display: none;">변경</button>
	                    		<p class="noty">아이디는 영문 소문자로 시작해야 하며, 영문 소문자 또는 숫자로 5 ~ 20자 이하의 값을 입력해주세요.</p>
                    		</dd>
		                </c:when>
                    	<c:otherwise>
                    		<dt>아이디</dt>
                    		<dd>
                    			<span id="dm_id_text"></span>
                    		</dd>
                    	</c:otherwise>
                    </c:choose>	                
	            </dl>
	            <dl>
	                <dt>비밀번호<c:if test="${type eq 'insert'}"><span class="required_value">*</span></c:if></dt>
	                <dd>
	                	<input type="password" id="pw" autocomplete="new-password"  onkeyup="setBlankToNull(this);"/>
	                	
	                	<c:if test="${type eq 'update'}">
           					<p class="noty">비밀번호를 변경할 시에만 입력해주세요.</p>
           				</c:if>
	                	<p class="noty">영문 대/소문자,숫자,특수문자를 1개이상 포함하여 8~20자 입력해주세요.</p>
	                </dd>
	            </dl>            
	            <div class="half">
	                <dl>
		                <dt>이름<span class="required_value">*</span></dt>
		                <dd>
		                	<input name="dm_name" id="dm_name" type="text" maxlength="20" autocomplete="off" onkeyup="setNamePattern(this);">
		                	<p class="noty">한글, 영문으로 1자 이상 20자 이하로 입력해주세요.</p>
		                </dd>
		            </dl>
	                <dl>
	                    <dt>닉네임</dt>
	                    <dd>
	                    	<input class="wd75" name="dm_nick" id="dm_nick" type="text" maxlength="20" onkeyup="setTextPattern(this);" autocomplete="off">
	                    	<button type="button" class="btn" id="confirm_nick">중복체크</button>
	                    	<button type="button" class="btn" id="chkAfterNick" onclick="refresh(this);" style="display: none;">변경</button>
	                    	<p class="noty">한글, 영문, 숫자 최대 20자까지 입력가능합니다.</p>
	                    </dd>
	                </dl>
	            </div>
	            <div class="half">
	                <dl>
	                    <dt>휴대폰번호<span class="required_value">*</span></dt>
	                    <dd>
	                    	<input name="dm_hp" id="dm_hp" type="text" maxlength="14" onkeyup="setTelPattern(this);" autocomplete="off">
	                    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
	                    </dd>
	                </dl>
	                <dl>
	                    <dt>전화번호</dt>
	                    <dd>
	                    	<input name="dm_tel" id="dm_tel" type="text" maxlength="14" onkeyup="setTelPattern(this);" autocomplete="off">
	                    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
	                    </dd>
	                </dl>
	            </div>
	            <div class="half">
	            	<dl>
		            	<dt>성별</dt>
		            	<dd>
		            		<input type="radio" name="dm_sex" id="dm_sex_man" value="M"><label for="dm_sex_man">남</label>&nbsp;&nbsp;&nbsp;&nbsp;
		            		<input type="radio" name="dm_sex" id="dm_sex_woman" value="F"><label for="dm_sex_woman">여</label>
		            	</dd>
		            </dl>
		            <dl>
		            	<dt>생년월일</dt>
		            	<dd>
		            		<input type="text" class="easyui-datebox" id="dm_birth_date" name="dm_birth_date" autocomplete="off" data-options="formatter:myformatter,parser:myparser">
		            	</dd>
		            </dl>
	            </div>
	            <dl>
					<dt>이메일<span class="required_value">*</span></dt>
					<dd>
						<input name="dm_email" id="dm_email" type="text" maxlength="30" onkeyup="setEmailPattern(this);" autocomplete="off">
						<p class="noty">최대 30자 까지 입력 가능합니다.</p>
					</dd>
	            </dl>         
            	<dl>
	                <dt>회원권한<span class="required_value">*</span></dt>
	                <dd>
	                    <select id="dm_level" name="dm_level" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1002&start_index=0&cut_index=5&selected=1',
	                                                method: 'get',
	                                                valueField: 'dm_code_value',
	                                                textField: 'dm_code_name',
	                                                editable: false">
	                    </select>
	                </dd>
	            </dl>
	            <dl class="address">
	                <dt>주소</dt>
	                <dd>
	                    <label for="dm_zip" class="sound_only">우편번호</label>
	                    <input type="text" name="dm_zip" id="dm_zip" class="wd10" maxlength="5" placeholder="우편번호" readonly="readonly">
	                    <button type="button" class="btn" onclick="win_zip('fm', 'dm_zip', 'dm_addr1', 'dm_addr2', 'dm_addr3', 'dm_addr_jibeon');">주소 검색</button><br>
	                    <input type="text" name="dm_addr1" id="dm_addr1" placeholder="기본주소" readonly="readonly">
	                    <label for="dm_addr1">기본주소</label>
	                    <input type="text" name="dm_addr2" id="dm_addr2" placeholder="상세주소" autocomplete="off">
	                    <label for="dm_addr2">상세주소</label>
	                    <br>
	                    <input type="text" name="dm_addr3" id="dm_addr3" placeholder="기타주소" autocomplete="off">
	                    <label for="dm_addr3">기타주소</label>
	                    <input type="hidden" name="dm_addr_jibeon" id="dm_addr_jibeon"><br>
	                </dd>
	            </dl>
	            <dl>
	            	<dt>자기소개</dt>
	            	<dd><textarea name="dm_about_me" id="dm_about_me"></textarea></dd>
	            </dl>
	            <dl>
	            	<dt>홈페이지</dt>
	            	<dd>
	            		<input type="text" id="dm_homepage" name="dm_homepage" maxlength="255" autocomplete="off">
	            	</dd>
	            </dl>
	        </div>
	        
	    </form>
    </div>    
</div>
<c:import url="/adm/page_bottom.do" />