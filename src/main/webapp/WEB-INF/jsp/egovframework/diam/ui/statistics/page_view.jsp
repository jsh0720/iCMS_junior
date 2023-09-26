<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);
	String seven = format.format(today.getTime()-60*60*24*1000*6);
%>
<c:import url="/adm/page_header.do" />
<c:set var="ymd" value="<%=ymd%>"/>
<c:set var="sevenago" value="<%=seven%>"/>
<script>
function allList() {
	$("#search_start_date").datebox('setValue', '<c:out value="${sevenago}"/>');
	$("#search_end_date").datebox('setValue', '<c:out value="${ymd}"/>');
	
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');
    
	$('#dg').datagrid('load', {
        search_start_date : encodeURIComponent(search_start_date),
        search_end_date : encodeURIComponent(search_end_date),
        emptyMsg:'데이터가 없습니다.'
    }
);
}

$(function () {
    fnSetTable();

    function fnSetTable(){
    	var search_start_date = $("#search_start_date").datebox('getValue');
        var search_end_date = $("#search_end_date").datebox('getValue');
                
        $('#dg').datagrid({
            url:'/adm/get_page_view.do?mode=page_view',
            columns:[[
                {field:'number',title:'순위',width:80,align:'center'},
                {field:'dm_fn_url',title:'접속 경로 페이지 URL',width:200,align:'center'},
                {field:'dm_type',title:'페이지명',width:80,align:'center'},
                {field:'page_count',title:'페이지뷰',width:80,align:'center'},
                {field:'percent',title:'비율(%)',width:80,align:'center'}
            ]],
            queryParams:{
             search_start_date : search_start_date,
             search_end_date : search_end_date
            },
            emptyMsg:'데이터가 없습니다.'
        });
    }

    $("#search_btn").off().on('click', function () {
        var search_start_date = $("#search_start_date").datebox('getValue');
        var search_end_date = $("#search_end_date").datebox('getValue');
        var search_domain = $("#search_domain").combobox('getValue');
        $('#dg').datagrid('load', {
                search_start_date : search_start_date,
                search_end_date : search_end_date,
                search_domain : search_domain,
                emptyMsg:'데이터가 없습니다.'
            }
        );
    });
    
    $("#search_domain").combobox({
        onChange:function (newValue, oldValue) {
        	var search_start_date = $("#search_start_date").datebox('getValue');
            var search_end_date = $("#search_end_date").datebox('getValue');
        	
        	$('#dg').datagrid('load', {
                search_start_date : search_start_date,
                search_end_date : search_end_date,
                search_domain : newValue,
                emptyMsg:'데이터가 없습니다.'
            });
        }
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>페이지뷰 분석</h1>
        </div>
        <div class="Srchbox">
        	<div>
        		<dl>
        			<dt><strong>사이트선택</strong></dt>
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
                <dl>                	
                    <dt><strong>기간검색</strong></dt>
                    <dd>
                        <input type="text" class="easyui-datebox" id="search_start_date" value="${sevenago}" data-options="formatter:myformatter,parser:myparser"> ~
                        <input type="text" class="easyui-datebox" id="search_end_date" value="${ymd}" data-options="formatter:myformatter,parser:myparser">
                        <a onclick="DateSearch.getToday();" class="btn">오늘</a>
                        <a onclick="DateSearch.getNextSevenDays()" class="btn">7일</a>
                        <a onclick="DateSearch.getNextFiftheenDays()" class="btn">15일</a>
                        <a onclick="DateSearch.getMonthAgo()" class="btn">1개월</a>
                        <a onclick="DateSearch.getThreeMonthAgo()" class="btn">3개월</a>
                    </dd>
                </dl>
            </div>
             <button class="btn bt00" id="search_btn">검색</button>
             <button class="btn" onclick="allList()">초기화</button>
        </div>
    </div>
    
    <div data-options="region:'center', border:false" class="Contents">
    	<table id="dg" class="easyui-datagrid" data-options="rownumbers:false,
            													 pagination:false, 
            													 url:'',
            													 method:'get', 
            													 singleSelect:true,
            													 fitColumns:true,
            													 striped:false,
            													 selectOnCheck:false,
            													 checkOnSelect:false"  fit="true" border="false" fit="true">
        </table>
    </div>
</div>    
<c:import url="/adm/page_bottom.do" />