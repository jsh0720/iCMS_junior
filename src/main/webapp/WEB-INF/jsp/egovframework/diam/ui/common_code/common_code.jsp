<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script type="text/javascript">
var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    $('#dg').datagrid('load',
        {
            search_type : search_type,
            search_value : search_value                    
            , emptyMsg:'데이터가 없습니다.'
        }
   	);
}

function fnNew() {
	var uWidth = 1024;
    var uHeight = 768;
    var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";
    window.open("/adm/common_code_form.do","common_code_new",option);
}

function fnDelete() {
	var ids = [];
    var rows = $('#dg').datagrid('getChecked');
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
                    ids.push(rows[i].dm_code_id);
                }
    			$.ajax({
    				url: "/adm/delete_common_code.do",
    				data: {dm_id : ids},
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
    } else {
        $.messager.alert('경고', "삭제할 항목을 선택해주세요", 'warning');
    }
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
   	$("#dg").datagrid('load', {
        search_type: '',
        search_value : ''
    });
}   

function formatDetail(value,row){
    return '<a data-id="'+row.dm_code_id+'" class="btn bt08 open_form" target="_blank">수정</a>';
}

$(document).ready(function(){
	$('#dg').datagrid({
	    emptyMsg:'데이터가 없습니다.',
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
	
	$("#fnNew").off().on('click', function () {
	     fnNew();
	});
	
	$("#fnRemove").off().on('click', function () {
	    fnDelete();
	});
	
	$("#search_btn").off().on('click', function () {
	    doSearch();
	});
	
	var pager = $('#dg').datagrid('getPager');
	pager.pagination ({
	    showPageList: true,
	    layout:['info','sep','first','prev','links','next','last','list'],
	    displayMsg : "검색 <strong>{to}</strong> 개 / 전체 <strong>{total}</strong> 개"
	});
	
	$(document).on('click', ".open_form", function () {
        var dm_code_id = $(this).data("id");
        window.open("/adm/common_code_form.do?dm_code_id="+dm_code_id, "popup_detail",option);
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>공통코드관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_code_group">코드그룹</option>
                            <option value="dm_code_value">코드값</option>
                            <option value="dm_code_name">코드이름</option>
                            <option value="dm_code_desc">코드설명</option>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true,singleSelect:true,url:'/adm/get_common_code_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
            <thead>
            <tr>
                <th data-options="field:'ck',width:100,align:'center',checkbox:true,
                editor:{
                    type:'checkbox',
                    options:{
                        on:'Y',
                        off:'N'
                    }
                }"></th>
                <th data-options="field:'dm_code_group',width:100,align:'center'">코드 그룹</th>
                <th data-options="field:'dm_code_value',width:100,align:'center'">코드 값</th>
                <th data-options="field:'dm_code_name',width:100,align:'center'">코드 이름</th>
                <th data-options="field:'dm_code_asc',width:50,align:'center'">코드정렬값</th>
                <th data-options="field:'dm_code_desc',width:80,align:'center'">코드설명</th>
                <th data-options="field:'dm_create_id',width:80,align:'center'">등록자</th>
                <th data-options="field:'dm_create_dt',width:80,align:'center'">등록일</th>
                <th data-options="field:'dm_modify_id',width:80,align:'center'">수정자</th>
                <th data-options="field:'dm_modify_dt',width:80,align:'center'">수정일</th>
                <th field ="detail" width="50" formatter="formatDetail" align="center">관리</th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'south'">
        <dl class="Tbottom">
            <dd>
                <button class="btn" id="fnRemove">선택삭제</button>
            </dd>
        </dl>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />