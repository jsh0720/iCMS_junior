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
<script type="text/javascript">
$(document).ready(function(){
	var dm_id = "<c:out value='${dm_id}'/>";
	if (dm_id != "") {
		$.ajax({
			url : "/adm/get_domain.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data : "dm_id="+dm_id,
			success : function (data) {
				if(data.result == "success") {
					$("#dm_domain_nm").val(unescapeHtml(data.rows.dm_domain_nm));
					$("#dm_root_text").text(data.rows.dm_domain_root);
					$("#dm_domain_root").val(data.rows.dm_domain_root);
					$("#dm_domain_root").attr("readonly", true);
					$("#chkBefore").toggle();
					$("#chkAfter").toggle();
					$("#dm_domain_status").combobox('reload',  '/adm/select_code.do?dm_code_group=1001&selected='+data.rows.dm_domain_status);
					$("#dm_domain_description").val(unescapeHtml(data.rows.dm_domain_description));
					$("#dm_create_id").text(data.rows.dm_create_id != null ? data.rows.dm_create_id : "");
	                $("#dm_create_dt").text(data.rows.dm_create_dt != null ? data.rows.dm_create_dt : "");
	                $("#dm_modify_id").text(data.rows.dm_modify_id != null ? data.rows.dm_modify_id : "");
	                $("#dm_modify_dt").text(data.rows.dm_modify_dt != null ? data.rows.dm_modify_dt : "");
					
					if (data.rows.dm_domain_main != '1') {
						getMainDomainCount();
					} else {
						$("#dm_domain_main").prop("checked", true);
					}
					$("#saveBtn").linkbutton({text:"수정"});
				} else {
					window.opener.$.messager.alert('경고',data.notice,'warning');
				    self.close();
				}
			}, error : function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert("error", request.responseJSON.notice, "warning");
				}
			}
		});
	} else {
		getMainDomainCount();
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

function getMainDomainCount() {
	$.ajax({
		url : '/adm/get_domain_main_cnt.do',
		type : "POST",
		cache : false,
		async : true,
		dataType : "json",
		success : function (data) {
			if(data.result == "success") {
				if (data.count > 0) {
					$('.main_at').remove();
				}
			} else {
				window.opener.$.messager.alert('경고',data.notice, 'warning');
				self.close();
			}
		},error : function(request, status, error) {
			if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				window.opener.$.messager.alert('경고',request.responseJSON.notice,'warning');
				self.close();
			}
		}
	});
}

function fnSave() {
	if ($.trim($("#dm_domain_nm").val()) == "") {
		$.messager.alert('경고', "도메인명을 입력해주세요",'warning');
		return;
	}
	
	if ($("#dm_domain_root").attr("readonly") != "readonly") {
		$.messager.alert('경고', "디렉토리 중복 여부를 먼저 확인해주세요.",'warning');
		return;
	}
	
    $(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: "/adm/set_domain.do",
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
        }, error : function(request, status, error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseJSON.notice, "warning");
	        	$(".btnWrap").show();
			}
        }
    });
}

function fnDirDuplicate(ele) {
	var dir = $("#dm_domain_root").val();
	if(dir != ""){
		$.ajax({
			url: "/adm/dup_domain_directory.do",
			dataType: "json",
			type: "post",
			data:{dm_domain_root : dir},
			success: function(response){
				if(response.result == "success"){
					$(ele).toggle();
					$("#chkAfter").toggle();
					$("#dm_domain_root").attr("readonly", true);
					$.messager.alert('경고', response.notice, 'info');
				} else {
					$.messager.alert('경고', response.notice, 'warning');
					$("#dm_domain_root").focus();
					$("#dm_domain_root").val("");
				}
			}, error : function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert('경고',request.responseJSON.notice,'warning');
					$("#dm_domain_root").focus();
					$("#dm_domain_root").val("");
				}
			}
		})
	} else {
		$.messager.alert('경고', "디렉토리를 입력해주시기 바랍니다.",'warning');
		$("#dm_domain_root").focus();
	}
}

function refresh(ele) {
	$(ele).toggle();
	$("#chkBefore").toggle();
	$("#dm_domain_root").attr("readonly", false);
}
</script>
<div class="easyui-layout">
	<div id="form_wrap">
		<form id="fm" name="fm" method="post" autocomplete="off">
	    	<input type="hidden" name="type" value="<c:out value='${type}'/>"/>
	    	<c:if test='${type eq "update"}'>
	    		<input type="hidden" name="dm_id" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>
	        <div class="title">
	            <h1>도메인관리</h1>
				<div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>도메인정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>            
	            <dl>
	                <dt>도메인명<span class="required_value">*</span></dt>
	                <dd>
	                	<input name="dm_domain_nm" id="dm_domain_nm" type="text" maxlength="50">
	                	<p class="noty">1자 이상 50자 이하로 입력해주세요. 예)디자인아이엠</p>
	                </dd>
	            </dl>
	            <dl>
                	<c:choose>
                		<c:when test="${type eq 'insert'}">
                			<dt>디렉토리<span class="required_value">*</span></dt>
                			<dd>
	                			<input type="text" class="wd90" name="dm_domain_root" id="dm_domain_root" maxlength="30" onkeyup="setEngPattern(this);">
	                			<button type="button" class="btn" id="chkBefore" onclick="fnDirDuplicate(this);">중복검사</button>
	                			<button type="button" class="btn" id="chkAfter" onclick="refresh(this);" style="display: none;">변경</button>
	                			<p class="noty">영문, 숫자만 입력 가능하며, 최대 30자까지 입력가능합니다.</p>
                			</dd>
                		</c:when>
                		<c:otherwise>
                			<dt>디렉토리</dt>
                			<dd>
	                			<span id="dm_root_text"></span>
	                			<input type="hidden" name="dm_domain_root" id="dm_domain_root"/>
                			</dd>
                		</c:otherwise>
                	</c:choose>
	            </dl>
	            <dl class="main_at">
	                <dt>메인도메인 여부</dt>
	                <dd><input type="checkbox" name="dm_domain_main" id="dm_domain_main" value="1"></dd>
	            </dl>
	            <dl>
	                <dt>사용여부<span class="required_value">*</span></dt>
	                <dd>
	                    <select id="dm_domain_status" name="dm_domain_status" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1001',
	                                                method: 'get',
	                                                valueField: 'dm_code_value',
	                                                textField: 'dm_code_name',
	                                                editable: false">
	                    </select>
	                    <p class="noty">사용안함의 경우 비활성화 됩니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>설명</dt>
	                <dd>
	                	<input name="dm_domain_description" id="dm_domain_description" type="text">
	                	<p class="noty">최대 200자까지 입력 가능합니다. 예)(주)디자인아이엠 입니다.</p>
	                </dd>
	            </dl>
	            <c:if test='${dm_id ne null && dm_id ne ""}'>
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