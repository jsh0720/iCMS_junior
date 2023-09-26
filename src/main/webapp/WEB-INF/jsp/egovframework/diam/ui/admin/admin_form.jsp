<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_no = request.getParameter("dm_no") != null ? request.getParameter("dm_no").trim() : "";
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
	<c:if test="${type eq 'insert'}">
		if ($("#dm_id").attr("readonly") != "readonly") {
			$.messager.alert("경고", "아이디 중복 여부를 먼저 확인해주세요.", "warning");
			return;
		}
	</c:if>	
	var email = $("#dm_email").val();
	var emailChk = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i; //이메일 정규식
	var dotCount = email.match(/\./g);
	if (!emailChk.test(email) || dotCount == null || dotCount.length > 2) {
		$.messager.alert("경고", "이메일 형식이 잘못됐습니다.", "warning");
		$("#dm_email").focus();
		return;
	}
	
	
	if ($.trim($("#dm_name").val()) == "") {
		$.messager.alert("경고", "이름을 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_hp").val()) == "") {
		$.messager.alert("경고", "휴대폰번호를 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_tel").val()) == "") {
		$.messager.alert("경고", "전화번호를 입력해주세요.", "warning");
		return;
	}
	
	var password = $("#dm_pw").val();
	if (password != "" && password != null) {
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
		$("#dm_password").val(rsa.encrypt(password));
	}
		
	$(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: '/adm/set_admin.do',
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

function fnIdDuplicate(ele) {
	var dm_id = $("#dm_id").val();
	if(dm_id != ""){
		$.ajax({
			url: "/adm/dup_admin_id.do",
			dataType: "json",
			type: "post",
			data:{dm_id : dm_id},
			success: function(response){
				if(response.result == "success"){
					$(ele).toggle();
					$("#chkAfter").toggle();
					$("#dm_id").attr("readonly", true);
					$.messager.alert('경고', response.notice, 'info');
				} else {
					$.messager.alert('경고', response.notice, 'warning');
					$("#dm_id").focus();
				}
			}, error : function(request, status, error) {
	 	 		if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert("error", request.responseJSON.notice, "warning");
					$("#dm_id").focus();
				}
			}
		});
	} else {
		$.messager.alert("경고", "아이디를 입력해 주시기바랍니다.", "warning");
		$("#dm_id").focus();
	}
}

function refresh(ele) {
	$(ele).toggle();
	$("#chkBefore").toggle();
	$("#dm_id").attr("readonly", false);
}

