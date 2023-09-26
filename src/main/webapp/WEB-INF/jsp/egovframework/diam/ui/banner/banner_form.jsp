<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);
	String yesterday = format.format(today.getTime()-60*60*24*1000);
%>
<%
	String dm_id = request.getParameter("dm_id") != null ? request.getParameter("dm_id") : "";
	String type = "";
	if (!"".equals(dm_id) && dm_id != null) {
		type = "update";
	} else {
		type = "insert";
	}
%>
<c:set var="ymd" value="<%=ymd%>"/>
<c:set var="yesterday" value="<%=yesterday%>"/>
<c:set var="dm_id" value="<%=dm_id%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />

<div class="easyui-layout">
    <div id="form_wrap">
    	<form id="fm" name="fm" method="post">
	    	<input type="hidden" name="type" value="<c:out value='${type}'/>"/>
	    	<c:if test='${type eq "update"}'>
	    		<input type="hidden" name="dm_id" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>
	        <div class="title">
	            <h1>배너관리</h1>
				<div class="btnWrap">
	                <button type="button" id="fnSave">저장</button>
	                <button type="button" id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>배너정보</h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
	            	<c:choose>
						<c:when test="${type eq 'insert'}">
							<dt>도메인선택<span class="required_value">*</span></dt>
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
								<span id="dm_domain_text"></span>
								<input type="hidden" name="dm_domain" id="dm_domain"/>
							</dd>
						</c:otherwise>
					</c:choose>
	            </dl>            
	            <dl>
	                <dt>배너 제목<span class="required_value">*</span></dt>
	                <dd>
	                	<input type="text" name="dm_banner_nm" id="dm_banner_nm" maxlength="100" autocomplete="off">
	                	<p class="noty">1자 이상 100자 이하로 입력해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>이미지 파일<span class="required_value">*</span></dt>
	                <dd class="file_dd">
	                	<input type="file" name="multiFile" id="multiFile" accept=".jpg, .jpeg, .gif, .png">
	                	<p class="noty">.jpg, .jpeg, .gif, .png 확장자 파일만 업로드 가능합니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>게시 기간<span class="required_value">*</span></dt>
	                <dd>
	                	<div class="during">
	                		<p class="label">시작일</p>
		                    <input type="text" id="start_dt" class="easyui-datebox wd15" value="<c:out value='${yesterday}'/>" data-options="formatter:myformatter,parser:myparser,editable:false"/>
		                    <select id="start_hour" class="easyui-combobox" panelHeight="300" title="시작일 시"
		                            data-options="url: '/adm/select_code.do?dm_code_group=2015',
		                                            method: 'get',
		                                            valueField: 'dm_code_value',
		                                            textField: 'dm_code_name',
		                                            editable: false" >
		                    </select>
		                    <select id="start_min" class="easyui-combobox" panelHeight="300" title="시작일 분"
		                            data-options="url: '/adm/select_code.do?dm_code_group=2016',
		                                            method: 'get',
		                                            valueField: 'dm_code_value',
		                                            textField: 'dm_code_name',
		                                            editable: false" >
		                    </select>
		                    <input type="hidden" id="dm_start_dt" name="dm_start_dt">
		                </div>
		                <div class="during">
		                	<p class="label">종료일</p>
		                    <input type="text" id="end_dt" class="easyui-datebox wd15" value="<c:out value='${ymd}'/>" data-options="formatter:myformatter,parser:myparser,editable:false"/>
		                    <select id="end_hour" class="easyui-combobox" panelHeight="300"  title="종료일 시"
		                            data-options="url: '/adm/select_code.do?dm_code_group=2015',
		                                            method: 'get',
		                                            valueField: 'dm_code_value',
		                                            textField: 'dm_code_name',
		                                            editable: false" >
		                    </select>
		                    <select id="end_min" class="easyui-combobox" panelHeight="300" title="종료일 분"
		                            data-options="url: '/adm/select_code.do?dm_code_group=2016',
		                                            method: 'get',
		                                            valueField: 'dm_code_value',
		                                            textField: 'dm_code_name',
		                                            editable: false" >
		                    </select>
		                    <input type="hidden" id="dm_end_dt" name="dm_end_dt">
	                	</div>
	                    <label for="dm_is_infinite" style="padding-left: 0;"><input type="checkbox" name="dm_is_infinite" id="dm_is_infinite" value="1"/>무제한</label>
	                	<p class="noty">기간 설정 후 무제한을 선택하는 경우 설정한 기간과 상관없이 계속 게시됩니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>링크</dt>
	                <dd>
	                    <input type="text" name="dm_link" id="dm_link" maxlength="255" autocomplete="off">
	                    <p class="noty">연결하고자 하는 외부페이지의 <b>http 프로토콜을 포함한 링크</b>를 입력해주세요. 최대 255자 작성가능합니다.</p>
	                    <p class="noty">단순 게시용 배너일 경우 링크를 입력하지 않고 등록해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>링크 타겟<span class="required_value">*</span></dt>
	                <dd>
	                	<select id="dm_link_type" name="dm_link_type" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1005',
	                                    method: 'post',
	                                    valueField: 'dm_code_value',
	                                    textField: 'dm_code_name',
	                                    editable: false">
	                    </select>
	                    <p class="noty">_self(메뉴 클릭 시 현재창에서 링크이동), _blank(메뉴 클릭 시 새창에서 링크이동)</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>정렬순서<span class="required_value">*</span></dt>
	                <dd>
	                    <input type="text" name="dm_order" id="dm_order" maxlength="2" autocomplete="off" onkeyup="setNumberPattern(this);">
	                    <p class="noty">1부터 99까지의 숫자만 입력가능합니다.</p>
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
	                                            editable: false" >
						</select>
						<p class="noty">사용안함의 경우 비활성화 됩니다.</p>
	                </dd>
	            </dl>
	        </div>
	    </form>
    </div>    
