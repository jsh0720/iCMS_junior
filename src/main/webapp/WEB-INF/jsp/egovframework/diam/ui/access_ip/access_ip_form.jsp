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
function fnSave() {
    $(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: "/adm/set_access_ip.do",
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
	var dm_id = "<c:out value='${dm_id}'/>";
	if (dm_id != "") {
		$.ajax({
	        url : "/adm/get_access_ip.do",
	        type : "POST",
	        cache : false,
	        async : true,
	        dataType : "json",
	        data : "dm_id="+dm_id,
	        success : function (data) {
	            if(data.result == "success") {
	            	 $("#dm_ip").val(data.rows.dm_ip);
	            	 $("#dm_ip_text").text(data.rows.dm_ip);
	            	 $("#dm_name").val(data.rows.dm_name);
	            	 $("#dm_status").combobox({
	            		 onLoadSuccess: function() {
			                 $("#dm_status").combobox('setValue', data.rows.dm_status);	            			 
	            		 }
	            	 });
	                 $("#dm_create_id").text(data.rows.dm_create_id);
	                 $("#dm_create_dt").text(data.rows.dm_create_dt);
	                 $("#dm_modify_id").text(data.rows.dm_modify_id);
	                 $("#dm_modify_dt").text(data.rows.dm_modify_dt);
	            } else {
	            	window.opener.$.messager.alert('경고', response.notice, 'warning');
	                self.close();
	            }
	        }, error:function(request,status,error) {
	        	if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	window.opener.$.messager.alert('error',request.responseJSON.notice,'warning');
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
	    		<input type="hidden" name="dm_id" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>
	    	<div class="title">
	            <h1>접근아이피관리</h1>
				<div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>접근아이피정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
                	<c:choose>
                		<c:when test="${type eq 'insert'}">
                			<dt>아이피<span class="required_value">*</span></dt>
                			<dd>
	                			<input name="dm_ip" id="dm_ip" type="text" maxlength="15" onkeyup="setIpPattern(this);">
	                    		<p class="noty">CMS접근할 IP(IPv4)를 입력해주세요. ex)단일설정 : 192.168.0.1, 대역설정 : 192.168.0.*</p>
	                    	</dd>
                		</c:when>	                		
                		<c:otherwise>
                			<dt>아이피</dt>
                			<dd>
	                			<span id="dm_ip_text"></span>
	                			<input type="hidden" name="dm_ip" id="dm_ip"/>
                			</dd>
                		</c:otherwise>
                	</c:choose>
	            </dl>
	            <dl>
	                <dt>아이피명<span class="required_value">*</span></dt>
	                <dd>
	                    <input name="dm_name" id="dm_name" type="text" maxlength="50" onkeyup="setTextPattern(this);">
	                    <p class="noty">한글,영문,숫자로 1자 이상 30자 이하로 입력해주세요. ex)디자인아이엠 아이피</p>
	                </dd>
	            </dl>
	            <dl>
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