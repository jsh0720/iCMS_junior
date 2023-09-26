<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_domain_id = request.getParameter("dm_domain_id") != null ? request.getParameter("dm_domain_id") : "";
%>
<c:set var="dm_domain_id" value="<%=dm_domain_id%>"/>
<c:import url="/adm/page_header.do" />
<div class="easyui-layout">
	<div id="form_wrap">
		<form id="fm" name="fm" enctype="multipart/form-data">
			<input type="hidden" name="dm_id" id="dm_id" value="" />
			<input type="hidden" name="dm_domain_id" value="<c:out value='${dm_domain_id}'/>" />
			<!-- <input type="hidden" name="type" id='type' value="insert" /> -->
			<div class="title">
				<h1>약관설정 관리</h1>
		        <div class="btnWrap">
		        	<button type="button" id="fnSave">저장</button>
		        	<button type="button" id="fnCancel" class="bt09">취소</button>
		        </div>
			</div>
			
			<div class="page">
				<h3>이용약관 설정</h3>
				<dl>
					<dt>사이트하단 출력</dt>
					<dd>
						<select id="dm_policy_status" name="dm_policy_status" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1001',
	                                                method: 'get',
	                                                valueField: 'dm_code_value',
	                                                textField: 'dm_code_name',
	                                                editable: false">
	                    </select>
						<p class="noty">홈페이지 하단 노출 여부를 설정합니다.</p>
					</dd>
				</dl>
				<dl>
					<dt>내용</dt>
					<dd>
						<textarea id="dm_policy_text" name="dm_policy_text"></textarea>
					</dd>
				</dl>
				<h3>개인정보취급방침 설정</h3>
				<dl>
					<dt>사이트하단 출력</dt>
					<dd>
						<select id="dm_private_status" name="dm_private_status" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1001',
	                                                method: 'get',
	                                                valueField: 'dm_code_value',
	                                                textField: 'dm_code_name',
	                                                editable: false">
	                    </select>
	                    <p class="noty">홈페이지 하단 노출 여부를 설정합니다.</p>
					</dd>	
				</dl>
				<dl>
					<dt>내용</dt>
					<dd>
						<textarea id="dm_private_text" name="dm_private_text"></textarea>
					</dd>
				</dl>
				<h3>이메일무단수집거부 설정</h3>
				<dl>
					<dt>사이트하단 출력</dt>
					<dd>
						<select id="dm_reject_status" name="dm_reject_status" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1001',
	                                                method: 'get',
	                                                valueField: 'dm_code_value',
	                                                textField: 'dm_code_name',
	                                                editable: false">
	                    </select>
	                    <p class="noty">홈페이지 하단 노출 여부를 설정합니다.</p>
					</dd>
				</dl>
				<dl>
					<dt>내용</dt>
					<dd>
						<textarea id="dm_reject_text" name="dm_reject_text"></textarea>
					</dd>
				</dl>
			</div>
		</form>
	</div>
</div>
<script>
var oEditors = [];

function createSE() {
	nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors, 
    	elPlaceHolder : "dm_policy_text", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true
    	},
    	fOnAppLoad : function() {
    		oEditors.getById["dm_policy_text"].exec("SET_CONTENTS", [$("#dm_policy_text").val()]);
    	}
    });
	nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors, 
    	elPlaceHolder : "dm_private_text", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true
    	},
    	fOnAppLoad : function() {
    		oEditors.getById["dm_private_text"].exec("SET_CONTENTS", [$("#dm_private_text").val()]);
    	}
    });
	nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors, 
    	elPlaceHolder : "dm_reject_text", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true
    	},
    	fOnAppLoad : function() {
    		oEditors.getById["dm_reject_text"].exec("SET_CONTENTS", [$("#dm_reject_text").val()]);
    	}
    });
}

$(function(){
    createSE();
	selectedDg();
	
	$("#fnSave").off().on('click', function () {
    	if(confirm("정말로 저장하시겠습니까?")){
        	fnSave();
    	}
    });
    
    $("#fnCancel").off().on('click', function(){
    	if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
    		self.close();
    	}
    });
});

function fnSave() {
    $(".btnWrap").hide();
    oEditors.getById["dm_policy_text"].exec("UPDATE_CONTENTS_FIELD", []);
    oEditors.getById["dm_private_text"].exec("UPDATE_CONTENTS_FIELD", []);
    oEditors.getById["dm_reject_text"].exec("UPDATE_CONTENTS_FIELD", []);
	
    var form = $("#fm")[0];
    var formData = new FormData(form);

    $.ajax({
        url : "/adm/set_page_env.do",
        data : formData,
        dataType: "json",
        type : "post",
        contentType: false,
        processData: false,
        success : function (data) {
            if(data.result == "success") {
            	window.opener.$.messager.alert('경고', data.notice, 'info');
                self.close();
            } else {
            	$.messager.alert('경고', data.notice, 'warning');
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

function selectedDg() {
	var dm_domain_id = '<c:out value="${dm_domain_id}"/>';
	if (dm_domain_id != '') {
		$.ajax({
			url : "/adm/get_env.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data: "dm_domain_id="+'<c:out value="${dm_domain_id}"/>',
			error:function(request,status,error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					window.opener.$.messager.alert('경고',request.responseJSON.notice,'warning');
					self.close();
				}
			}
		}).done(function(response) {
			if(response.result == "success") {
				if (response.rows != null) {
					setEvn(response);					
				} else {
					window.opener.$.messager.alert('경고', "기본 환경설정 이후 설정 가능합니다." , 'warning');
				    self.close();
				}
			} else {
				window.opener.$.messager.alert('경고', response.notice, 'warning');
			    self.close();
			}
		});
	}
}

function setEvn(response) {
	$("#dm_policy_text").val(response.rows.dm_policy_text);
	$("#dm_private_text").val(response.rows.dm_private_text);
	$("#dm_reject_text").val(response.rows.dm_reject_text);
	$("#dm_id").val(response.rows.dm_id);
	$("#dm_policy_status, #dm_private_status, #dm_reject_status").combobox({
		onLoadSuccess: function() {
			$("#dm_policy_status").combobox("setValue", response.rows.dm_policy_status != null ? response.rows.dm_policy_status : 1);
			$("#dm_private_status").combobox("setValue", response.rows.dm_private_status != null ? response.rows.dm_private_status : 1);
			$("#dm_reject_status").combobox("setValue", response.rows.dm_reject_status != null ? response.rows.dm_reject_status : 1);
		}
	});
}

</script>
<c:import url="/adm/page_bottom.do" />