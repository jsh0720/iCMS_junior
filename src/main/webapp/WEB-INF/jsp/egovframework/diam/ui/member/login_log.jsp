<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/adm/page_header.do" />
<script>
function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_start_date = $('#search_start_date').val();
    var search_end_date = $('#search_end_date').val();
	
    if (searchDateCheck(search_start_date, search_end_date)) {
    	$('#dg').datagrid('load',
   	        {
   	            search_type : search_type,
   	            search_value : search_value,
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
    $("#search_start_date").datebox('setValue', '');
    $("#search_end_date").datebox('setValue', '');

    $("#dg").datagrid('load', {
        search_type: '',
        search_value : '',
        search_start_date: '',
        search_end_date : ''
    });
}

$(function () {
    $('#dg').datagrid({
        emptyMsg: '데이터가 없습니다.',
	 	onLoadError: function(request, status, error) {
	 		if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
	 	}
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
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>회원로그인 기록</h1>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>기간</strong></dt>
                    <dd>
                        <input type="text" class="easyui-datebox" id="search_start_date" value="" data-options="formatter:myformatter,parser:myparser"> ~
                        <input type="text" class="easyui-datebox" id="search_end_date" value="" data-options="formatter:myformatter,parser:myparser">
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
                    <dt>통합검색</dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="dm_login_id">아이디</option>
                            <option value="dm_type">구분</option>                            
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

        <table id="dg" class="easyui-datagrid" data-options="pagePosition:'top',rownumbers:true,pagination:true, url:'/adm/get_member_login_log.do',method:'post', singleSelect:true,fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" border="false" pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
            <thead>
            <tr>
                <th data-options="field:'dm_login_id',width:100,align:'center',fixed:true">아이디</th>
                <th data-options="field:'dm_type',width:150,align:'center',fixed:true">구분</th>
                <th data-options="field:'dm_datetime',align:'center',width:150,fixed:true">로그인일시</th>
                <th data-options="field:'dm_ip',width:120,align:'center',fixed:true">로그인IP</th>
                <th data-options="field:'dm_fn_result',width:120,align:'center',fixed:true">성공여부</th>
                <th data-options="field:'dm_agent_info',width:120,align:'center'">정보</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />