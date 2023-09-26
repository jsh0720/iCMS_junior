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
        url : "/adm/get_visit_orgin.do?mode=orgin_info&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        type:"post",
        dataType : "json",
        success : function (data) {
            $("#search_count").text(data.search_count);
            $("#search_percent").html(data.search_percent+"<em>%</em>");
            $("#top_eng").html(data.top_eng+"<em>%</em>");
            $("#total_count").text(data.total_count);
        }
    });

    $.ajax({
        url : "/adm/get_visit_orgin.do?mode=weekend_orgin&search_start_date="+search_start_date+"&search_end_date="+search_end_date+"&search_domain="+search_domain,
        type:"post",
        dataType : "json",
        success : function (data) {
            $("#chart-area").empty();
            var widthSize = $('.Contents').width();
            var container = document.getElementById('chart-area'),
                chart_data = {
                    categories: data.date,
                    series: [
                            {
                                name: '네이버',
                                data: data.naver
                            },
                            {
                                name: '다음',
                                data: data.daum
                            },
                            {
                                name: '구글',
                                data: data.google
                            },
                            {
                                name: '네이트',
                                data: data.nate
                            },
                            {
                                name: '빙',
                                data: data.bing
                            },
                            {
                                name: '기타',
                                data: data.etc
                            }
                    ]
                },
                options = {
                    chart: {
                        width: widthSize, height: 400 /*,title: ''*/
                    },
                    yAxis: {
                        title: '', min: 0
                    },
                    xAxis: {
                        title: ''
                    },
                    tooltip: {
                        suffix: ''/* ,grouped: true*/
                    },
                    series: {
                        showLabel: false, showDot: true
                    },
                    legend: {
                        showCheckbox: true, align: 'bottom'
                    }
                };
            tui.chart.lineChart(container, chart_data, options);
        }
    });
}

function fnSetTable(){
	var search_start_date = $("#search_start_date").datebox('getValue');
    var search_end_date = $("#search_end_date").datebox('getValue');
    var search_domain = $("#search_domain").combobox('getValue');
    
    $('#dg').datagrid({
        url:'/adm/get_visit_orgin.do?mode=all_orgin',
        columns:[[
            {field:'number',title:'순위',width:80,align:'center'},
            {field:'site',title:'검색엔진',width:80,align:'center'},
            {field:'count',title:'유입수',width:80,align:'center'},
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
            <h1>방문자경로</h1>
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
        <div class="State">
            <table class="tb">
                <thead>
                <tr>
                    <th>검색유입수</th>
                    <th>전체유입수</th>
                    <th>검색유입율</th>
                    <th>TOP검색엔진</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><strong id="search_count">0</strong></td>
                    <td><strong id="total_count">0</strong></td>
                    <td><strong id="search_percent">0<em>%</em></strong></td>
                    <td><strong id="top_eng">0<em>%</em></strong></td>
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