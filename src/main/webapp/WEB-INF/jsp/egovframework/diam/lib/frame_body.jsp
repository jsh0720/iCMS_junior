<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String ymd = format.format(today);
	String seven = format.format(today.getTime()-60*60*24*1000*6);
%>
<c:set var="ymd" value="<%=ymd%>"/>
<c:set var="sevenago" value="<%=seven%>"/>

<script>
    $(function () {
    	
    	weeklyNewMember();

    	getVisitorWeekly("today");
    	
    	$("#today_visitor_graph").off().on('click', function () {
            getVisitorWeekly('today');
        });

        $("#weekend_visitor_graph").off().on('click', function () {
            getVisitorWeekly('weekend');
        });

        $("#lastweek_visitor_graph").off().on('click', function () {
            getVisitorWeekly('last_week');
        });
    	
 		// today 방문자
 		$.ajax({
			url : "/adm/get_today_visit.do?mode=dash_board&search_type=total",
            type:"post",
            dataType : "json",
            success : function (data) {
                $("#today_visitor").text(data.today_visitor);
                $("#today_visitor").counterUp({
                    delay: 10,
                    time: 500
                });
                $("#today_compare").text(data.today_compare);
                $("#today_compare").counterUp({
                    delay: 10,
                    time: 500
				});
			}
		});
 		// 전체회원
 		$.ajax({
			url : "/adm/get_statistics_member.do?mode=dash_board",
            type:"post",
            dataType : "json",
            success : function (data) {
                $("#member_count").text(data.member_count);
                $("#member_count").counterUp({
                    delay: 10,
                    time: 500
                });
                $("#member_compare").text(data.member_compare);
                $("#member_compare").counterUp({
                    delay: 10,
                    time: 500
				});
			}
		});
 		
 		getBoardList();
    });
    
    function getBoardList(){
    	// 게시판
    	$.ajax({
            url : "/adm/statistics_board.do?mode=dash_board",
            type:"post",
            dataType : "json",
            success : function (data) {
            	var html = '';
            	var rows = data.rows;
            	for (var i = 0; i < rows.length; i++) {
            		html += '<tr>\n';
            		html += '    <td>'+rows[i].domain_name+'</td>\n';
            		html += '    <td>'+rows[i].board_name+'</td>\n';
            		html += '    <td>'+rows[i].total_count+'</td>\n';
            		html += '    <td>'+rows[i].write_count+'</td>\n';
            		html += '    <td>'+rows[i].reply_count+'</td>\n';
            		html += '    <td>'+rows[i].comment_count+'</td>\n';
            		html += '</tr>\n';
				}
                $("#board_body").html(html);
            }
        });
    }

    function getVisitorWeekly(type) {
    	var container = document.getElementById('chart-area2');
    	var widthSize = $('#chart-area2').width();
        $.ajax({
            url : "/adm/get_today_visit.do?mode=dash_board&search_type="+type,
            dataType:"json",
            success : function (data) {
                $('#chart-area2').empty();
                
                var data1 = data.dm_visit_count;
                var data2 = data.dm_new_visit_count;
                var data3 = data.dm_re_visit_count;
                
                var chart_data = {
                    categories: [data.search_date],
                    series: [
//                         {
//                             name: '방문자',
//                             data: data1
//                         },
                        {
                            name: '신규방문자',
                            data: data2
                        },
                        {
                            name: '재방문자',
                            data: data3
                        }
                    ]
                };
                var options = {
                    chart: {
                        width: widthSize - 50, height: 296 /*,title: ''*/
                    },
                    format: function(value, chartType, areaType, valuetype, legendName) {
                        if (areaType === 'makingSeriesLabel') { // formatting at series area
                            value = value + '%';
                        }
                        return value;
                    },
                    series: {
                        radiusRange: ['60%', '100%'],
                        showLabel: false
                    },
                    tooltip: {
                        suffix: '%'
                    },
                    legend: {
                        align: 'right'
                    }
                };
                tui.chart.pieChart(container, chart_data, options);
            }
        }); 
    }
    
    function weeklyNewMember() {
    	var container = document.getElementById('chart-area');
    	var widthSize = $('#chart-area').width();
    	$.ajax({
            url: "/adm/get_statistics_member.do?mode=weekly_member_chart&search_start_date=<c:out value="${sevenago}"/>&search_end_date=<c:out value="${ymd}"/>",
            dataType: "json",
            success: function (data) {
                $("#chart-area").empty();
                var container = document.getElementById('chart-area'),
                    chart_data = {
                        categories: data.date,
                        series: [
                            {
                                name: '신규회원',
                                data: data.total_count
                            },
                            {
                                name: '탈퇴회원',
                                data: data.leave_count
                            }
                        ]
                    },
                    options = {
                        chart: {
                            width: widthSize, height: 320 /*,title: ''*/
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
            }
        });
    }


</script>
<div data-options="region:'center',title:'',iconCls:'icon-ok'">
    <div id="m_frame" fit="true" border="false" class="easyui-tabs tab-main" style="margin:0px;height:auto" data-options="tools:'#tab-tools'">
        <div class="easyui-layout" fit="true">
        	 <div class="contents">
        	 	<div class="row">
        	 		<div class="col-md-6">
                        <div class="cardbox tilebox">
                            <i class="icon01"></i>
                            <h6>방문자</h6>
                            <h3 id="today_visitor">0</h3>
                            <span class="badge red mr-1" id="today_compare"> +00% </span> <span>전날 대비</span>
                        </div>
                    </div><!-- end col-->
                    <div class="col-md-6">
                        <div class="cardbox tilebox">
                            <i class="icon02"></i>
                            <h6>전체회원</h6>
                            <h3 id="member_count">0</h3>
                            <span class="badge yellow mr-1" id="member_compare"> +00% </span> <span>전달 대비</span>
                        </div>
                    </div>
        	 	</div>
				<div class="row">
                   <div class="col-lg-6 col-xl-4">
                        <div class="cardbox">
                            <h6 class="mb10">방문자 추이</h6>

                            <div class="btn-group">
                                <button type="button" class="btn" id="today_visitor_graph">Today</button>
                                <button type="button" class="btn" id="weekend_visitor_graph">이번주</button>
                                <button type="button" class="btn" id="lastweek_visitor_graph">지난주</button>
                            </div>

                            <div id="chart-area2"></div>
                        </div>
                    </div><!-- end col-->
                    <div class="col-lg-6 col-xl-8">
                        <div class="cardbox">
                            <h6>Weekly's 회원</h6>
                            <div id="chart-area"></div>
                        </div>
                    </div>
				</div>
				<div class="row">
                    <div class="col-lg-12 col-xl-12">
                        <div class="cardbox cardbox2">
                            <h6>게시판 현황</h6>
                            <table>
                                <thead>
	                                <tr>
	                                	<th>도메인명</th>
	                                    <th>게시판명</th>
	                                    <th>총 게시글(게시글+답글)</th>
	                                    <th>게시글</th>
	                                    <th>답글</th>
	                                    <th>댓글</th>
	                                </tr>
                                </thead>
                                <tbody id="board_body">
                              
                                </tbody>
                            </table>
                        </div>
                    </div><!-- end col-->
                </div>
        	 </div>
        </div>
    </div>
</div>
