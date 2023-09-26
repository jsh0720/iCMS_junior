<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_id = request.getParameter("dm_id") != null ? request.getParameter("dm_id") : "";
	String dm_domain = request.getParameter("dm_domain") != null ? request.getParameter("dm_domain") : "";
%>
<c:set var="dm_id" value="<%=dm_id%>"/>
<c:set var="dm_domain" value="<%=dm_domain%>"/>
<c:import url="/adm/page_header.do" />
<style>
	.cover, progress {
		accent-color: gray;
		color: gray;
	}
</style>
<div class="easyui-layout">
    <div id="form_wrap">
	    <form id="fm" name="fm" method="post" autocomplete="off">
	    	<input type="hidden" name="dm_id" id="dm_id" value="${dm_id }">
	        <input type="hidden" name="dm_uid" id='dm_uid' />
	        <div class="title">
	        	<h1>페이지 관리</h1>
		        <div class="btnWrap">
		            <button type="button" id="fnSave">저장</button>
		            <button type="button" id="fnCancel" class="bt09">취소</button>
		        </div>
	        </div>
	        <div class="page">
	            <h3><span id="title">페이지 정보</span></h3>
	            <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	            <dl>
	            	<c:choose>
						<c:when test="${dm_id ne null && dm_id ne ''}">
							<dt>도메인선택</dt>
							<dd>
								<span id="dm_domain_text"></span>
								<input type="hidden" name="dm_domain" id="dm_domain"/>
							</dd>							
						</c:when>
						<c:otherwise>
							<dt>도메인 선택<span class="required_value">*</span></dt>						
							<dd>
								<select id="dm_domain" name="dm_domain" class="easyui-combobox" panelHeight="auto"
							        data-options="url: '/adm/select_domain_id.do',
							                        method: 'get',
							                        valueField: 'dm_id',
							                        textField: 'dm_domain_nm',
							                        editable: false" >
								</select>
							</dd>							
						</c:otherwise>
					</c:choose>
				</dl>
	            <dl id="main_check">
	                <dt>메인페이지 여부</dt>
	                <dd>
	                    <input type="checkbox" name="dm_main_content" value="1" class="easyui-checkbox" id="dm_main_content" />
	                    <p class="noty">도메인별 1개의 페이지만 설정 가능합니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>파일명</dt>
	                <dd>
	                    <input type="text" name="dm_file_name" id='dm_file_name' style="width:300px" readonly placeholder="자동생성"/>
	                    <p class="noty">파일명은 자동으로 생성됩니다.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>페이지 이름<span class="required_value">*</span></dt>
	                <dd>
	                    <input type="text" name="dm_page_name" id="dm_page_name" onkeyup="setTextPattern(this);">
	                    <p class="noty">한글,영문,숫자로 1자 이상 50자 이하로 입력해주세요.</p>
	                </dd>
	            </dl>
	            <dl>
	                <dt>페이지타입<span class="required_value">*</span></dt>
	                <dd>
	                    <label>페이지 <input type="radio" name="dm_page_type" value="PAGE" checked="checked"/></label>
	                    <label>게시판 <input type="radio" name="dm_page_type" value="BOARD"/></label>
	                    <label>로그인 <input type="radio" name="dm_page_type" value="LOGIN"/></label>
	                    <label>회원 <input type="radio" name="dm_page_type" value="MEMBER"/></label>
	                    <label>FAQ <input type="radio" name="dm_page_type" value="FAQ"/></label>
	                    <label>통합검색 <input type="radio" name="dm_page_type" value="SEARCH"/></label>
	                    <label>약관 <input type="radio" name="dm_page_type" value="CLAUSE"/></label>
	                    <p class="noty">도메인별 로그인, 회원, FAQ, 통합검색, 약관은 1회만 생성 가능합니다.</p>
	                </dd>
	            </dl>
	            <dl class="border_menu">
	                <dt>게시판<span class="required_value">*</span></dt>
	                <dd>
	                    <select id="dm_board_id" name="dm_board_id" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_board.do',
	                                    method: 'get',
	                                    valueField: 'dm_id',
	                                    textField: 'dm_subject',
	                                    editable: false">
	                    </select>
	                </dd>
	            </dl>
	            <dl>
	                <dt>UID</dt>
	                <dd>
	                    <p class="dm_uid"></p>
	                </dd>
	            </dl>
	        </div>        
	    </form>
	    <table id="dg2" style="height:auto; width:100%;" class="easyui-datagrid" border="false" data-options="fitColumns:true,striped:false">
		    <thead>
		     <tr>
		         <th data-options="field:'dm_version',width:80,align:'center'">버전</th>
		         <th data-options="field:'dm_page_type',width:80,align:'center'">페이지타입</th>
		         <th data-options="field:'dm_main_content',width:80,align:'center'">메인페이지 여부</th>		         
		         <th data-options="field:'dm_file_name',width:140,align:'center'">파일명</th>         
		         <th data-options="field:'dm_create_id',width:100,align:'center'">등록자</th>
		         <th data-options="field:'dm_create_dt',width:150,align:'center'">등록일</th>
		         <th data-options="field:'dm_status_text',width:100,align:'center'">사용여부</th>
		         <th field ="detail" width = "70" formatter="formatDetail" align="center">적용</th>
		         <th field ="detail2" width = "70" formatter="formatDetail2" align="center">관리</th>
		     </tr>
		    </thead>
		</table>
    </div>
</div>
<script>
var dm_id = "<c:out value='${dm_id}'/>";
var dm_domain = "<c:out value='${dm_domain}'/>";

function selectPage() {
    if (dm_id != "") {
        $.ajax({
            url : "/adm/get_page.do?dm_id="+dm_id,
            type : "POST",
            cache : false,
            dataType : "json",
            success : function (data) {
                if(data.result == "success") {
                	setPageData(data.rows);
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

function setPageData(row) {
	
   	if (row.dm_main_content == '1') {
   		$("#dm_main_content").checkbox({checked:true});
   		$("input[name=dm_page_type]").not("input[value=PAGE]").closest("label").hide();
   		$("input[name=dm_page_type]").not("input[value=PAGE]").hide();
   	}
   	if(row.dm_page_type != 'PAGE'){
   		$("#main_check").hide();   		
   	}
   	
    $("input:radio[name='dm_page_type']:radio[value='"+row.dm_page_type+"']").prop('checked', true);
    $("#dm_uid").val(row.dm_uid);
    $(".dm_uid").text(row.dm_uid);
    $("#dm_file_name").val(row.dm_file_name);
    $("#dm_page_name").val(row.dm_page_name);
    $("#dm_domain_text").text(row.dm_domain_text);
    $("#dm_domain").val(row.dm_domain);
    
    if (row.dm_page_type == 'BOARD') {
        $(".border_menu").show();
        $("#dm_board_id").combobox('reload', '/adm/select_board.do?selected='+row.dm_board_id);
    } else {
    	$(".border_menu").hide();
    }
    
}

function fnSave() {
	
	if ($.trim($("#dm_page_name").val()) == "") {
		$.messager.alert("경고", "페이지 이름을 입력해주세요.", "warning");
		return;
	}
	
	
	$(".btnWrap").hide();
    var form = $("#fm")[0];
    var formData = new FormData(form);

    $.ajax({
        url : '/adm/set_page.do',
        data : formData,
        dataType: "json",
        type : "post",
        async : false,
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

function setSetting(id, dm_domain) {
	$.messager.confirm('경고', '버전을 변경하시겠습니까?', function(r){
        if (r){
            $.ajax({
                url:'/adm/change_page_version.do?dm_id='+id+'&dm_domain='+dm_domain,
                dataType:'json',
                success: function (data) {
                	if (data.result == 'success') {
                		window.opener.$.messager.alert('알림', data.notice, 'info');
                		window.opener.$("#dg").datagrid('reload');
                		self.close();
                	} else {
                		$.messager.alert('경고', data.notice, 'warning');
                	}                	
                }, error:function(request,status,error) {
                	if (request.status == "303") {
        				location.replace("/adm/login.do");
        			} else {
        				$.messager.alert('경고',request.responseJSON.notice,'warning');
        			}
    			}
            });
        }
    });
}

function formatDetail(value,row){
    var href = "javascript:setSetting('"+row.dm_id+"', '"+row.dm_domain+"');";
    return '<a href="'+href+'" class="btn">버전적용</a>';
}

function formatDetail2(value,row){
    return '<a data-id="'+row.dm_id+'" class="btn removeVersion" target="_blank">삭제</a>';
}

$(function () {
    selectPage();
    
	if (dm_id != null && dm_id != "") {
	    $("#dg2").datagrid({
	        url:'/adm/get_page_history.do?dm_id=<c:out value="${dm_id}"/>&dm_domain=<c:out value="${dm_domain}"/>',
	        rownumbers:true,singleSelect:true,method:'post',fitColumns:false,striped:false,selectOnCheck:false,checkOnSelect:false,
	        emptyMsg: '데이터가 없습니다.',
		 	onLoadError: function(data) {
		 		if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
			 		window.opener.$.messager.alert("error", data.responseJSON.notice, "warning");
			 		self.close();
				}
		 	}
	    });
	    $("input[name=dm_page_type]").addClass("cover");
	}
	
    <c:if test="${empty dm_id}">
	    $("#dm_domain").combobox({
			onChange: function(newValue){
				$("#dm_board_id").combobox({
					url: "/adm/select_board.do?domain="+newValue
				})
			}
		});
    </c:if>
	
    $("#fnSave").off().on('click', function () {
    	if(confirm("정말로 저장하시겠습니까?")){
    		fnSave();
    	}
    });
	
    $(".border_menu").hide();
    
    $("input[name='dm_page_type']").off().on('click', function () {
    	if (dm_id != null && dm_id != "") {
    		$.messager.alert("경고", "페이지 타입은 수정이 불가합니다.", "warning");
			return false;
		} else {
			var menu_type = $(this).val();
	        var bd_menu  = $(".border_menu");
	        
	        if (menu_type == 'BOARD') {
	            bd_menu.show();
	        } else {
	        	bd_menu.hide();
	        }
		}
    });
    
    $("#fnCancel").off().on('click', function () {
		if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
			self.close();
		}
	});
    
	$("#dm_main_content").checkbox({
		onChange: function(chk){
			if (chk) {
				var flag = $("input[name=dm_page_type]").attr("class");
				if (flag != null && flag.indexOf("cover") > -1) {
					if ($("input[name=dm_page_type]:checked").val() != "PAGE") {
						$.messager.alert("경고","PAGE 타입만 메인 페이지로 등록할 수 있습니다.", "warning");
						return;
					}
				} else {
					$("input[name=dm_page_type]").eq(0).trigger("click");
					$("input[name=dm_page_type]").not("input[value=PAGE]").closest("label").hide();
			   		$("input[name=dm_page_type]").not("input[value=PAGE]").hide();					
				}
			} else {
				$("input[name=dm_page_type]").not("input[value=PAGE]").closest("label").show();
		   		$("input[name=dm_page_type]").not("input[value=PAGE]").show();
			}
		}
	});
	
	$(document).on("click",".removeVersion", function() {
		var id = $(this).data("id");
		$.messager.confirm('경고', '정말 버전을 삭제하시겠습니까?', function(r){
			if (r) {
				$.ajax({
					url: "/adm/deleteVersion.do",
					data: {dm_id : id},
					type: "post",
					success: function(data) {
						$.messager.alert('경고', data.notice, 'info');
						if (data.result == "success") {
							$("#dg2").datagrid("reload");
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
		});
	});
	
	
});
</script>
<c:import url="/adm/page_bottom.do" />