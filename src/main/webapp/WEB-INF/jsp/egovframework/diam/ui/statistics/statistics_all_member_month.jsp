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
        url: "/adm/get_statistics_member.do?mode=all_month_member&search_start_date=" + search_start_date + "&search_end_date=" + search_end_date,
        dataType: "json",
        success: function (data) {
        	$("#total_count").text(data.total_count);
            $("#new_count").text(data.new_count);
            $("#leave_count").text(data.leave_count);
        }, error:function(request,status,error) {
        	$.messager.alert('경고',request.responseJSON.notice,'warning');
        }
    });

    $.ajax({
        url: "/adm/get_statistics_member.do?mode=all_month_member_chart&search_start_date=" + search_start_date + "&search_end_date=" + search_end_date,
        dataType: 'json',
        type: 'post',
        success: function (data) {
        	console.log(data.rows)
        	$("#chart-area").empty();
            var container = document.getElementById('chart-area'),
                chart_data = {
                    categories: data.date,
                    series: [
                        {
                            name: '신규 회원수',
                            data: data.new_count
                        },
                        {
                            name: '탈퇴회원',
                            data: data.leave_count
                        }
                    ]
                },
                options = {
                chart: {
                    width: widthSize, height: 400 /*,title: ''*/
                },
                yAxis: {
                    title: ''
                },
                xAxis: {
                    title: '',
                    min: 0
                },
                tooltip: {
                    suffix: ''/* ,grouped: true*/
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
        url:'/adm/get_statistics_member.do?mode=all_month_member_table',
        columns:[[
            {field:'dm_datetime',title:'날짜',width:80,align:'center'},
            {field:'new_count',title:'신규 회원수',width:80,align:'center'},
            {field:'leave_count',title:'탈퇴 회원수',width:80,align:'center'}
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
            <h1>전체회원분석</h1>
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
		    <li><a onclick="location.href='/adm/new_member_month.do'">월별 신규회원 현황</a></li>
		    <li><a  onclick="location.href='/adm/all_member.do'">일별 전체회원 현황</a></li>
		    <li class="on"><a onclick="location.href='/adm/all_member_month.do'">월별 전체회원 현황</a></li>
		</ul>
		<script>var widthSize = $('.Contents').width();</script>
        <div class="State">
            <table class="tb">
                <thead>
                <tr>
                    <th>총 회원수</th>
                    <th>신규 회원수</th>
                    <th>탈퇴 회원수</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <strong id="total_count">0</strong>
                    </td>
                    <td>
                        <strong id="new_count">0</strong>
                    </td>
                    <td>
                        <strong id="leave_count">0</strong>
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