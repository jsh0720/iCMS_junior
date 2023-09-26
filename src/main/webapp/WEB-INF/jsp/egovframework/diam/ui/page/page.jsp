<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script>
var uWidth = 1100;
var uHeight = 800;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_domain = $('#search_domain').combobox('getValue');
    $('#dg').datagrid('load',
        {
            search_type : search_type,
            search_value : search_value,
            search_domain : search_domain
        });
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
    $("#search_domain").combobox('reload');
    
    $("#dg").datagrid('load', {
        search_type: '',
        search_value: '',
        search_domain : ''
    });
}

function formatDetail(value,row){
	if (row.dm_page_type != 'MEMBER' && row.dm_page_type != 'LOGIN') {
		if (typeof row.dm_domain_url != 'undefined' && row.dm_domain_url != null) {
			var href = 'http://'+row.dm_domain_url+'/index.do?contentId='+row.dm_uid;
		    return '<a href="' + href + '" class="btn" target="_blank">바로가기</a>';
		} else {
			return '<a href="javascript:alert(\'도메인 URL 정보가 없습니다.\');" class="btn">바로가기</a>';
		}		
	} else {
		return '';
	}
}

function formatDetail2(value,row){
	return '<a data-id="'+row.dm_id+'" data-domain="'+row.dm_domain+'" class="btn bt08 open_form" target="_blank">수정</a>';
}

function formatDetail3(value, row) {
	if (row.dm_page_type == "PAGE") {
		return '<a data-id="'+row.dm_id+'" data-domain="'+row.dm_domain+'" class="btn newVersion" target="_blank">버전생성</a>';		
	} else {
		return '';
	}
}

function newPage() {
	window.open("/adm/page_form.do","page_new",option);
}

function deletePage(){
    var ids = [];
    var domains = [];
    var rows = $('#dg').datagrid('getChecked');
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
    	            ids.push(rows[i].dm_id);
    	            domains.push(rows[i].dm_domain);
    	        }
    			$.ajax({
    				url: "/adm/delete_page.do",
    				data: {dm_id : ids, dm_domain: domains},
    				type: "post"
    			}).done(function(response){
    				$.messager.alert('경고', response.notice , 'warning');
	                $('#dg').datagrid('reload');
    			}).fail(function(request, status, error) {
    				if (request.status == "303") {
    					location.replace("/adm/login.do");
    				} else {
    					$.messager.alert('경고',request.responseJSON.notice,'warning');
    				}
    	        });
    		}
    	});
    } else {
        $.messager.alert('경고', "삭제할 항목을 선택해주세요", 'warning');
    }
}

$(function () {
	$('#dg').datagrid({
        emptyMsg:'데이터가 없습니다.',
	 	onLoadError: function(request, status, error) {
	 		if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
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
        newPage();
    });

    $("#fnRemove").off().on('click', function () {
    	deletePage();
    });

    var pager = $('#dg').datagrid('getPager');
    pager.pagination ({
        showPageList: true,
        layout:['info','sep','first','prev','links','next','last','list'],
        displayMsg : "검색 <strong>{to}</strong> 개 / 전체 <strong>{total}</strong> 개"
    });

    $("#search_btn").off().on('click', function () {
        doSearch();
    });
    
    $(document).on('click', ".open_form", function () {
        var dm_id = $(this).data("id");
        var dm_domain = $(this).data("domain");
        window.open("/adm/page_form.do?dm_id="+dm_id+"&dm_domain="+dm_domain, "page_detail",option);
    });
    
    $(document).on("click", ".newVersion", function() {
    	var dm_id = $(this).data("id");
    	$.messager.confirm("신규 생성", "새로운 버전을 생성하시겠습니까?", function (r) {
    		if (r) {
		    	$.ajax({
		    		url : "/adm/insertNewVersion.do",
		    		data: {dm_id : dm_id},
		    		type: "post",
		    		success: function(data) {
		    			$.messager.alert('경고', data.notice, 'info');
		    			if (data.result == "success") {
							$('#dg').datagrid('reload'); 
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
<script type="text/javascript" src="https://www.jeasyui.com/easyui/datagrid-detailview.js"></script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>페이지관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
        <div class="Srchbox">
            <div>
            	<dl>
            		<dt><strong>도메인명</strong></dt>
                    <dd>
                        <select id="search_domain" name="search_domain" class="easyui-combobox" panelHeight="auto"
                                    data-options="url: '/adm/select_domain_id.do?search=1',
                                                    method: 'get',
                                                    valueField: 'dm_id',
                                                    textField: 'dm_domain_nm',
                                                    editable: false" ></select>
                    </dd>
            	</dl>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_page_name">페이지이름</option>
                            <option value="dm_uid">uid</option>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true, singleSelect:true,url:'/adm/get_page_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false"
                pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
            <thead>
            <tr>
                <th data-options="field:'ck',width:100,align:'center',checkbox:true,
                editor:{
                    type:'checkbox',
                    options:{
                        on:'Y',
                        off:'N'
                    }
                }">-</th>
                 <th data-options="field:'dm_domain_text',width:100,align:'center'">도메인명</th>
                <th data-options="field:'dm_page_name',width:100,align:'center'">페이지 이름</th>
                <th data-options="field:'dm_uid',width:200,align:'center'">uid</th>
                <th data-options="field:'dm_file_name',width:100,align:'center'">파일명</th>
                <th data-options="field:'dm_page_type',width:70,align:'center'">페이지타입</th>
                <th data-options="field:'dm_version',width:50,align:'center',fixed:true">버전</th>
                <th field="detail3" width="40" formatter="formatDetail3" align="center">버전생성</th>
                <th data-options="field:'dm_main_content',width:50,align:'center'">메인페이지 여부</th>
                <th field="detail" width="40" formatter="formatDetail" align="center">바로가기</th>
                <th field="detail2" width="30" formatter="formatDetail2" align="center">관리</th>
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