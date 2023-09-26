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
	window.open("/adm/write_form.do","new_write",option);
}

function fnRemove(){
    var ids = [];
    var rows = $('#dg').datagrid('getChecked');
    if (rows.length > 0) {
    	$.messager.confirm("", "정말 삭제하시겠습니까?", function (r) {
    		if (r) {
    			for(var i=0; i<rows.length; i++){
                    ids.push(rows[i].wr_id);
                }
    			
    			$.ajax({
    				url: "/adm/delete_write.do",
    				data: {wr_id : ids},
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

function fnMove() {
	var ids = [];
	var rows = $('#dg').datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('경고', '정말로 이동하시겠습니까?', function (r) {
	        if (r) {
	            for(var i=0; i<rows.length; i++){
	                ids.push(rows[i].wr_id);
	            }
                var target = $("#target_table").combobox('getValue');
                
                $.ajax({
                	url: "/adm/move_write.do",
                	contentType:'application/json',
                	data: JSON.stringify({
               			"wr_id" : ids,
               			"target" : target
               		}),
                	type: "post",
                	success: function(result) {
                		$.messager.alert('경고', result.notice, 'warning');
                		$('#dg').datagrid('reload');
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
	} else {
		$.messager.alert('경고', "이동할 항목을 선택해주세요", 'warning');
	}	
}

function doSearch() {
    var search_type = $('#search_type').val();
    var search_value = $('#stx').val();
    var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date= $("#search_end_date").datebox('getValue');
    var search_board = $("#search_board").combobox('getValue');
	
    if (searchDateCheck(search_start_date, search_end_date)) {
    	$('#dg').datagrid('load',
   	        {
   	            search_type : search_type,
   	            search_value : search_value,
   	            search_start_date : search_start_date,
   	            search_end_date : search_end_date,
   	            search_board : search_board
   	        }
   	    );
    } else {
    	$.messager.alert('경고', "검색 시 시작일이 종료일보다 이후일 수 없습니다.", 'warning');
    }    
}

function allList() {
    $("#search_type").val('');
    $("#stx").val('');
    $("#search_start_date").datebox('setValue', '<c:out value="${ymd}"/>');
    $("#search_end_date").datebox('setValue', '<c:out value="${ymd}"/>');
    $("#search_board").combobox('reload');

    $("#dg").datagrid('load', {
        search_type: '',
        search_value : '',
        search_board : ''
    });
}

function formatDetail(value,row){
	str = '<a class="btn bt08 open_reply" data-id="'+row.wr_id+'" data-wr_board="'+row.wr_board+'">답글</a> '
		+ '<a class="btn bt08 open_comment" data-id="'+row.wr_id+'" data-wr_board="'+row.wr_board+'">댓글</a> '
		+ '<a class="btn bt08 open_form" data-id="'+row.wr_id+'" data-wr_board="'+row.wr_board+'">수정</a>';
    return str;
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
        fnNew();
    });

    $("#fnRemove").off().on('click', function () {
        fnRemove();
    });

    $("#move_write").off().on('click', function () {
    	fnMove();
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
        var wr_id = $(this).data("id");
        var dm_table = $(this).data("table");
        var wr_board = $(this).data("wr_board");
        window.open('/adm/write_form.do?wr_id='+wr_id+'&wr_board='+wr_board,"modify_write",option);
    });
    $(document).on('click', ".open_comment", function () {
        var wr_id = $(this).data("id");
        var dm_table = $(this).data("table");
        var wr_board = $(this).data("wr_board");
        window.open('/adm/comment.do?wr_id='+wr_id+'&wr_board='+wr_board,"reply_write",option);
    });
    $(document).on("click", ".open_reply", function(){
   	 	var wr_id = $(this).data("id");
        var wr_board = $(this).data("wr_board");
        location.href = "/adm/reply.do?wr_id="+wr_id+"&wr_board="+wr_board;
    });
});
</script>
<script type="text/javascript" src="https://www.jeasyui.com/easyui/datagrid-detailview.js"></script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>게시글 관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>기간</strong></dt>
                    <dd>
                        <input type="text" class="easyui-datebox" id="search_start_date" value="<c:out value='${ymd}'/>" data-options="formatter:myformatter,parser:myparser"> ~
                        <input type="text" class="easyui-datebox" id="search_end_date" value="<c:out value='${ymd}'/>" data-options="formatter:myformatter,parser:myparser">
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
                    <dt><strong>게시판명</strong></dt>
                    <dd>
                        <select id="search_board" name="search_board" class="easyui-combobox" panelHeight="auto" style="width:150px;height:30px;"
                                data-options="url: '/adm/select_board.do?search=1',
                                            method: 'get',
                                            valueField: 'dm_id',
                                            textField: 'dm_subject',
                                            editable: false">
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><strong>통합검색</strong></dt>
                    <dd>
                        <select name="search_type" id="search_type">
                            <option value="">통합검색</option>
                            <option value="wr_subject">제목</option>
                            <option value="wr_content">내용</option>
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
               data-options="pagePosition:'top',rownumbers:true,pagination:true,singleSelect:true,url:'/adm/get_write_list.do',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" pageList="[10,20,30,50,70,100]" pageSize="50" fit="true">
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
                <th data-options="field:'dm_table_text',width:150,align:'center'">게시판명</th>
                <th data-options="field:'ca_name',width:80,align:'center'">카테고리명</th>
                <th data-options="field:'wr_is_notice',width:50,align:'center'">공지사항여부</th>
                <th data-options="field:'wr_subject',width:300,align:'center'">제목</th>
                <th data-options="field:'wr_name',width:80,align:'center'">작성자</th>
                <th data-options="field:'wr_hit',width:60,align:'center'">조회수</th>
                <th data-options="field:'re_count',width:60,align:'center'">답글수</th>
                <th data-options="field:'com_count',width:60,align:'center'">댓글수</th>
                <th data-options="field:'mb_id',width:80,align:'center'">등록자</th>
                <th data-options="field:'wr_datetime',width:100,align:'center'">등록일</th>
                <th field="detail" width="100" formatter="formatDetail" align="center">관리</th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'south'">
        <dl class="Tbottom">
            <dd>
                <button class="btn" id="fnRemove">선택삭제</button>
            </dd>
            <dd>
                <em class="fas fa-check tcol"></em> 선택한 게시글을 <select id="target_table" name="target_table" class="easyui-combobox" panelHeight="auto" style="width:150px;height:30px;"
                                                                     data-options="url: '/adm/select_board.do',
                                                method: 'get',
                                                valueField: 'dm_id',
                                                textField: 'dm_subject',
                                                editable: false">
                </select> 으로  <button class="btn bt09" type="button" id="move_write" >이동</button>
            </dd>
        </dl>
    </div>
</div>
<c:import url="/adm/page_bottom.do" />