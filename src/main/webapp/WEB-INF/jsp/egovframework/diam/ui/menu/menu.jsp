<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script>
function doSearch(value) {
	var url = '/adm/get_menu_list.do?dm_domain=' + value;
	var url2 = '/adm/get_page_combo.do?search_domain='+value;
	    		
	$('#tt').tree({
        url : url
    });
	
	$("#dm_parent_id").val("");
	$(".parent_text").text("선택되지 않음");
	$("#link_combo").combobox('reload', url2);
	$("#link_combo").combobox({
        disabled:false
    });
	$("#dm_url").val("");
	$('#dm_link_target').combobox('setValue', '_self');
}

function doSearch2() {
	$("#dg1").datagrid('load', {
        search_nm: $("#search_name").val()
    });
}

function fnNew() {
	if($("#dm_id").val() == "") {
    	$.messager.alert("경고", "상위메뉴가 없습니다. 상위메뉴를 선택해주세요.", "warning");
    	return;
    }
	
    var dm_parent_id = $("#dm_id").val();
    var dm_parent_text = $("#dm_parent_text").val();
    
    
    
    $("#type").val("insert");
    $("#dm_id").val('');
    $("#dm_menu_text").val('');
    $("#menu_depth").val('');
    $("#dm_parent_id").val(dm_parent_id);
    $(".parent_text").text(dm_parent_text);
    $('#dm_link_type').combobox('setValue', 1);
        
    $("#link_combo").combobox({
        disabled:false
    });
    $("#dm_link_data_combo").show();
    $('#dm_link_target').combobox('setValue', '_self');
    $('#dm_menu_view').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected=1');
    $('#dm_menu_hidden').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected=0');    
    $('#dm_menu_desc').val('');
    $("#dm_url").val("");
    $("#dm_menu_order").val("");
    $("#title").text('신규 메뉴');
    
    $('#dm_create_dt').text('');
    $('#dm_create_id').text('');
    $('#dm_modify_dt').text('');
    $('#dm_modify_id').text('');
}

function fnRefresh() {
    $("#type").val("insert");
    $("#dm_id").val('');
    $("#dm_menu_text").val('');
    $("#dm_parent_id").val('');
    $("#dm_parent_text").val('');
    $(".parent_text").text('');
    $('#dm_link_type').combobox('setValue', 1);
    $("#menu_depth").val('');
        
    $("#link_combo").combobox({
        disabled:false
    });
    $("#dm_link_data_combo").show();
    $('#dm_link_target').combobox('setValue', '_self');
    $('#dm_menu_view').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected=1');
    $('#dm_menu_hidden').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected=0');    
    $('#dm_menu_desc').val('');
    $("#dm_url").val("");
    $("#dm_menu_order").val("");
    $("#title").text('신규 메뉴');
    
    $('#dm_create_dt').text('');
    $('#dm_create_id').text('');
    $('#dm_modify_dt').text('');
    $('#dm_modify_id').text('');
}

function fnSave() {
	if ($("#menu_depth").val() != '0') {
		if ($.trim($("#dm_menu_text").val()) == "") {
			$.messager.alert("경고", "메뉴명을 입력해주세요.", "warning");
			return;
		}
		
		if ($("#dm_link_type").combobox("getValue") == "2") {
			var url = $("#dm_url").val();
			if (url != "") {
				var idx = url.indexOf("http://") < 0 ? ( url.indexOf("https://") < 0 ? false : true ) : true ;
				if (!idx) {
					$.messager.alert("경고", "외부링크 타입의 메뉴는 http 프로토콜을 입력해야합니다.", "warning");
					return;
				}
			}
		} else {
			if ($.trim($("#dm_url").val()) == "") {
				$.messager.alert("경고", "연결할 페이지를 선택해주세요.", "warning");
				return;
			}
		}
		
		if ($.trim($("#dm_menu_order").val()) == "") {
			$.messager.alert("경고", "메뉴 순서를 입력하세요.", "warning");
			return;
		}
	}
	
    $(".btnWrap").hide();
    var param = $("#fm").serialize();
    $.ajax({
        url: '/adm/set_menu.do',
        data:param,
        type : 'POST',
        async : false,
        dataType:'json',
        success: function(data){
        	if (data.result == 'success') {
        		$('#dg').datagrid('reload');
        		$('#tt').tree('reload');
    	        fnRefresh();
    	    }
        	$(".btnWrap").show();
        	$.messager.alert('저장알림', data.notice,'warning');
        }, error:function(request,status,error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
        }
    });
}

function fnRemove () {
	var dm_id = $("#dm_id").val();
	var dm_domain = $("#dm_domain").val();
    if (dm_id != "") {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
	    		$(".btnWrap").hide();
	    		$.ajax({
	    			url : '/adm/delete_menu.do',
	    			data : {"dm_id" : dm_id, "dm_domain" : dm_domain},
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
	    					$.messager.alert('경고',request.responseJSON.notice,'warning');
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
    	$("#dm_id").val(currentRow.dm_id);
        $("#dm_parent_text").val(currentRow.dm_menu_text);
        $(".parent_text").text(currentRow.dm_parent_text);
        $("#dm_parent_id").val(currentRow.dm_parent_id);
        $("#dm_menu_text").val(unescapeHtml(currentRow.dm_menu_text));
        $("#dm_menu_desc").val(unescapeHtml(currentRow.dm_menu_desc));
        $('#dm_link_type').combobox('setValue', currentRow.dm_link_type);
        $('#dm_link_target').combobox('setValue', currentRow.dm_link_target);
        $('#dm_menu_view').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected='+currentRow.dm_menu_view);
        $('#dm_menu_hidden').combobox('reload', '/adm/select_code.do?dm_code_group=1006&selected='+currentRow.dm_menu_hidden);
        
        $('#dm_create_dt').text(currentRow.dm_create_dt != null ? currentRow.dm_create_dt : "");
        $('#dm_create_id').text(currentRow.dm_create_id != null ? currentRow.dm_create_id : "");
        $('#dm_modify_dt').text(currentRow.dm_modify_dt != null ? currentRow.dm_modify_dt : "");
        $('#dm_modify_id').text(currentRow.dm_modify_id != null ? currentRow.dm_modify_id : "");
        $('#type').val("update");
        
        if (currentRow.dm_link_type == 1) {
            $("#link_combo").combobox({
                disabled:false
            });
            $("#dm_url").val("");
            $("#dm_url").attr("readonly", true);
            $("#link_target").show();
            $("#dm_link_data_combo").show();
            $('#link_combo').combobox('setValue', currentRow.dm_link_data);
        } else {
            $("#dm_link_data_combo").hide();
            $("#link_target").hide();
            $("#link_combo").combobox({
                disabled:true
            });
        }
		
        $('#dm_menu_order').val(currentRow.dm_menu_order);
        $('#dm_url').val(unescapeHtml(currentRow.dm_url));
        $("#title").text(unescapeHtml(currentRow.dm_menu_text));
        
        $("#menu_depth").val(currentRow.dm_depth-1);
    }
}

function fnOpen(value) {
    $("#dlg").dialog('open');
    $("#search_name").focus();
    $('#dg1').datagrid({
    	url : "/adm/get_page_list.do?search_status=table&search_domain="+value,
        onDblClickCell: function(index,field,value){
            $('#dg1').datagrid('selectRow',index);
            var currentRow = $("#dg1").datagrid("getSelected");
            $("#link_combo").combobox('setValue', currentRow.dm_uid);
            $("#dlg").dialog('close');
        },
	 	onLoadError: function(data) {
	 		$.messager.alert('경고', data.responseText,'warning');
	 	}
    });
}

function allList() {
    $("#search_name").val('');
    $("#dg1").datagrid('load', {
        search_nm: $("#search_name").val()
    });
}

function loadTreeMenu(value) {
	$("#tt").tree({
    	url : '/adm/get_menu_list.do?dm_domain='+value,
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
    		$.messager.alert('경고', request.responseText,'warning');
    	}
    });
}

function loadTableMenu(value) {
	$("#dg").datagrid({
		url:'/adm/get_menu_table.do?dm_domain=' + value
	});
}

$(function () {
    $("#dm_link_type").combobox({
        onChange:function (newValue, oldValue) {
            if (newValue == 1) {
                $("#link_combo").combobox({
                    disabled: false
                });
                $("#dm_url").val("");
                $("#dm_url").attr("readonly", true);
                $("#link_target").show();
                $("#dm_link_data_combo").show();
            } else {
                $("#link_combo").combobox({
                    disabled: true
                });
                $("#dm_url").val("");
                $("#dm_url").removeAttr("readonly");
                $("#dm_link_data_combo").hide();
                $("#link_target").hide();
            }
        }
    });

    $("#link_combo").combobox({
        onChange:function (newValue, oldValue) {
            var content_id = newValue;
            var url = '?contentId='+content_id;
            $("#dm_url").val(url);
        }
    });

    $("#dlg").dialog({
        title : "페이지 선택"
    }).dialog("close");

    $("#search_name").off().on('keypress' , function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            doSearch2();
        }
    });

    $("#search_btn").off().on('click', function () {
        doSearch2();
    });

    $("#fnSave").off().on('click', function () {
    	var menu_depth = $("#menu_depth").val();
    	var type = $("#type").val();
    	if(type == "insert"){
	    	if(menu_depth > 3){
	    		$.messager.alert('저장알림', "메뉴는 최대 4depth 까지 가능합니다.",'warning');
	    		return;
	    	}    		
    	}
    	$.messager.confirm("저장알림", "정말 저장하시겠습니까?", function (r) {
    		if (r) {
    			fnSave();
    		}    		
    	});
    });

    $("#fnNew").off().on('click', function () {
    	var menu_depth = $("#menu_depth").val();
    	if(menu_depth > 3){
    		$.messager.alert('생성알림', "메뉴는 최대 4depth 까지 가능합니다.",'warning');
    		return;
    	}
    	if (menu_depth == 3 && $("#dm_link_type").combobox("getValue") == 2) {
    		$.messager.alert('생성알림', "3depth 이상의 외부링크 타입 메뉴는 하위 메뉴를 생성할 수 없습니다.",'warning');
    		return;
		}
        fnNew();
    });

    $("#fnRemove").off().on('click', function () {
    	fnRemove();
    });
    
    $("#search_domain").combobox({
        onChange:function (newValue, oldValue) {
        	doSearch(newValue);
        	loadTreeMenu(newValue);
        	loadTableMenu(newValue);
        	$("#dm_domain").val(newValue);
        }
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
	        <h1>메뉴관리</h1>
	        <div class="btnWrap">
	            <button id="fnSave">저장</button>
	            <button id="fnNew" class="bt01">신규생성</button>
	            <button id="fnRemove" class="bt03">삭제</button>
	        </div>
        </div>
        <div class="Srchbox">
        	<div>
	       		<dl>
					<dt><strong>도메인명</strong></dt>
					<dd>
						<select id="search_domain" name="search_domain" class="easyui-combobox" panelHeight="auto"
						                          data-options="url: '/adm/select_domain_id.do',
						                                          method: 'get',
						                                          valueField: 'dm_id',
						                                          textField: 'dm_domain_nm',
						                                          editable: false" >
						</select>
					</dd>
				</dl>
			</div>
        </div>
    </div>
    <form id="fm" method="post" autocomplete="off">
    	<input type="hidden" name="dm_domain" id="dm_domain"/>
    	<input type="hidden" name="dm_id" id="dm_id"/>
    	<input type="hidden" name="dm_parent_text" id="dm_parent_text" value="" />
        <input type="hidden" name="type" id="type" value="insert"/>
        <div data-options="region:'west',split:true,border:false" style="width:220px;">
        	<ul id="tt" class="easyui-tree" fit="true" width="100%"></ul>
        </div>
        <div data-options="region:'center',border:false" style="width:100%;height:260px;padding:0px" valign="middle">
            <div class="easyui-layout" fit="true" data-options="border:false" style="border:solid 0px #f6c1bc;">
                <div data-options="region:'center', border:false" style="width:100%;border:solid 0px #f6c1bc;padding:5px">
                    <div class="page">
                        <h3 id="title">신규 메뉴</h3>
                        <p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>   
                        <dl>
                            <dt>상위메뉴</dt>
                            <dd>
                            	<span class="parent_text"></span>
                            	<input type="hidden" name="dm_parent_id" id="dm_parent_id"/>
                            </dd>
                        </dl>
                        <dl>
                        	<dt>depth</dt>
                            <dd>
                            	<input type="text" id="menu_depth" readonly="readonly">
                            	<p class="noty">값은 자동 입력되며, 최대 4depth까지 생성 가능합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>메뉴명<span class="required_value">*</span></dt>
                            <dd>
                                <input type="text" name="dm_menu_text" id="dm_menu_text" maxlength="30">
                                <p class="noty">1자 이상 30자 이하로 입력해주세요.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>연결타입<span class="required_value">*</span></dt>
                            <dd>
                                <select id="dm_link_type" name="dm_link_type" class="easyui-combobox" panelHeight="auto"
                                        data-options="url: '/adm/select_code.do?dm_code_group=1004',
                                                method: 'get',
                                                valueField: 'dm_code_value',
                                                textField: 'dm_code_name',
                                                editable: false">
                                </select>
                                <p class="noty">페이지 : CMS에서 생성된 URL, 외부링크 : 외부URL로 연결</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>URL<span class="required_value">*</span></dt>
                            <dd>
                                <input type="text" name="dm_url" id="dm_url" placeholder="ex) https://diam.kr"/>
                                <p class="noty">연결타입이 페이지인 경우 '그리드 보기' 버튼을 눌러 페이지를 연결해주세요.</p>
                                <p class="noty">연결타입이 외부링크인 경우 연결하고자 하는 외부페이지의 <b>http 프로토콜을 포함한 링크</b>를 입력해주세요.</p>
                            </dd>
                        </dl>
                        <dl id="link_target">
                            <dt>연결 대상</dt>
                            <dd id="dm_link_data_combo">
                                <span style="float:right;"><a href="javascript:fnOpen($('#dm_domain').val());" class="easyui-linkbutton">그리드로 보기</a></span>
                                <select name="dm_link_data" id="link_combo" class="easyui-combobox"
                                        data-options="url: '/adm/get_page_combo.do',
                                                method: 'get',
                                                valueField: 'dm_uid',
                                                textField: 'dm_page_name',
                                                editable: false">
                                </select>
								<p class="noty">우측 상단의 그리드로 보기 버튼을 클릭하여 페이지를 선택하거나 콤보박스를 클릭하여 연결대상을 선택해주세요.</p>
                            </dd>           
                        </dl>
                        <dl>
                            <dt>링크타겟<span class="required_value">*</span></dt>
                            <dd>
                                <select id="dm_link_target" name="dm_link_target" class="easyui-combobox" panelHeight="auto"
                                        data-options="url: '/adm/select_code.do?dm_code_group=1005',
                                                method: 'get',
                                                valueField: 'dm_code_value',
                                                textField: 'dm_code_name',
                                                editable: false">
                                </select>
                                <p class="noty">현재창(메뉴 클릭 시 현재창에서 링크이동), 새창(메뉴 클릭 시 새창에서 링크이동)</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>설명</dt>
                            <dd>
                                <textarea id="dm_menu_desc" name="dm_menu_desc" rows="10" maxlength="200" placeholder="메뉴에 대한 설명을 입력해주세요."></textarea>
                                <p class="noty">최대 200자 까지 입력가능합니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>메뉴사용여부<span class="required_value">*</span></dt>
                            <dd>
                                <select id="dm_menu_view" name="dm_menu_view" class="easyui-combobox" panelHeight="auto"
                                        data-options="url: '/adm/select_code.do?dm_code_group=1006',
                                                method: 'get',
                                                valueField: 'dm_code_value',
                                                textField: 'dm_code_name',
                                                editable: false">
                                </select>
                                <p class="noty">사용안함의 경우 비활성화 됩니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>메뉴숨김여부<span class="required_value">*</span></dt>
                            <dd>
                                <select id="dm_menu_hidden" name="dm_menu_hidden" class="easyui-combobox" panelHeight="auto"
                                        data-options="url: '/adm/select_code.do?dm_code_group=1006',
                                                method: 'get',
                                                valueField: 'dm_code_value',
                                                textField: 'dm_code_name',
                                                editable: false">
                                </select>
                                <p class="noty">메뉴 숨김 사용시 메뉴에 노출되지 않습니다.</p>
                            </dd>
                        </dl>
                        <dl>
                            <dt>메뉴 순서<span class="required_value">*</span></dt>
                            <dd>
                                <input type="text" name="dm_menu_order" id="dm_menu_order" maxlength="2" onkeyup="setOrderPattern(this);">
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
	<div style="display:none;">
	    <table id="dg" class="easyui-datagrid" data-options="rownumbers:true,pagination:true,singleSelect:true,method:'post',toolbar:'#tb',footer:'#ft',fitColumns:false,striped:true" pageList="[10,20,30,50,70,100]" pageSize="50">
	        <tr>
	            <th data-options="field:'id',width:200,align:'center'" width="100">메뉴아이디</th>
	            <th data-options="field:'dm_parent_id',width:200,align:'center'" width="100">부모메뉴아이디</th>
	            <th data-options="field:'text',width:200,align:'center'" width="100">메뉴텍스트</th>
	            <th data-options="field:'dm_link_type',width:200,align:'center'" width="100">메뉴링크타입</th>
	            <th data-options="field:'dm_link_data',width:200,align:'center'" width="100">메뉴링크데이터</th>
	            <th data-options="field:'dm_link_target',width:200,align:'center'" width="100">메뉴링크타겟</th>
	            <th data-options="field:'dm_menu_view',width:200,align:'center'" width="100">메뉴뷰</th>
	            <th data-options="field:'dm_menu_order',width:200,align:'center'" width="100">메뉴순서</th>
	            <th data-options="field:'dm_create_dt',width:200,align:'center'" width="100">메뉴만든일</th>
	            <th data-options="field:'dm_modify_dt',width:200,align:'center'" width="100">메뉴수정일</th>
	        </tr>
	    </table>
	</div>
	<div id="dlg" class="easyui-dialog" style="width:1280px; height: 600px;">
	    <table id="dg1" data-options="rownumbers:true, singleSelect:true, toolbar:'#tb1',footer:'#ft'" pageList="[10,20,30,50,70,100]" pageSize="50" pagination="true"  fit="true">
	        <thead>
	        <tr>
	            <th data-options="field:'dm_page_name',width:300,align:'center'">페이지이름</th>
	            <th data-options="field:'dm_page_type',width:100,align:'center'">페이지타입</th>
	            <th data-options="field:'dm_uid',width:400,align:'center'">uid</th>
	            <th data-options="field:'dm_create_id',width:150,align:'center'">생성자</th>
	            <th data-options="field:'dm_create_dt',width:150,align:'center'">생성일</th>
	        </tr>
	        </thead>
	    </table>
	    <div id="tb1" style="padding:2px 8px;border:solid 0px #f6c1bc;">
	        <table width="100%">
	            <tr>
	                <td>
	                    <span>페이지이름:</span>
	                    <input id="search_name" value="" type="text" />
	                    <button class="btn" id="search_btn">검색</button>
	                    <button class="btn" onclick="allList()">전체목록</button>
	                </td>
	            </tr>
	        </table>
	    </div>
	</div>
</div>
<c:import url="/adm/page_bottom.do" />