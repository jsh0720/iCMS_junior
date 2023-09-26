<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script>
var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function fnNew() {
	window.open("/adm/layout_form.do","layout_new",option);    
}

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    $('#dg').datagrid('load',
        {
            search_type : search_type,
            search_value : search_value
        })
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
	var str = '<a data-id="'+row.dm_id+'" class="btn bt08 open_form" target="_blank">수정</a>';
    str += ' <a data-id="'+row.dm_id+'" class="btn private_delete">삭제</a>';
    return str;
}

$(function () {
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

     $("#fnNew").off().on('click', function () {
         fnNew();
     });

     $("#fnRemove").off().on('click', function () {
        fnRemove();
     });

     $("#search_btn").off().on('click', function () {
         doSearch();
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
     
     $(document).on('click', ".open_form", function () {
         var dm_id = $(this).data("id");
         window.open("/adm/layout_form.do?dm_id="+dm_id, "layout_detail",option);
     });
     
     $(document).on('click', ".private_delete", function () {
    		
    	    var dm_id = $(this).data("id");

   		$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
   	        if (r) {                
   				$.ajax({
   					url: "/adm/delete_layout.do",
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
});
</script>
<div class="easyui-layout" style="width:700px;height:350px;" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>레이아웃관리</h1>
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
                            <option value="dm_layout_nm">레이아웃명</option>
                            <option value="dm_layout_folder">레이아웃폴더명</option>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true, singleSelect:true,url:'/adm/get_layout_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false,footer:'#ft'"
               pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
            <thead>
            <tr>
            	<th data-options="field:'dm_layout_nm',width:200,align:'center'">레이아웃명</th>
                <th data-options="field:'dm_layout_folder',width:200,align:'center'">레이아웃폴더명</th>
                <th data-options="field:'dm_create_id',width:150,align:'center'">등록자</th>
                <th data-options="field:'dm_create_dt',width:150,align:'center'">등록일</th>
                <th data-options="field:'dm_modify_id',width:150,align:'center'">수정자</th>
                <th data-options="field:'dm_modify_dt',width:150,align:'center'">수정일</th>
                <th field ="detail" width="150" formatter="formatDetail" align="center">관리</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />