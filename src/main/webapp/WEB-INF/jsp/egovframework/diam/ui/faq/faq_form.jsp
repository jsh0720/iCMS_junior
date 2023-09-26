<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String dm_id = request.getParameter("dm_id") != null ? request.getParameter("dm_id") : ""; 
	String type = "";
	if (!"".equals(dm_id) && dm_id != null) {
		type = "update";
	} else {
		type = "insert";
	}
%>
<c:set var="dm_id" value="<%=dm_id%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />
<script>
function fnSave() {
	if ($.trim($("#dm_question").val()) == "") {
		$.messager.alert("경고", "FAQ 질문을 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_answer").val()) == "") {
		$.messager.alert("경고", "FAQ 답변을 입력해주세요.", "warning");
		return;
	}
	if ($.trim($("#dm_order").val()) == "") {
		$.messager.alert("경고", "정렬순서를 입력해주세요.", "warning");
		return;
	}
	
	
	$(".btnWrap").hide();
	
    var form = $("#fm")[0];
    var formData = new FormData(form);
	
    $.ajax({
        url : "/adm/set_faq.do",
        data : formData,
        dataType: "json",
        type : "post",
        contentType: false,
        processData: false,
        success : function (data) {
            if(data.result == "success") {
            	window.opener.$.messager.alert('알림', data.notice, 'info');
            	window.opener.$("#dg").datagrid('reload');
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

function selectPage() {
    var dm_id = "<c:out value='${dm_id}'/>";
   	if (dm_id != "") {
        $.ajax({
            url : "/adm/get_faq.do",
            type : "POST",
            cache : false,
            async : true,
            dataType : "json",
            data : "dm_id="+dm_id,
            success : function (data) {
                if(data.result == "success") {
                    fnSetData(data.rows);
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
}

function fnSetData(row) {
    $("#dm_question").val(row.dm_question);
    $("#dm_answer").val(row.dm_answer);
    $("#dm_status").combobox('reload', '/adm/select_code.do?dm_code_group=1001&selected='+row.dm_status);
    $("#dm_domain").val(row.dm_domain);
	$("#dm_domain_nm").text(row.dm_domain_nm);
    $("#dm_order").val(row.dm_order);
}

$(function () {
    selectPage();
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
		<form id="fm" method="post">
			<input type="hidden" name="type" id="type" value="<c:out value='${type}'/>" />
			<c:if test='${type eq "update"}'>
				<input type="hidden" name="dm_id" id="dm_id" value="<c:out value='${dm_id}'/>"/>
			</c:if>
			<div class="title">
				<h1>FAQ관리</h1>
		        <div class="btnWrap">
		            <button type="button" id="fnSave">저장</button>
		            <button type="button" id="fnCancel" class="bt09">취소</button>
		        </div>
			</div>
		    <div class="page">
				<h3 id="title">FAQ정보</h3>
				<p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
				<dl>
					<c:choose>
						<c:when test="${type eq 'insert'}">
							<dt><label for="dm_domain">도메인선택<span class="required_value">*</span></label></dt>
							<dd>
								<select id="dm_domain" name="dm_domain" class="easyui-combobox" panelHeight="auto"
										data-options="url: '/adm/select_domain_id.do',
										method: 'get',
										valueField: 'dm_id',
										textField: 'dm_domain_nm',
										editable: false">
								</select>
				      		</dd>
						</c:when>
						<c:otherwise>
							<dt>도메인선택</dt>
							<dd>
								<span id="dm_domain_nm"></span>
								<input type="hidden" name="dm_domain" id="dm_domain"/>
							</dd>
						</c:otherwise>
					</c:choose>
		  		</dl>
		        <dl>
		            <dt>질문<span class="required_value">*</span></dt>
		            <dd>
		                <textarea name="dm_question" id="dm_question" rows="10" placeholder="질문 내용을 입력해주세요."></textarea>
		            </dd>
		        </dl>
		        <dl>
		            <dt>답변<span class="required_value">*</span></dt>
		            <dd>
		                <textarea name="dm_answer" id="dm_answer" rows="10" placeholder="답변 내용을 입력해주세요."></textarea>
		            </dd>
		        </dl>
		        <dl class="history_menu">
		            <dt>사용여부<span class="required_value">*</span></dt>
		            <dd>
		                <select id="dm_status" name="dm_status" class="easyui-combobox" panelHeight="auto"
		                        data-options="url: '/adm/select_code.do?dm_code_group=1001',
		                            method: 'get',
		                            valueField: 'dm_code_value',
		                            textField: 'dm_code_name',
		                            editable: false">
						</select>
						<p class="noty">사용안함의 경우 비활성화 됩니다.</p>
		            </dd>
		        </dl>
		        <dl class="org_menu">
		            <dt>정렬순서<span class="required_value">*</span></dt>
		            <dd>
		                <input type="text" name="dm_order" id="dm_order" maxlength="2" onkeyup="setNumberPattern(this);">
		                <p class="noty">1부터 99까지의 숫자만 입력가능합니다.</p>
		            </dd>
		        </dl>
		    </div>
		</form>
	</div>	
</div>
<c:import url="/adm/page_bottom.do" />