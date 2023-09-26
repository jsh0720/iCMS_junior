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
    var search_domain = $("#search_domain").combobox('getValue');

    $.ajax({
    	url : "/adm/get_today_visit.do?mode=day_of_week_accessor&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        dataType: 'json',
        type: 'post',
        success: function (data) {
            $("#max_count").text(data.max_count);
            $("#max_date").text(data.max_date);
            $("#min_count").text(data.min_count);
            $("#min_date").text(data.min_date);
            $("#max_pv_count").text(data.max_pv_count);
            $("#max_pv_date").text(data.max_pv_date);
            $("#min_pv_count").text(data.min_pv_count);
            $("#min_pv_date").text(data.min_pv_date);

            var diff_m = data.max_count - data.min_count;
            var diff_pv = data.max_pv_count - data.min_pv_count;

            $("#diff_m").text(diff_m);
            $("#diff_pv").text(diff_pv);
        }
    });

    $.ajax({
        url: "/adm/get_today_visit.do?mode=day_of_week_accessor_chart&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        dataType: 'json',
        type: 'post',
        success: function (data) {
        	console.log(data);
            var visitor = data.visitor;
            var visit_count = data.visit_count;
            var new_count = data.new_count;
            var re_count = data.re_count;
            var pv = data.pv;
            $("#chart-area").empty();

            var container = document.getElementById('chart-area'),
                chart_data = {
                    categories: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'],
                    series: [
                        {
                            name: '방문자수',
                            data: visitor
                        },
                        /* {
                            name: '방문횟수',
                            data: visit_count

                        }, */
                        {
                            name: '신규방문자수',
                            data: new_count

                        },
                        {
                            name: '재방문자수',
                            data: re_count

                        },
                        {
                            name: '방문당 페이지뷰',
                            data: pv

                        }
                    ]
                },
                options = {
                    chart: {
                        width: widthSize, height: 400
                    },
                    yAxis: {
                        title: '',
                        min: 0
                    },
                    xAxis: {
                        title: ''
                    },
                    tooltip: {
                        suffix: '',grouped: true
                    },
                    series: {
                        showLabel: false, stackType: 'normal'
                    },
                    legend: {
                        showCheckbox: true, align: 'bottom'
                    }
                }
            tui.chart.barChart(container, chart_data, options);
        }
    });
}

function fnSetTable(){
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');
    var search_domain = $("#search_domain").combobox('getValue');
    $('#dg').datagrid({
        url:'/adm/get_today_visit.do?mode=day_of_week_accessor_table',
        columns:[[
            {field:'dm_date',title:'요일',width:80,align:'center'},
            {field:'dm_visitor',title:'방문자수',width:80,align:'center'},
            //{field:'visit_count',title:'방문횟수',width:80,align:'center'},
            {field:'new_count',title:'신규방문자수',width:80,align:'center'},
            {field:'re_count',title:'재방문자수',width:80,align:'center'},
            {field:'pv',title:'방문당 페이지뷰',width:80,align:'center'}
        ]],
        queryParams:{
         search_start_date : encodeURIComponent(search_start_date),
         search_end_date : encodeURIComponent(search_end_date),
         search_domain : search_domain
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
    
    $("#search_domain").combobox({
        onChange:function (newValue, oldValue) {
        	fnSetData();
            fnSetTable();
        }
    });
});
</script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>방문자분석</h1>
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
                        <a onclick="DateSearch.getPrevOneDays();" class="btn">전일</a>
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
        <ul class="Tabs mt30">
		    <li><a id="visitToday" onclick="location.href='/adm/visit.do'">전체 방문현황</a></li>
		    <li><a id="visitDay" onclick="location.href='/adm/visit_day.do'">일별 방문현황</a></li>
		    <li><a id="visitHour" onclick="location.href='/adm/visit_hour.do'">시간대별 방문현황</a></li>
		    <li class="on"><a id="visitWeek" onclick="location.href='/adm/visit_week.do'">요일별 방문현황</a></li>
		    <li><a id="visitMonth" onclick="location.href='/adm/visit_month.do'">월별 방문현황</a></li>
		</ul>
		<script>var widthSize = $('.Contents').width();</script>
        <div class="State">
            <table class="tb">
                <thead>
                    <tr>
                        <th>최대 방문자수</th>
                        <th>최소 방문자수</th>
                        <th>최대 페이지뷰</th>
                        <th>최소 페이지뷰</th>
                        <th>최소·최대 편차</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>
                        <strong id="max_count">0</strong>
                        <em id="max_date">월요일</em>
                    </td>
                    <td>
                        <strong id="min_count">0</strong>
                        <em id="min_date">화요일</em>
                     </td>
                    <td>
                        <strong id="max_pv_count">0</strong>
                        <em id="max_pv_date">월요일</em>
                    </td>
                    <td>
                        <strong id="min_pv_count">0</strong>
                        <em id="min_pv_date">화요일</em>
                    </td>
                    <td>
                        <dl><dt>방문자수</dt><dd><strong id="diff_m">0</strong></dd></dl>
                        <dl><dt>페이지뷰</dt><dd><strong id="diff_pv">0</strong></dd></dl>
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