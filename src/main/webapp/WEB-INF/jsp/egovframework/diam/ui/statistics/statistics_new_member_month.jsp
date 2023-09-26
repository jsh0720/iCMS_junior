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
function fnSetData() {
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');

    $.ajax({
        url: "/adm/get_statistics_member.do?mode=month_member&search_start_date="+search_start_date+"&search_end_date="+search_end_date,
        dataType: 'json',
        success: function (data) {
            $("#total").text(data.total_count);
            $("#max").text(data.max_count);
            $("#max_date").text(data.max_date);
            $("#min").text(data.min_count);
            $("#min_date").text(data.min_date);
        }, error:function(request,status,error) {
        	$.messager.alert('경고',request.responseJSON.notice,'warning');
        }
    });

    $.ajax({
        url: "/adm/get_statistics_member.do?mode=month_member_chart&search_start_date="+search_start_date+"&search_end_date="+search_end_date,
        dataType: 'json',
        success: function (data) {
            $("#chart-area").empty();
            var container = document.getElementById('chart-area'),
                chart_data = {
                    categories: data.date,
                    series: [
                        {
                            name: '신규회원',
                            data: data.total_count
                        }
                    ]
                },
                options = {
                    chart: {
                        width: widthSize, height: 400 /*,title: ''*/
                    },
                    yAxis: {
                        title: '',
                        min: 0
                    },
                    xAxis: {
                        title: ''
                    },
                    tooltip: {
                        suffix: ''/*,grouped: true*/
                    },
                    series: {
                        showLabel: false, showDot: false
                    },
                    legend: {
                        showCheckbox: true, align: 'bottom'
                    }
                }
            tui.chart.columnChart(container, chart_data, options);
        }, error:function(request,status,error) {
        	$.messager.alert('경고',request.responseJSON.notice,'warning');
        }
    });
}

function fnSetTable(){
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');
    $('#dg').datagrid({
        url:'/adm/get_statistics_member.do?mode=month_member_table',
        columns:[[
            {field:'dm_date',title:'월',width:80,align:'center'},
            {field:'total_count',title:'신규회원수',width:80,align:'center'},
            {field:'percent',title:'비율(%)',width:80,align:'center'}
        ]],
        queryParams:{
         search_start_date : encodeURIComponent(search_start_date),
         search_end_date : encodeURIComponent(search_end_date)
        },
        emptyMsg:'데이터가 없습니다.'
    });
}

function allList() {
	$("#search_start_date").datebox('setValue', '<c:out value="${sevenago}"/>');
	$("#search_end_date").datebox('setValue', '<c:out value="${ymd}"/>');
	
	fnSetData();
	fnSetTable();
	
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
    fnSetData();
    fnSetTable();

    $("#search_btn").off().on('click', function () {
        fnSetData();
        var search_start_date = $("#search_start_date").datebox('getValue');
        var search_end_date = $("#search_end_date").datebox('getValue');
        $('#dg').datagrid('load', {
                search_start_date : encodeURIComponent(search_start_date),
                search_end_date : encodeURIComponent(search_end_date),
                emptyMsg:'데이터가 없습니다.'
            }
        );
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>신규회원분석</h1>
        </div>
        <div class="Srchbox">
            <div>
                <dl>
                    <dt><strong>기간검색</strong></dt>
                    <dd>
                        <input type="text" class="easyui-datebox" id="search_start_date" value="${sevenago}" data-options="formatter:myformatter,parser:myparser"> ~
                        <input type="text" class="easyui-datebox" id="search_end_date" value="${ymd}" data-options="formatter:myformatter,parser:myparser">
                        <a onclick="DateSearch.getMonthAgo()" class="btn">1개월</a>
                        <a onclick="DateSearch.getThreeMonthAgo()" class="btn">3개월</a>
                        <a onclick="DateSearch.getSixMonthAgo()" class="btn">6개월</a>
                        <a onclick="DateSearch.getTwelveMonthAgo()" class="btn">12개월</a>
                    </dd>
                </dl>
            </div>
             <button class="btn bt00" id="search_btn">검색</button>
             <button class="btn" onclick="allList()">초기화</button>
        </div>
    </div>
    
    <div data-options="region:'center', border:false" class="Contents">
    	<ul class="Tabs mt30">
		    <li><a  onclick="location.href='/adm/new_member.do'">일별 신규회원 현황</a></li>
		    <li class="on"><a onclick="location.href='/adm/new_member_month.do'">월별 신규회원 현황</a></li>
		    <li><a  onclick="location.href='/adm/all_member.do'">일별 전체회원 현황</a></li>
		    <li><a onclick="location.href='/adm/all_member_month.do'">월별 전체회원 현황</a></li>
		</ul>
		<script>var widthSize = $('.Contents').width();</script>
        <div class="State">
            <table class="tb">
                <thead>
                <tr>
                    <th>총 신규회원수</th>
                    <th>최소 신규회원수</th>
                    <th>최대 신규회원수</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <strong id="total">0</strong>
                    </td>
                    <td>
                        <strong id="min">0</strong>
                        <em id="min_date">2020-10</em>
                    </td>
                    <td>
                        <strong id="max">0</strong>
                        <em id="max_date">2020-10</em>
                    </td>
                </tr>
                </tbody>
            </table>
            
            <div id="chart-area" style="margin:20px -10px 20px -10px;"></div>

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
</div>    
<c:import url="/adm/page_bottom.do" />