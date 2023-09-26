<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/adm/page_header.do" />
<script>

var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_status = $('#search_status').combobox('getValue');

    $('#dg').datagrid('load',
        {
            search_type : search_type,
            search_value : search_value,
            search_status : search_status
        }
   	);
}

function fnNew() {
    window.open("/adm/domain_form.do","domain_new",option);
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
    $("#search_status").combobox('reload');
    $("#dg").datagrid('load', {
        search_type: '',
        search_value : '',
        search_status : ''
    });
}

function formatSetting(value,row){
    return '<a data-id="'+row.dm_id+'" class="btn config_form" target="_blank">환경설정</a> <a data-id="'+row.dm_id+'" class="btn page_form" target="_blank">약관설정</a>';
}

function formatDetail(value,row){
    var str = '<a data-id="'+row.dm_id+'" class="btn bt08 open_form" target="_blank">수정</a> ';
    str += ' <a data-id="'+row.dm_id+'" class="btn private_delete">삭제</a>';
    return str;
}
$(function () {	
	$("#fnNew").off().on('click', function () {
        fnNew();
    });

    $("#fnRemove").off().on('click', function () {
        fnDelete();
    });

    $("#search_btn").off().on('click', function () {
        doSearch();
    });
    
    $('#dg').datagrid({
    	singleSelect: true,
    	checkOnSelect: true,
    	selectOnCheck: true,
        emptyMsg: '데이터가 없습니다.',
        onLoadError: function(request, status, error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseJSON.notice, "warning");
			}
	 	}
    });
    
    $("#stx").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            doSearch();
        }
    });
    
	var pager = $('#dg').datagrid('getPager');
    pager.pagination ({
        showPageList: true,
        layout:['info','sep','first','prev','links','next','last','list'],
        displayMsg : "검색 <strong>{to}</strong> 개 / 전체 <strong>{total}</strong> 개"
    });
});
$(document).on('click', ".config_form", function () {
    var dm_id = $(this).data("id");
    window.open("/adm/config_form.do?dm_domain_id="+dm_id, "config_detail",option);
});

$(document).on('click', ".page_form", function () {
	var dm_id = $(this).data("id");
	window.open("/adm/page_env.do?dm_domain_id="+dm_id, "page_detail",option);
});
    
$(document).on('click', ".open_form", function () {
    var dm_id = $(this).data("id");
    window.open("/adm/domain_form.do?dm_id="+dm_id, "domain_detail",option);
});

$(document).on('click', ".private_delete", function () {
	
    var dm_id = $(this).data("id");

	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
        if (r) {                
			$.ajax({
				url: "/adm/delete_domain.do",
				data: {dm_id : dm_id},
				type: "post"
			}).done(function(response){
				$.messager.alert('경고', response.notice , 'warning');
             $('#dg').datagrid('reload');
			}).fail(function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert("error", request.responseJSON.notice, "warning");
				}
	        });
        }
    });
});

</script>
<div class="easyui-layout" style="width:700px;height:350px;" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>도메인관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>사용여부</strong></dt>
                    <dd>
                        <select id="search_status" name="search_status" class="easyui-combobox" panelHeight="auto"
                                data-options="url: '/adm/select_code.do?dm_code_group=1001&search=1',
                                                    method: 'get',
                                                    valueField: 'dm_code_value',
                                                    textField: 'dm_code_name',
                                                    editable: false">
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_domain_nm">도메인명</option>
                            <option value="dm_domain_root">디렉토리</option>
                        </select>
                        <input type="text" name="search_value" id="stx" maxlength="20" placeholder="검색어는 20글자까지 입력가능합니다." autocomplete="off" onkeyup="setTextPattern(this);">
                    </dd>
                </dl>
            </div>
            <button class="btn bt00" id="search_btn">검색</button>
            <button class="btn" onclick="allList()">초기화</button>
        </div>
    </div>
    <div data-options="region:'center', border:false" class="Contents">
        <table id="dg"
               class="easyui-datagrid" fit="true" border="false"
               data-options="pagePosition:'top',rownumbers:true,pagination:true, url:'/adm/get_domain_list.do',method:'post',fitColumns:true,striped:false,footer:'#ft'"
               pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
        	<thead>
	        	<tr>
		            <th data-options="field:'dm_domain_nm',width:100,align:'center'">도메인명</th>
		            <th data-options="field:'dm_domain_main',width:60,align:'center'">메인도메인 여부</th>
		            <th data-options="field:'dm_domain_root',width:100,align:'center'">디렉토리</th>
		            <th data-options="field:'domain_url',width:100,align:'center'">URL</th>
		            <th data-options="field:'dm_domain_status_nm',width:60,align:'center'">사용여부</th>
		            <th data-options="field:'dm_domain_description',width:150,align:'center'">설명</th>
		            <th data-options="field:'theme_text',width:70,align:'center'">레이아웃</th>
		            <th field ="detail2" width="100" formatter="formatSetting" align="center">기본설정</th>
                	<th field ="detail" width="70" formatter="formatDetail" align="center">관리</th>
		        </tr>
        	</thead>
        </table>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />