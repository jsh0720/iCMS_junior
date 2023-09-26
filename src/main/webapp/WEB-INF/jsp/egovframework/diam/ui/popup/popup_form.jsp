<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);
	String tomorrow = format.format(today.getTime()+60*60*24*1000);
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
<c:set var="tomorrow" value="<%=tomorrow%>"/>
<c:set var="dm_id" value="<%=dm_id%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />

<div class="easyui-layout">
    <div id="form_wrap">
    	<form id="fm" name="fm" method="post" autocomplete="off">
	    	<input type="hidden" name="type" value="<c:out value='${type}'/>"/>
	    	<c:if test='${type eq "update"}'>
	    		<input type="hidden" name="dm_id" value="<c:out value='${dm_id}'/>"/>
	    	</c:if>
	        <div class="title">
	            <h1>팝업관리</h1>
				<div class="btnWrap">
	                <button id="fnSave" type="button">저장</button>
	                <button id="fnCancel" class="bt09">취소</button>
	            </div>
	        </div>
	        <div class="page">
	            <h3>팝업정보</h3>
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
	                <dt>팝업 제목<span class="required_value">*</span></dt>
	                <dd>
	                	<input type="text" name="dm_popup_nm" id="dm_popup_nm" maxlength="100" autocomplete="off">
	                	<p class="noty">1자 이상 100자 이하로 입력해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>팝업 종류<span class="required_value">*</span></dt>
	                <dd>
	                    <select id="dm_popup_type" name="dm_popup_type" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_code.do?dm_code_group=1009',
	                                            method: 'get',
	                                            valueField: 'dm_code_value',
	                                            textField: 'dm_code_name',
	                                            editable: false" >
	                    </select>
	                    <p class="noty">레이어 : 현재 창에서 쪽지 형식으로 표출되는 팝업, 윈도우 : 새창으로 표출되는 팝업</p>
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
		                    <input type="text" id="start_dt" class="easyui-datebox wd15" value="<c:out value='${ymd}'/>" data-options="formatter:myformatter,parser:myparser,editable:false"/>
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
		                    <input type="text" id="end_dt" class="easyui-datebox wd15" value="<c:out value='${tomorrow}'/>" data-options="formatter:myformatter,parser:myparser,editable:false"/>
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
	                    <label style="padding-left: 0;"><input type="checkbox" name="dm_is_infinite" id="dm_is_infinite" value="1"/>무제한</label>
	                	<p class="noty">기간 설정 후 무제한을 선택하는 경우 설정한 기간과 상관없이 계속 게시됩니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>만료 시간<span class="required_value">*</span></dt>
	                <dd>
	                    <input type="text" name="dm_popup_expired" id="dm_popup_expired" maxlength="2" onkeyup="setNumberPattern(this);" autocomplete="off">
	                    <p class="noty">OO시간동안 창 열지 않음. 시간을 설정해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>링크</dt>
	                <dd>
	                    <input type="text" name="dm_link" id="dm_link" maxlength="255" autocomplete="off">
	                    <p class="noty">연결하고자 하는 외부페이지의 <b>http 프로토콜을 포함한 링크</b>를 입력해주세요. 최대 255자 작성가능합니다.</p>
	                    <p class="noty">단순 게시용 팝업일 경우 링크를 입력하지 않고 등록해주세요.</p>
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
	                    <p class="noty">현재창(메뉴 클릭 시 현재창에서 링크이동), 새창(메뉴 클릭 시 새창에서 링크이동)</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>팝업 크기<span class="required_value">*</span></dt>
	                <dd>
		                <input type="text" name="dm_popup_width" id="dm_popup_width" placeholder="이미지 넓이" class="wd20" onkeyup="setNumberPattern(this);" autocomplete="off"> px
						<input type="text" name="dm_popup_height" id="dm_popup_height" placeholder="이미지 높이" class="wd20" onkeyup="setNumberPattern(this);" autocomplete="off"> px
	                </dd>
	            </dl>
	            <dl>
	                <dt>팝업 위치<span class="required_value">*</span></dt>
	                <dd>
						<input type="text" name="dm_popup_left" id="dm_popup_left" class="wd20" placeholder="x 좌표" onkeyup="setNumberPattern(this);" autocomplete="off"> px
						<input type="text" name="dm_popup_top" id="dm_popup_top" class="wd20" placeholder="y 좌표" onkeyup="setNumberPattern(this);" autocomplete="off"> px
						<p class="noty">좌측 상단부터 0, 0 좌표 입니다.</p>
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
	if ($.trim($("#dm_popup_nm").val()) == "") {
		$.messager.alert("경고", "팝업 제목을 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_popup_expired").val()) == "") {
		$.messager.alert("경고", "만료시간을 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_popup_width").val()) == "") {
		$.messager.alert("경고", "이미지 넓이를 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_popup_height").val()) == "") {
		$.messager.alert("경고", "이미지 높이를 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_popup_left").val()) == "") {
		$.messager.alert("경고", "팝업 위치(X 좌표)를 입력하세요.", "warning");
		return;
	}
	if ($.trim($("#dm_popup_top").val()) == "") {
		$.messager.alert("경고", "팝업 위치(Y 좌표)를 입력하세요.", "warning");
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
		$.messager.alert("경고", "시작일이 종료일보다 이전날짜이어야 합니다." , "warning");
		return;
	}
	
	$("#dm_start_dt").val(dm_start_dt);
	$("#dm_end_dt").val(dm_end_dt);
	
	$(".btnWrap").hide();   
    var form = $("#fm")[0];
    var formData = new FormData(form);
    $.ajax({
        url : "/adm/set_popup.do",
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
	        	$.messager.alert('경고',request.responseJSON.notice,'warning');
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
			url : "/adm/get_popup.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data : {"dm_id" : dm_id},
			success : function (data) {
				if(data.result == "success") {
					$("#dm_id").val(data.rows.dm_id);
					$("#dm_domain").val(data.rows.dm_domain);
					$("#dm_domain_text").text(data.rows.dm_domain_text);
			        $("#dm_popup_nm").val(unescapeHtml(data.rows.dm_popup_nm));
			        $('#dm_popup_expired').val(data.rows.dm_popup_expired);
			        $("#dm_popup_type").combobox('reload', '/adm/select_code.do?dm_code_group=1009&selected='+data.rows.dm_popup_type);
			        $("#dm_start_dt").val(data.rows.dm_start_dt);
			        $("#dm_end_dt").val(data.rows.dm_end_dt);
			        $("#dm_link").val(unescapeHtml(data.rows.dm_link));
					$("#dm_link_type").combobox('reload', '/adm/select_code.do?dm_code_group=1005&selected='+data.rows.dm_link_type);
			        $("#dm_popup_width").val(data.rows.dm_popup_width);
			        $('#dm_popup_height').val(data.rows.dm_popup_height);
			        $("#dm_popup_top").val(data.rows.dm_popup_top);
			        $("#dm_popup_left").val(data.rows.dm_popup_left);
			        (data.rows.dm_is_infinite == "1") ? $("#dm_is_infinite").prop('checked', true) : $("#dm_is_infinite").prop('checked', false);
			        
			        $("#dm_status").combobox('reload',  '/adm/select_code.do?dm_code_group=1001&selected='+data.rows.dm_status);
			        			        
			        if (data.rows.dm_popup_img != null && data.rows.dm_popup_img != "") {
			        	$('.file_dd').append("<img src='/resources/popup/"+data.rows.dm_popup_img+"' style='max-width: 300px; margin-top:8px;'/><div><label><input type='checkbox' name='dm_popup_del_img' class='file_link' id='dm_popup_del_img' value='"+data.rows.dm_popup_img+"'/>삭제("+ data.rows.dm_popup_img_ori +")</label></div>");
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