$(document).ready(function(){
	var dm_no = "<c:out value='${dm_no}'/>";
	if (dm_no != "") {
		$.ajax({
			url : "/adm/get_admin.do",
			type : "POST",
			cache : false,
            async : false,
            dataType : "json",
            data : "dm_no="+dm_no,
            success : function (data) {
            	if(data.result == "success") {
            		$("#dm_name").val(data.rows.dm_name);
	                $("#dm_id").val(data.rows.dm_id);
            		$("#dm_email").val(data.rows.dm_email);
	                $("#dm_hp").val(data.rows.dm_hp);
	                $("#dm_tel").val(data.rows.dm_tel);
	                $("#dm_zip").val(data.rows.dm_zip);
	                $("#dm_addr1").val(data.rows.dm_addr1);
	                $("#dm_addr2").val(unescapeHtml(data.rows.dm_addr2));
	                $("#dm_addr3").val(unescapeHtml(data.rows.dm_addr3));
	                $("#dm_addr_jibeon").val(data.rows.dm_addr_jibeon);
	                $('#dm_level').combobox('reload', '/adm/select_code.do?dm_code_group=1002&start_index=6&cut_index=10&selected='+data.rows.dm_level);
	                $('#group_id').combobox('reload', '/adm/get_group_table.do?selected='+data.rows.group_id);
					$("#dm_id_text").text(data.rows.dm_id);
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
    
    $("#fnCancel").off().on('click', function () {
		if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
			self.close();
		}
	});
	
	$("#fnSave").off().on('click', function () {
		if(confirm("정말로 저장하시겠습니까?")){
        	fnSave();
		}
    });
	
}); 
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
	    		<input type="hidden" name="dm_id" id="dm_id" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>
	    	<div class="title">
	            <h1>관리자정보관리</h1>
	            <div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>관리자정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
                    <c:choose>
                    	<c:when test="${type eq 'insert'}">
                    		<dt>아이디<span class="required_value">*</span></dt>
                    		<dd>
                    			<input name="dm_id" type="text" id="dm_id" class="wd90" onkeyup="setEngPattern(this);">
                    			<button type="button" class="btn" id="chkBefore" onclick="fnIdDuplicate(this);">중복검사</button>
                    			<button type="button" class="btn" id="chkAfter" onclick="refresh(this);" style="display: none;">변경</button>
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
	                	<input type="password" id="dm_pw" autocomplete="new-password" onkeyup="setBlankToNull(this);">
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
		                	<input name="dm_name" id="dm_name" type="text" maxlength="20" onkeyup="setNamePattern(this);">
		                	<p class="noty">한글, 영문으로 1자 이상 20자 이하로 입력해주세요.</p>
		                </dd>
		            </dl>
	                <dl>
	                    <dt>이메일<span class="required_value">*</span></dt>
	                    <dd>
	                    	<input name="dm_email" id="dm_email" type="text" maxlength="30" onkeyup="setEmailPattern(this);">
	                    	<p class="noty">최대 30자 까지 입력 가능합니다.</p>
	                    </dd>
	                </dl>
	            </div>       
	            <div class="half">
	                <dl>
	                    <dt>휴대폰번호<span class="required_value">*</span></dt>
	                    <dd>
	                    	<input name="dm_hp" id="dm_hp" type="text" maxlength="14" onkeyup="setTelPattern(this);">
	                    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
	                    </dd>
	                </dl>
	                <dl>
	                    <dt>전화번호<span class="required_value">*</span></dt>
	                    <dd>
	                    	<input name="dm_tel" id="dm_tel" type="text" maxlength="14" onkeyup="setTelPattern(this);">
	                    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
	                    </dd>
	                </dl>
	            </div>     
	            <div class="half">
	                <dl>
	                    <dt>관리자권한<span class="required_value">*</span></dt>
	                    <dd>
	                        <select id="dm_level" name="dm_level" class="easyui-combobox" panelHeight="auto"
	                                data-options="url: '/adm/select_code.do?&dm_code_group=1002&start_index=6&cut_index=<c:out value="${DiamLoginVO.dm_level}"/>',
	                                                    method: 'get',
	                                                    valueField: 'dm_code_value',
	                                                    textField: 'dm_code_name',
	                                                    editable: false">
	                        </select>
	                    </dd>
	                </dl>
	                <dl>
	                    <dt>관리자그룹<span class="required_value">*</span></dt>
	                    <dd>
	                    	<select id="group_id" name="group_id" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/get_group_table.do',
	                                                method: 'get',
	                                                valueField: 'dm_group_id',
	                                                textField: 'dm_group_name',
	                                                editable: false">
	                    	</select>
	                    </dd>
	                </dl>
	            </div>
				<dl class="address">
	                <dt>주소</dt>
	                <dd>
	                    <label for="dm_zip" class="sound_only">우편번호</label>
	                    <input type="text" name="dm_zip" id="dm_zip" class="wd10" maxlength="5" placeholder="우편번호" readonly="readonly">
	                    <button type="button" class="btn" onclick="win_zip('fm', 'dm_zip', 'dm_addr1', 'dm_addr2', 'dm_addr3', 'dm_addr_jibeon');">주소 검색</button><br>
	                    <input type="text" name="dm_addr1" id="dm_addr1" placeholder="기본주소" readonly="readonly">
	                    <label for="dm_addr1">기본주소</label>
	                    <input type="text" name="dm_addr2" id="dm_addr2" placeholder="상세주소">
	                    <label for="dm_addr2">상세주소</label>
	                    <br>
	                    <input type="text" name="dm_addr3" id="dm_addr3" placeholder="기타주소">
	                    <label for="dm_addr3">기타주소</label>
	                    <input type="hidden" name="dm_addr_jibeon" id="dm_addr_jibeon"><br>
	                </dd>
	            </dl>      
	        </div>
	    </form>
	</div>    
</div>
<c:import url="/adm/page_bottom.do" />