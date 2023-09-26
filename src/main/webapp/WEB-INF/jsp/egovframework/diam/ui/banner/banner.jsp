<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);			
%>
<c:set var="ymd" value="<%=ymd%>"/>
<c:import url="/adm/page_header.do" />
<script>
var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function fnNew() {
	window.open("/adm/banner_form.do","banner_new",option);    
}

function fnRemove() {
    var ids = [];
    var rows = $('#dg').datagrid('getChecked');
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
    	            ids.push(rows[i].dm_id);
    	        }
    			$.ajax({
    				url: '/adm/delete_banner.do',
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
    var search_start_date = $('#search_start_date').datebox('getValue');
	var search_end_date = $('#search_end_date').datebox('getValue');
    
	if (searchDateCheck(search_start_date, search_end_date)) {
		$('#dg').datagrid('load',
	        {
				search_type : search_type,
	            search_value : search_value,
	            search_status : search_status,
	            search_domain : search_domain,
	            search_start_date : search_start_date,
	            search_end_date : search_end_date
	        }
		);
	} else {
		$.messager.alert('경고', "검색 시 시작일이 종료일보다 이후일 수 없습니다.", 'warning');
	}
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
    $("#search_status").combobox('setValue', '');
    $("#search_domain").combobox('reload');
    $("#search_start_date").datebox('setValue', '');
    $("#search_end_date").datebox('setValue', '');
    
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
        onLoadError: function(data) {
        	$.messager.alert("error", data.responseJSON.notice, "warning");
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
        window.open("/adm/banner_form.do?dm_id="+dm_id, "banner_detail",option);
    });
    
});
</script>
<div class="easyui-layout" style="width:700px;height:350px;" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>배너관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
        <div class="Srchbox">
            <div>
            	<dl>
                    <dt><strong>게시 기간</strong></dt>
                    <dd>
                        <input type="text" class="easyui-datebox" id="search_start_date" value="" data-options="formatter:myformatter,parser:myparser,editable:false"> ~
                        <input type="text" class="easyui-datebox" id="search_end_date" value="" data-options="formatter:myformatter,parser:myparser,editable:false">
                        <a onclick="DateSearch.getToday();" class="btn">오늘</a>
                        <a onclick="DateSearch.getThisWeek();" class="btn">이번주</a>
                        <a onclick="DateSearch.getThisMonth();" class="btn">이번달</a>
                        <a onclick="DateSearch.getNextSevenDays()" class="btn">1주일</a>
                        <a onclick="DateSearch.getNextFiftheenDays()" class="btn">15일</a>
                        <a onclick="DateSearch.getMonthAgo()" class="btn">1개월</a>
                        <a onclick="DateSearch.getThreeMonthAgo()" class="btn">3개월</a>
                        <a onclick="DateSearch.getSixMonthAgo()" class="btn">6개월</a>
                        <a onclick="DateSearch.resetDate()" class="btn">전체</a>
                    </dd>
                </dl>
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
                                                    editable: false" ></select>
                    </dd>
                </dl>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_banner_nm">제목</option>                            
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true, singleSelect:true,url:'/adm/get_banner_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false,footer:'#ft'"
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
		            <th data-options="field:'dm_domain_text',width:200,align:'center'">도메인명</th>
		            <th data-options="field:'dm_banner_nm',width:200,align:'center'">제목</th>
		            <th data-options="field:'dm_start_dt',width:200,align:'center'">시작일</th>
		            <th data-options="field:'dm_end_dt',width:200,align:'center'">종료일</th>
		            <th data-options="field:'dm_infinite_text',width:150,align:'center'">무제한여부</th>
		            <th data-options="field:'dm_create_dt',width:200,align:'center'">등록일</th>
		            <th data-options="field:'dm_status_text',width:150,align:'center'">사용여부</th>
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