<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<script>
var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function deleteFaq(){
    var ids = [];
    var rows = $('#dg').datagrid('getChecked');
    
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
                    ids.push(rows[i].dm_id);
                }
    			
    			$.ajax({
    				url: "/adm/delete_faq.do",
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

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_status = $('#search_status').combobox('getValue');
	var search_domain = $('#search_domain').combobox('getValue');
    
    $('#dg').datagrid('load',
        {
            search_type : search_type,
            search_value : search_value,
            search_status : search_status,
            search_domain : search_domain
        })
}

function newFaq() {
	window.open("/adm/faq_form.do","faq_new",option);
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
    $("#search_status").combobox('reload');
    $("#search_domain").combobox('reload');
    $("#dg").datagrid('load', {
        search_type: '',
        search_value : '',
        search_status : '',
        search_domain : ''
    });
}

function formatDetail(value,row){
	return '<a data-id="'+row.dm_id+'" class="btn bt08 open_form" target="_blank">수정</a>';
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
        newFaq();
    });

    $("#fnRemove").off().on('click', function () {
       	deleteFaq();
    });

    $("#stx").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            doSearch();
        }
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

    $("#detail_search").off().on('click', function () {
        $("#Dsearch").toggle();
        var dl_count = $("#Dsearch dl").length;
        dl_count = Math.ceil(dl_count / 2);
        var margin_top = dl_count * $("#Dsearch dl").height();

        if($("#Dsearch").is(":visible")) {
            $(".Contents").css("margin-top", margin_top+"px");
        } else {
            $(".Contents").css("margin-top", "0px");
        }
    });
	
    $(document).on('click', ".open_form", function () {
        var dm_id = $(this).data("id");
        window.open("/adm/faq_form.do?dm_id="+dm_id, "faq_detail",option);
    });
    
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>FAQ관리</h1>
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
                    <dt><strong>도메인명</strong></dt>
                    <dd>
                        <select id="search_domain" name="search_domain" class="easyui-combobox" panelHeight="auto"
		                        data-options="url: '/adm/select_domain_id.do?search=1',
								method: 'get',
								valueField: 'dm_id',
                                textField: 'dm_domain_nm',
								editable: false">
	                	</select>
                    </dd>
                </dl>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_question">질문</option>
                            <option value="dm_answer">답변</option>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true, singleSelect:true,url:'/adm/get_faq_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false,footer:'#ft'"
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
                 <th data-options="field:'dm_domain_nm',width:150,align:'center'">도메인명</th>
                <th data-options="field:'dm_question',width:350,align:'center'">질문</th>
                <th data-options="field:'dm_answer',width:700,align:'center'">답변</th>
                <th data-options="field:'dm_state_txt',width:50,align:'center'">사용여부</th>
                <th data-options="field:'dm_order',width:50,align:'center'">정렬순서</th>
                <th field ="detail" width="70" formatter="formatDetail" align="center">관리</th>
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
