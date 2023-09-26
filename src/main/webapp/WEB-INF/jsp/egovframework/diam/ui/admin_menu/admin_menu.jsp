<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script>
function fnNew(parentDepth) {
	
	if($("#dm_id").val() == "") {
    	$.messager.alert("경고", "상위메뉴가 없습니다. 상위메뉴를 선택해주세요.", "warning");
    	return;
    }
	
    var dm_parent_id = $("#dm_id").val();
    var dm_parent_text = $("#dm_parent_text").val();
    
    $("#type").val("insert");
    $("#dm_id").val('');
    $("#dm_parent_id").val(dm_parent_id);   
    $(".parent_text").text(dm_parent_text);
        
    $("#dm_title").val('');
    if (parentDepth == 0) {
    	$('#dm_link_url').val('default');
    	$('#dm_link_url').attr('readonly', true);    	
	} else {
		$('#dm_link_url').val('');
		$('#dm_link_url').attr('readonly', false);  
	}
    $('#dm_access_level').combobox('setValue', 6);
    $('#dm_view_order').val("");    
    $('#dm_status').combobox('setValue', 1);
        
  	$('#dm_create_dt').text('');
    $('#dm_create_id').text('');
    $('#dm_modify_dt').text('');
    $('#dm_modify_id').text('');
    
    $("#menu_depth").val('');
}

function fnRefresh() {
    $("#type").val("insert");
    $("#dm_id").val('');
    $("#dm_parent_id").val('');
    $("#dm_parent_text").val("");
    $(".parent_text").text('');
        
    $("#dm_title").val('');
    $('#dm_link_url').val('');
    $('#dm_link_url').attr('readonly', false);
    $('#dm_access_level').combobox('setValue', 6);
    $('#dm_view_order').val("");    
    $('#dm_status').combobox('setValue', 1);
        
  	$('#dm_create_dt').text('');
    $('#dm_create_id').text('');
    $('#dm_modify_dt').text('');
    $('#dm_modify_id').text('');
    
    $("#menu_depth").val('');
}

function fnSave() {   
	if ($("#menu_depth").val() != '0') {
		if ($.trim($("#dm_title").val()) == "") {
			$.messager.alert("경고", "메뉴명을 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_link_url").val()) == "") {
			$.messager.alert("경고", "URL을 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_view_order").val()) == "") {
			$.messager.alert("경고", "정렬순서를 입력해주세요.", "warning");
			return;
		}
	}
	
	$(".btnWrap").hide();
    var form = $("#fm")[0];
    var formData = new FormData(form);
    $.ajax({
        url : "/adm/set_admin_menu.do",
        data : formData,
        dataType: "json",
        type : "post",
        contentType: false,
        processData: false,
        success : function (data) {
        	if (data.result == 'success') {
        		$('#dg').datagrid('reload');
            	$('#tt').tree('reload');
                fnRefresh();
        	}
        	$(".btnWrap").show();
            $.messager.alert('알림',data.notice,"warning");
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

function fnRemove () {
	var dm_id = $("#dm_id").val();
	if (dm_id != "") {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			$(".btnWrap").hide();
        		$.ajax({
        			url : '/adm/delete_admin_menu.do',
        			data : {"dm_id" : dm_id},
        			type : 'POST',
        			async : false,
        			success : function(data) {
        				if (data.result = 'success') {
        					$('#dg').datagrid('reload');
        			        $('#tt').tree('reload');
        			        fnRefresh();
        			    }
        				$(".btnWrap").show();
        				$.messager.alert("경고", data.notice, "warning");
        			}, error:function(request,status,error) {
        	 	 		if (request.status == "303") {
        					location.replace("/adm/login.do");
        				} else {
        					$.messager.alert("error", request.responseJSON.notice, "warning");
        				}
        			}
        		});
    		}    		
    	});
    } else {
        $.messager.alert("경고", "삭제하실 항목을 선택해주세요", "warning");
    }
}

