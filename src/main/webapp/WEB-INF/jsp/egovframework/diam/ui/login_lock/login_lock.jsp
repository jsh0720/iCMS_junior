<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);			
%>
<c:import url="/adm/page_header.do" />
<c:set var="ymd" value="<%=ymd%>"/>
<script>
function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_start_date = $('#search_start_date').datebox('getValue');
    var search_end_date = $('#search_end_date').datebox('getValue');
    var search_level = $('#search_level').combobox('getValue');
    
    if (searchDateCheck(search_start_date, search_end_date)) {
    	$('#dg').datagrid('load',
   	        {
   	            search_type : search_type,
   	            search_value : search_value,
   	            search_start_date : search_start_date,
   	            search_end_date : search_end_date,
   	         	search_level : search_level
   	        }
   	   	);    	
    } else {
    	$.messager.alert('경고', "검색 시 시작일이 종료일보다 이후일 수 없습니다.", 'warning');
    }    
}

function fnUnlock() {
	var ids = [];
    var rows = $('#dg').datagrid('getChecked');
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 잠김해제 처리하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
                    ids.push(rows[i].dm_no);
                }
    			$.ajaxSetup({ async:false });
    			$.post('/adm/set_unlock.do',{dm_no:ids},function(result){
    				$.messager.alert('경고', result.notice, 'warning');
                },'json')
                $('#dg').datagrid('reload');
    		}
    	});
    } else {
        $.messager.alert('경고', "잠김해제 처리할 항목을 선택해주세요", 'warning');
    }
}

function allList() {
    $("#search_level").val('');
    $("#search_type").val('');
    $("#stx").val('');
    $("#dg").datagrid('load', {
        search_type: '',
        search_value : '',
        search_level : ''
    });
}

$(document).ready(function(){
    $("#stx").keypress(function(e){
        if(e.keyCode === 13){
            e.preventDefault();
            doSearch();
        }
    });

    $("#search_btn").off().on('click', function () {
        doSearch();
    });

    $("#fnUnlock").off().on('click', function () {
    	fnUnlock();
    });
	
    $('#dg').datagrid({
        emptyMsg: '데이터가 없습니다.',
	 	onLoadError: function(data) {
	 		alert('잠김계정 리스트를 불러오던 중 오류가 발생하였습니다.');
	 	}
    });
    
    var pager = $('#dg').datagrid('getPager');
    pager.pagination ({
        showPageList: true,
        layout:['info','sep','first','prev','links','next','last','list'],
        displayMsg : "검색 <strong>{to}</strong> 개 / 전체 <strong>{total}</strong> 개"
    });
});
</script>
<div class="easyui-layout" fit="true">
	<div data-options="region:'north', border:false">
        <div class="title">
            <h1>잠김계정관리</h1>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>실패 기간</strong></dt>
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
<!--                 	<dt><strong>계정구분</strong></dt>
                    <dd>
                    	<select name="search_gubun" id="search_gubun">
                    		<option value="admin">관리자</option>                            
                    	</select>
                    </dd> -->
                    <dt><strong>권한</strong></dt>
                    <dd>
                        <select id="search_level" class="easyui-combobox" panelHeight="auto"
                                data-options="url: '/adm/select_code.do?dm_code_group=1002&search=1&start_index=0&cut_index=5',
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
                            <option value="dm_id">아이디</option>
                            <option value="dm_name">이름</option>
                        </select>
                        <input type="text" name="search_value" id="stx" maxlength="10" placeholder="검색어는 10글자까지 입력가능합니다."/>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true,singleSelect:true,url:'/adm/get_lock_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
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
	                <th data-options="field:'dm_id',width:150,align:'center'">아이디</th>
	                <th data-options="field:'dm_name',width:100,align:'center'">이름</th>
	                <th data-options="field:'dm_level_text',width:70,align:'center'">권한</th>
	                <th data-options="field:'dm_fail_cnt',width:120,align:'center'">로그인실패횟수</th>
	                <th data-options="field:'dm_fail_time',width:120,align:'center'">최근로그인실패일</th>
	                <th data-options="field:'dm_datetime',width:120,align:'center'">회원가입일</th>                
	            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'south'">
        <dl class="Tbottom">
            <dd>
                <button class="btn" id="fnUnlock">선택잠김해제</button>
            </dd>
        </dl>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />