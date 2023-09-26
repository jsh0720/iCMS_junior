<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_code_id = request.getParameter("dm_code_id") != null ? request.getParameter("dm_code_id") : "";
	String type = "";
	if (!"".equals(dm_code_id) && dm_code_id != null) {
		type = "update";
	} else {
		type = "insert";
	}
%>
<c:set var="dm_code_id" value="<%=dm_code_id%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />
<script type="text/javascript">
function fnSave() {
	if ($.trim($("#dm_code_group").val()) == "") {
		$.messager.alert("경고", "코드그룹을 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_code_value").val()) == "") {
		$.messager.alert("경고", "코드값을 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_code_name").val()) == "") {
		$.messager.alert("경고", "코드이름을 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_code_asc").val()) == "") {
		$.messager.alert("경고", "코드정렬순서를 입력해주세요.", "warning");
		return;
	}
	
    $(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: '/adm/set_common_code.do',
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
            	$(".btnWrap").show();
            }
        }, error:function(request,status,error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseJSON.notice, "warning");
	        	$(".btnWrap").show();
			}
        }
    });
}
$(document).ready(function(){
	var dm_code_id = "<c:out value='${dm_code_id}'/>";
	if (dm_code_id != "") {
		$.ajax({
			url : "/adm/get_common_code.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data : {"dm_code_id" : dm_code_id},
			success : function (data) {
				if(data.result == "success") {
					$("#dm_code_group").val(data.rows.dm_code_group);
					$("#dm_code_value").val(data.rows.dm_code_value);
					$("#dm_code_name").val(data.rows.dm_code_name);
					$("#dm_code_asc").val(data.rows.dm_code_asc);
					$("#dm_code_desc").val(unescapeHtml(data.rows.dm_code_desc));
					$("#dm_code_var_name").val(data.rows.dm_code_var_name);
					if(data.rows.dm_create_id != null){
						$("#dm_create_id").text(data.rows.dm_create_id);						
					}
					if(data.rows.dm_create_dt != null){
						$("#dm_create_dt").text(data.rows.dm_create_dt);						
					}
					if(data.rows.dm_modify_id != null){
						$("#dm_modify_id").text(data.rows.dm_modify_id);						
					}
					if(data.rows.dm_modify_dt != null){
						$("#dm_modify_dt").text(data.rows.dm_modify_dt);						
					}
				} else {
					window.opener.$.messager.alert('경고', response.notice, 'warning');
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
});
</script>
<div class="easyui-layout">
    <div id="form_wrap">
    	<form id="fm" name="fm" method="post" autocomplete="off">
	    	<input type="hidden" name="type" value="<c:out value='${type}'/>"/>
	    	<c:if test='${type eq "update"}'>
	    		<input type="hidden" name="dm_code_id" value="<c:out value='${dm_code_id}'/>"/>
	    	</c:if>
	        <div class="title">
	            <h1>공통코드관리</h1>
				<div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>공통코드정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
	                <dt>코드그룹<span class="required_value">*</span></dt>
	                <dd>
	                    <input name="dm_code_group" id="dm_code_group" type="text" maxlength="4" autocomplete="off" onkeyup="setNumberPattern(this);">
	                    <p class="noty">숫자로 1자 이상 4자 이하로 입력해주세요. ex) 1234</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>코드값<span class="required_value">*</span></dt>
	                <dd>
	                    <input name="dm_code_value" id="dm_code_value" type="text" maxlength="20" autocomplete="off" onkeyup="setcodePattern(this);">
	                    <p class="noty">영문, 숫자, 특수문자(_)로 최대 20자까지 입력해주세요. ex) M, F, L, D, 1, 2</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>코드이름<span class="required_value">*</span></dt>
	                <dd>
	                	<input name="dm_code_name" id="dm_code_name" type="text" maxlength="20" autocomplete="off" onkeyup="setTextPattern(this);">
	                	<p class="noty">한글, 영문, 숫자로 1자 이상 20자 이하로 입력해주세요.</p>
					</dd>
	            </dl>
	            <dl>
	                <dt>코드정렬값<span class="required_value">*</span></dt>
	                <dd>
	                	<input name="dm_code_asc" id="dm_code_asc" type="text" maxlength="2" autocomplete="off" onkeyup="setOrderPattern(this);">
	                	<p class="noty">1부터 99까지의 숫자만 입력가능합니다.</p>
					</dd>
	            </dl>
	            <dl>
	                <dt>확장데이터</dt>
	                <dd>
	                	<input name="dm_code_var_name" id="dm_code_var_name" type="text" maxlength="20" autocomplete="off" onkeyup="setTextPattern(this);">
	                	<p class="noty">확장데이터는 공통코드를 활용한 프로그래밍 시  별도의 값이 필요할 때 사용됩니다.</p>
	                	<p class="noty">한글, 영문, 숫자로 1자 이상 20자 이하로 입력해주세요.</p>
					</dd>
	            </dl>
	            <dl>
	                <dt>코드설명</dt>
	                <dd>
	                	<input name="dm_code_desc" id="dm_code_desc" type="text" autocomplete="off" maxlength="200">
	                	<p class="noty">최대 200자 까지 입력가능합니다.</p>
					</dd>
	            </dl>
	            <c:if test='${type eq "update"}'>
	            	<dl>
		                <dt>등록자</dt>
		                <dd id="dm_create_id"></dd>
		            </dl>
		            <dl>
		                <dt>등록일</dt>
		                <dd id="dm_create_dt"></dd>
		            </dl>
		            <dl>
		                <dt>수정자</dt>
		                <dd id="dm_modify_id"></dd>
		            </dl>
		            <dl>
		                <dt>수정일</dt>
		                <dd id="dm_modify_dt"></dd>
		            </dl>
	            </c:if>
	        </div>
	    </form>
    </div>    
</div>
<c:import url="/adm/page_bottom.do" />