function selectedDg(rowIndex) {
    $('#dg').datagrid('selectRow',rowIndex);
    var currentRow =$("#dg").datagrid("getSelected");
    if (currentRow) {
    	if (currentRow.dm_depth == 1) {
    		// to do ,
		} else if (currentRow.dm_depth == 2) {
			$('#dm_link_url').val("default");
			$('#dm_link_url').attr("readonly", true);
		} else {
		    $('#dm_link_url').val(unescapeHtml(currentRow.dm_link_url));			
			$('#dm_link_url').attr("readonly", false);
		}
    	
        $("#dm_id").val(currentRow.dm_id);
        $("#dm_parent_text").val(currentRow.dm_title);
        $(".parent_text").text(currentRow.dm_parent_text);
        $("#dm_parent_id").val(currentRow.dm_parent_id);
        $("#dm_title").val(unescapeHtml(currentRow.dm_title));
 		$('#dm_access_level').combobox('setValue', currentRow.dm_access_level);
        $('#dm_view_order').val(currentRow.dm_view_order);
        $('#dm_status').combobox('setValue', currentRow.dm_status);
        
        $('#dm_create_dt').text(currentRow.dm_create_dt != null ? currentRow.dm_create_dt : "");
        $('#dm_create_id').text(currentRow.dm_create_id != null ? currentRow.dm_create_id : "");
        $('#dm_modify_dt').text(currentRow.dm_modify_dt != null ? currentRow.dm_modify_dt : "");
        $('#dm_modify_id').text(currentRow.dm_modify_id != null ? currentRow.dm_modify_id : "");
        $('#type').val("update");
        
        $("#menu_depth").val(currentRow.dm_depth-1);
        
    }
}

function loadTreeMenu() {
	$("#tt").tree({
    	url : '/adm/get_admin_menu_list.do',
    	method:'post',
    	animate:true,
    	dnd:false,
    	onSelect: function(node){
    		var rows = $('#dg').datagrid('getRows');
    		for(var i=0; i<rows.length; i++) {
    				var row = rows[i];
    				if(row.dm_id == node.id) {
    					selectedDg(i);
    					return;
    				}
    			}
    	},
    	onLoadError: function(request,status,error) {
 	 		if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseText, "warning");
			}
    	}
    });
}


