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
	
	if ($.trim($("#dm_layout_nm").val()) == "") {
		$.messager.alert("경고", "레이아웃명을 입력하세요.", "warning");
		return;
	}
	
	<c:if test="${type eq 'insert'}">
		if ($("#dm_layout_folder").attr("readonly") != "readonly") {
			$.messager.alert("경고", "레이아웃 폴더 중복 여부를 먼저 확인해주세요.", "warning");
			return;
		}		
	</c:if>
	
    $(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: '/adm/set_layout.do',
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

function fnlayoutDuplicate(ele) {
	var dm_layout_folder = $("#dm_layout_folder").val();
	if (dm_layout_folder != "") {
		$.ajax({
			url: "/adm/dup_layout_folder.do",
			dataType: "json",
			type: "post",
			data:{dm_layout_folder : dm_layout_folder},
			success: function(response){
				if(response.result == "success"){
					$(ele).toggle();
					$("#chkAfter").toggle();
					$("#dm_layout_folder").attr("readonly", true);
					$.messager.alert('경고', response.notice, 'info');
				} else {
					$.messager.alert('경고', response.notice, 'warning');
					$("#dm_domain_root").focus();
				}
			}, error : function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert('경고',request.responseJSON.notice,'warning');
					$("#dm_domain_root").focus();
				}
			}
		});
	} else {
		$.messager.alert('경고',"레이아웃 폴더명을 입력해주시기 바랍니다.",'warning');
		$("#dm_domain_root").focus();
	}
}
function refresh(ele) {
	$(ele).toggle();
	$("#chkBefore").toggle();
	$("#dm_layout_folder").attr("readonly", false);
}
$(document).ready(function(){
	var dm_id = "<c:out value='${dm_id}'/>";
	if (dm_id != "") {
		$.ajax({
               url : "/adm/get_layout.do",
               type : "POST",
               cache : false,
               async : true,
               dataType : "json",
               data : "dm_id="+dm_id,
               success : function (data) {
                   if(data.result == "success") {
                   	 	$("#dm_layout_nm").val(data.rows.dm_layout_nm);
                   	 	$("#dm_layout_folder").text(data.rows.dm_layout_folder);
                   	 	$("input[name='dm_layout_folder']").val(data.rows.dm_layout_folder);
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
	    		<input type="hidden" name="dm_id" value="<c:out value='${dm_id}'/>"/>
	    		<input type="hidden" name="dm_layout_folder"/>
	    	</c:if>
	        <div class="title">
	            <h1>레이아웃관리</h1>
				<div class="btnWrap">
	                <button id="fnSave" type="button">저장</button>
	                <button id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>레이아웃정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
	                <dt>레이아웃명<span class="required_value">*</span></dt>
	                <dd>
	                    <input name="dm_layout_nm" id="dm_layout_nm" type="text" maxlength="30" onkeyup="setTextPattern(this);">
	                    <p class="noty">한글,영문,숫자로 1자 이상 30자 이하로 입력해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <c:choose>
		            	<c:when test="${dm_id ne null && dm_id ne ''}">
			            	<dt>레이아웃 폴더명</dt>
			            	<dd id="dm_layout_folder"></dd>
		            	</c:when>
		            	<c:otherwise>
		            		<dt>레이아웃 폴더명<span class="required_value">*</span></dt>
		            		<dd>
			                  	<input class="wd90" name="dm_layout_folder" id="dm_layout_folder" type="text" maxlength="20" onkeyup="setEngPattern(this);">
			                  	<button type="button" class="btn" id="chkBefore" onclick="fnlayoutDuplicate(this);">중복검사</button>
			                  	<button type="button" class="btn" id="chkAfter" onclick="refresh(this);" style="display: none;">변경</button>
			                  	<p class="noty">영문,숫자로 1자 이상 20자 이하로 입력해주세요.</p>
							</dd>	            		
		            	</c:otherwise>
		            </c:choose>	                
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