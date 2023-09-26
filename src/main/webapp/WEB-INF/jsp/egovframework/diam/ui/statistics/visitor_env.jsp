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
        url : "/adm/get_visit_env.do?mode=get_env&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        dataType:"json",
        success : function (data) {
            $("#pc_percent").text(data.pc_percent);
            $("#mobile_percent").text(data.mobile_percent);
            $("#max_pc_count").text(data.max_pc_count);
            $("#max_pc_name").text(data.max_pc_name);
            $("#max_mobile_name").text(data.max_mobile_name);
            $("#max_mobile_count").text(data.max_mobile_count);
        }
    });

    $.ajax({
        url : "/adm/get_visit_env.do?mode=weekend_env&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        dataType:"json",
        success : function (data) {
            $("#chart-area").empty();
            var series_data = data.os;
            var temp = [];
            for (var key in series_data) {
                var tt = {
                    name : series_data[key]['os'],
                    data : series_data[key]['count']
                };
                temp.push(tt);
            }
            var container = document.getElementById('chart-area'),
                chart_data = {
                    categories: data.date,
                    series : temp,
                },
                options = {
                    chart: {
                        width: widthSize, height: 400
                    },
                    yAxis: {
                        min: 0,
                        title: ''
                    },
                    xAxis: {
                        title: ''
                    },
                    series: {
                        hasDot: false, showDot: true
                    },
                    tooltip: {
                        suffix: ''
                    },
                    legend: {
                        showCheckbox: true, align: 'right'
                    }
                }
            tui.chart.lineChart(container, chart_data, options);
        }
    });
}

function fnSetTable(){
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');
    var search_domain = $("#search_domain").combobox('getValue');
    
    $('#dg').datagrid({
        url:'/adm/get_visit_env.do?mode=weekend_env_table',
        columns:[[
            {field:'number',title:'순위',width:80,align:'center'},
            {field:'dm_os',title:'운영체제',width:80,align:'center'},
            {field:'count',title:'방문자수',width:80,align:'center'},
            {field:'percent',title:'비율',width:80,align:'center'}
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
            <h1>방문자환경</h1>
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
	    <ul class="Tabs mt30">
		    <li class="on"><a  onclick="location.href='/adm/visitor_env.do'">운영체제 현황</a></li>
		    <li><a onclick="location.href='/adm/visitor_browser.do'">브라우저 현황</a></li>
		</ul>
		<script>var widthSize = $('.Contents').width();</script>
        <div class="State">
            <table class="tb">
                <thead>
                <tr>
                    <th>PC OS 총점유율</th>
                    <th>모바일 OS 총점유율</th>
                    <th>PC 최다OS</th>
                    <th>모바일 최다OS</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="bln">
                        <strong id="pc_percent">0</strong> %
                    </td>
                    <td>
                        <strong id="mobile_percent">0</strong> %
                    </td>
                    <td>
                        <strong id="max_pc_count">0</strong>
                        <em><b id="max_pc_name">winodws</b></em></td>
                    <td>
                        <strong id="max_mobile_count">0</strong>
                        <em><b id="max_mobile_name">iPhone</b></em></td>
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
                <%--
                <thead>
                <tr>
                    <th data-options="field:'number',width:80,align:'center'">순위</th>
                    <th data-options="field:'site',width:80,align:'center'">검색엔진</th>
                    <th data-options="field:'count',width:80,align:'center'">유입수</th>
                    <th data-options="field:'percent',width:80,align:'center'">비율</th>
                </tr>
                </thead>
                --%>
            </table>
        </div>    
    </div>
</div>    
<c:import url="/adm/page_bottom.do" />