$(function () {
	loadTreeMenu();
	
	$("#fnSave").off().on('click', function () {
		var menu_depth = $("#menu_depth").val();
    	var type = $("#type").val();
    	if(type == "insert"){
    		if(menu_depth > 1) {
    			$.messager.alert('저장알림', "메뉴는 최대 2depth 까지 가능합니다.",'warning');
	    		return;
    		}
    		
    	}
    	$.messager.confirm("", "정말 저장하시겠습니까?", function (r) {
    		fnSave();
    	});
    });
	
	$("#fnNew").off().on('click', function () {
		var menu_depth = $("#menu_depth").val();
    	if(menu_depth > 1){
    		$.messager.alert('생성알림', "메뉴는 최대 2depth 까지 가능합니다.",'warning');
    		return;
    	}
    	
        fnNew(menu_depth);
    });
	
	$("#fnRemove").off().on('click', function () {
    	fnRemove();
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
    	<div class="title">
	        <h1>관리자메뉴관리</h1>
	        <div class="btnWrap">
	            <button id="fnSave">저장</button>
	            <button id="fnNew" class="bt01">신규생성</button>
	            <button id="fnRemove" class="bt03">삭제</button>
	        </div>
        </div>
    </div>
    <form id="fm" method="post" novalidate>
    	<input type="hidden" name="dm_id" id="dm_id"/>
    	<input type="hidden" name="dm_parent_text" id="dm_parent_text"/>
		<input type="hidden" name="type" id="type" value="insert" />
        <div data-options="region:'west',split:true,border:false" style="width:220px;">
            <ul id="tt" class="easyui-tree" fit="true" width="100%"></ul>
        </div>
        <div data-options="region:'center',border:false" style="width:100%;height:260px;padding:0px" valign="middle">
            <div class="easyui-layout" fit="true" data-options="border:false" style="border:solid 0px #f6c1bc;">
                <div data-options="region:'center', border:false" style="width:100%;border:solid 0px #f6c1bc;padding:5px">
                    <div class="page">
                    	<h3 id="title">관리자메뉴 정보</h3>
                    	<p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
                        <dl>
                            <dt>상위메뉴</dt>
                            <dd>
                            	<span class="parent_text">선택되지 않음</span>
                                <input type="hidden" name="dm_parent_id" id="dm_parent_id"/>
                            </dd>
                        </dl>
                        <dl>
                        	<dt>depth</dt>
                            <dd>
                            	<input type="text" id="menu_depth" readonly="readonly">
                            	<p class="noty">값은 자동 입력되며, 최대 2depth까지 생성 가능합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>메뉴명<span class="required_value">*</span></dt>
                            <dd>
                               <input type="text" name="dm_title" id="dm_title" maxlength="30" autocomplete="off">
                               <p class="noty">1자 이상 30자 이하로 입력해주세요.</p>
							</dd>
                        </dl>        
                        <dl>
                            <dt>URL<span class="required_value">*</span></dt>
                            <dd>
                               <input type="text" name="dm_link_url" id="dm_link_url" maxlength="50" autocomplete="off">
                               <p class="noty">관리자메뉴 URL의 경우 별도의 프로그램 작업 후에 등록이 가능합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>사용권한<span class="required_value">*</span></dt>
                            <dd>
                               <select id="dm_access_level" name="dm_access_level" class="easyui-combobox" panelHeight="auto" style="width:300px;"
									data-options="url: '/adm/select_code.do?dm_code_group=1002&start_index=6&cut_index=10',
										method: 'get',
										valueField: 'dm_code_value',
										textField: 'dm_code_name',
										editable: false">
								</select>
								<p class="noty">관리자권한에 따라 메뉴를 노출 합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>사용여부<span class="required_value">*</span></dt>
                            <dd>
                            	<select id="dm_status" name="dm_status" class="easyui-combobox" panelHeight="auto" style="width:300px;"
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
                            <dt>정렬순서<span class="required_value">*</span></dt>
                            <dd>
                            	<input type="text" name="dm_view_order" id='dm_view_order' autocomplete="off" maxlength="2" onkeyup="setOrderPattern(this);">
                            	<p class="noty">1부터 99까지의 숫자만 입력가능합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>등록자</dt>
                            <dd>
                            	<p id="dm_create_id"></p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>등록일</dt>
                            <dd>
                            	<p id="dm_create_dt"></p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>수정자</dt>
                            <dd>
                            	<p id="dm_modify_id"></p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>수정일</dt>
                            <dd>
                            	<p id="dm_modify_dt"></p>
                            </dd>
                        </dl>                        
                    </div>
				</div>
			</div>    
		</div>		
    </form>
</div>
<div>
    <table id="dg" class="easyui-datagrid" data-options="rownumbers:true,pagination:true,singleSelect:true, url:'/adm/get_admin_menu_table.do',method:'post',toolbar:'#tb',footer:'#ft',fitColumns:false,striped:true" pageList="[10,20,30,50,70,100]" pageSize="50">
        <tr>
            <th data-options="field:'id',width:200,align:'center'" width="100">메뉴아이디</th>
            <th data-options="field:'dm_parent_id',width:200,align:'center'" width="100">부모메뉴아이디</th>
            <th data-options="field:'text',width:200,align:'center'" width="100">메뉴이름</th>
            <th data-options="field:'dm_link_url',width:200,align:'center'" width="100">연결경로</th>            
            <th data-options="field:'dm_access_level',width:200,align:'center'" width="100">사용권한</th>
            <th data-options="field:'dm_status',width:200,align:'center'" width="100">사용유무</th>
            <th data-options="field:'dm_view_order',width:200,align:'center'" width="100">정렬순서</th>            
            <th data-options="field:'dm_create_dt',width:200,align:'center'" width="100">생성일자</th>
            <th data-options="field:'dm_create_id',width:200,align:'center'" width="100">생성자ID</th>
            <th data-options="field:'dm_modify_dt',width:200,align:'center'" width="100">수정일자</th>
            <th data-options="field:'dm_modify_id',width:200,align:'center'" width="100">수정자ID</th>
        </tr>
    </table>
</div>
<c:import url="/adm/page_bottom.do" />