</div>

<script>
function fnSave() {
	if ($.trim($("#dm_banner_nm").val()) == "") {
		$.messager.alert("경고", "배너 제목을 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_order").val()) == "") {
		$.messager.alert("경고", "정렬순서를 입력하세요.", "warning");
		return;
	}
	
	var start_dt = $("#start_dt").datebox("getValue");
	var end_dt = $("#end_dt").datebox("getValue");
	
	var start_hour = $("#start_hour").combobox('getValue');
	var end_hour = $("#end_hour").combobox('getValue');
	
	var start_min = $("#start_min").combobox('getValue');
	var end_min = $("#end_min").combobox('getValue');
	
	var dm_start_dt = start_dt + " " +start_hour+":"+start_min+":00";
	var dm_end_dt = end_dt + " " +end_hour+":"+end_min+":00"; 
	
	var start_date = new Date(dm_start_dt);
	var end_date = new Date(dm_end_dt);
	
	if (start_date > end_date) {
		$.messager.alert("경고", "시작일이 종료일보다 이전날짜이어야 합니다.", "warning");
		return;
	}
	
	$("#dm_start_dt").val(dm_start_dt);
	$("#dm_end_dt").val(dm_end_dt);
    
    $(".btnWrap").hide();   
    var form = $("#fm")[0];
    var formData = new FormData(form);

    $.ajax({
        url : "/adm/set_banner.do",
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

function setDateTime(item){
	var sDate = new Date(item.dm_start_dt);
	var eDate = new Date(item.dm_end_dt);
	
	var startYear = sDate.getFullYear();
	var startMonth = (sDate.getMonth()+1);
	var startDate = sDate.getDate();
	var startHour = "";
	
	if(sDate.getHours() < 10){
		startHour = "0"+sDate.getHours();
	}else{
		startHour = sDate.getHours();
	}
	
	var startMin = sDate.getMinutes();
	
	var endYear = eDate.getFullYear();
	var endMonth = (eDate.getMonth()+1);
	var endDate = eDate.getDate();
	
	var endHour = "";
	if(eDate.getHours() < 10){
		endHour = "0"+eDate.getHours();
	} else {
		endHour = eDate.getHours();
	}
	var endMin = eDate.getMinutes();
	
	$("#start_dt").datebox('setValue', startYear+"-"+startMonth+"-"+startDate);
	$("#end_dt").datebox('setValue', endYear+"-"+endMonth+"-"+endDate);
	
	$("#start_hour").combobox({
		onLoadSuccess : function() {
			$("#start_hour").combobox('setValue', startHour);
		}
	})
	$("#start_min").combobox({
		onLoadSuccess : function() {
			$("#start_min").combobox('setValue', startMin);
		}
	})
	$("#end_hour").combobox({
		onLoadSuccess : function() {
			$("#end_hour").combobox('setValue', endHour);
		}
	})
	$("#end_min").combobox({
		onLoadSuccess : function() {
			$("#end_min").combobox('setValue', endMin);
		}
	})
	
}

$(function () {
	var dm_id = "<c:out value='${dm_id}'/>";
	if (dm_id != "") {
		$.ajax({
			url : "/adm/get_banner.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data : "dm_id="+dm_id,
			success : function (data) {
				if(data.result == "success") {
					$("#dm_id").val(data.rows.dm_id);
			        $("#dm_banner_nm").val(unescapeHtml(data.rows.dm_banner_nm));
			        $("#dm_start_dt").val(data.rows.dm_start_dt);
			        $("#dm_end_dt").val(data.rows.dm_end_dt);
			        $("#dm_link").val(unescapeHtml(data.rows.dm_link));
			        $("#dm_link_type").combobox('reload', '/adm/select_code.do?dm_code_group=1005&selected='+data.rows.dm_link_type);
			        $("#dm_order").val(data.rows.dm_order);
			        $("#dm_domain").val(data.rows.dm_domain);
					$("#dm_domain_text").text(data.rows.dm_domain_text);
			        $("#dm_status").combobox('reload',  '/adm/select_code.do?dm_code_group=1001&selected='+data.rows.dm_status);
			        (data.rows.dm_is_infinite == "1") ? $("#dm_is_infinite").prop('checked', true) : $("#dm_is_infinite").prop('checked', false);
			        			        			        
			        if (data.rows.dm_banner_img != null && data.rows.dm_banner_img != "") {
			        	$('.file_dd').append("<img src='/resources/banner/"+data.rows.dm_banner_img+"' style='margin-top:8px;'/><div><label><input type='checkbox' name='dm_del_image' class='file_link' id='dm_del_image' value='"+data.rows.dm_banner_img+"'/>삭제("+ data.rows.dm_banner_img_ori +")</label></div>");
			        }
			        
			        setDateTime(data.rows);
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
<c:import url="/adm/page_bottom.do"/>