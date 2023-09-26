<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	String wr_id = request.getParameter("wr_id") != null ? request.getParameter("wr_id") : "";
	String wr_board = request.getParameter("wr_board") != null ? request.getParameter("wr_board") : "";
%>
<c:set var="wr_parent" value="<%=wr_id%>"/>
<c:set var="wr_board" value="<%=wr_board%>"/>
<c:import url="/adm/page_header.do" />
<style>
  	.tree-icon{
		display: none;
	}
</style>
<script>
var uWidth = 1024;
var uHeight = 768;
var option = "scrollbars=auto,toolbar=no,location=no,status=no,menubar=no,resizable=yes,titlebar=no,width="+uWidth+",height="+uHeight+",left=0,top=0";

function fnNew(parent) {
	window.open("/adm/reply_form.do?wr_board=${wr_board}&wr_parent="+parent,"new_write",option);
}

function formatDetail3(value,row){
	var str = "";
	if (row.dm_delete_yn == "N") {
		str = '<a class="btn" onclick="javascript:fnNew('+"'" +row.wr_id+ "'"+');">답글작성</a>';
	}
	return str;
}

function formatDetail(value,row){
	var str = "";
	if (row.dm_delete_yn == "N") {
		str = '<a class="btn bt08 modify" data-id="'+row.wr_id+'">수정</a> '
			+ '<a class="btn remove" data-id="'+row.wr_id+'">삭제</a>';
	}
	return str;
}

$(function () {
	$('#tt').treegrid({
        emptyMsg:'데이터가 없습니다.',
	 	onLoadError: function(request, status, error) {
	 		if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
	        	$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
	 	}
    });
	
	var parent = "${wr_parent}";
	if (parent != null && parent != "") {
		$.ajax({
			url: "/adm/get_write.do",
			data: {wr_id : parent},
			type: "post",
			success: function(data) {
				if (data.result == "success") {
					$("#subject").text(data.rows.wr_subject);
					$("#content").append(data.rows.wr_content);
					$("#datetime").text(data.rows.wr_datetime);
					$("#mb_id").text(data.rows.mb_id);
					if (data.wr_file_array.length > 0) {
						var str = "";
						for (var i=0; i<data.wr_file_array.length; i++) {
							if (i != 0) {
								str += '<br>';
							}
							str += '<a href="'+data.wr_file_array[i]+'" target="_blank" >'+data.wr_file_ori_array[i]+'</a>';
						}
						$("#attach").append(str);
					}
					var linkStr = "";
					if (data.rows.wr_link1 != "") {
						linkStr += '<a href="'+unescapeHtml(data.rows.wr_link1)+'" target="_blank">'+unescapeHtml(data.rows.wr_link1)+'</a>';
					}
					if (data.rows.wr_link2 != "") {
						if (linkStr.length > 0) {
							linkStr += '<br>';
						}
						linkStr += '<a href="'+unescapeHtml(data.rows.wr_link2)+'" target="_blank">'+unescapeHtml(data.rows.wr_link2)+'</a>';
					}
					$("#link").append(linkStr);
				}
			}, error:function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	$.messager.alert('경고',request.responseJSON.notice,'warning');
				}
	        }
		});
	}
	
    var board_id = "${wr_board}";
    if (board_id != null && board_id != "") {
		$.ajax({
			url: "/adm/get_board.do",
			data: {dm_id : board_id},
			type: "post",
			success: function(data) {
				if (data.result == "success") {
					if (data.rows.dm_is_reply != "1" || data.rows.dm_skin != "basic") {
						$("#fnNew").remove();
						$('#tt').treegrid({
					        emptyMsg:'<b>답글이 허용되지 않은 게시판입니다.<br>게시판 설정을 확인하세요.</b>'
					    });
						$("#tt").treegrid("updateRow", {index: 0, row: null})
					}
				} else {
					$.messager.alert('경고', data.notice, 'warning');
				}
			}, error:function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	$.messager.alert('경고',request.responseJSON.notice,'warning');
				}
	        }
		});
	}

    $("#fnNew").off().on('click', function () {
        fnNew("${wr_parent}");
    });

    $("#fnRemove").off().on('click', function () {
        fnRemove();
    });

    $(document).on('click', ".modify", function () {
        var dm_id = $(this).data("id");
        window.open('/adm/reply_form.do?wr_id='+dm_id+"&wr_board=${wr_board}&wr_parent=${wr_parent}","modify_write",option);
    });
    
    $(document).on("click", ".remove", function(){
   		var id = $(this).data("id");
   		var ids = new Array();
       	$.messager.confirm("삭제 알림", "정말 삭제하시겠습니까?", function (r) {
       		if (r) {
       			ids.push(id);
       			if (id != null && id != "") {
					$.ajax({
						url: "/adm/delete_write.do",
						type: "post",
						data: {wr_id: ids},
						success: function(data) {
							$.messager.alert("info", data.notice, "info");
							$("#tt").treegrid("reload");
						}, error:function(request, status, error) {
							if (request.status == "303") {
								location.replace("/adm/login.do");
							} else {
					        	$.messager.alert('경고',request.responseJSON.notice,'warning');
							}
				        }
					});
				} else {
					$.messager.alert('경고', "잘못된 접근입니다. 관리자에게 문의해주세요.", 'warning');
				}
       		}
       	});
    });
});
</script>
<script type="text/javascript" src="https://www.jeasyui.com/easyui/datagrid-detailview.js"></script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>답글 관리</h1>
            <div class="btnWrap">
                <button onclick="javascript:location.href='/adm/write_list.do';" class="bt03">뒤로가기</button>
                <button id="fnNew" class="bt01">신규생성</button>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false" class="Contents">
		<div class="page">
			<h3><span id="title">게시글</span></h3>
			<dl>
			    <dt>제목</dt>
			    <dd id="subject"></dd>
			</dl>
			<dl>
				<dt>내용</dt>
				<dd id="content"></dd>
			</dl>
			<dl>
				<dt>작성일</dt>
				<dd id="datetime"></dd>
				<dt>작성자</dt>
				<dd id="mb_id"></dd>
			</dl>
			<dl>
				<dt>링크</dt>
				<dd id="link"></dd>
				<dt>첨부</dt>
				<dd id="attach"></dd>
			</dl>
			<br><br>
	        <table id="tt"
	               class="easyui-treegrid" fit="true" border="false"
	               data-options="lines: true,idField:'wr_id', treeField:'wr_subject',rownumbers:true,singleSelect:true,url:'/adm/selectReplyList.do?wr_id=${wr_parent}',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" fit="true">
	            <thead>
	            <tr>
	                <th data-options="field:'wr_subject',width:100,align:'left'">제목</th>
	                <th data-options="field:'wr_content',width:300,align:'left'">내용</th>
	                <th data-options="field:'wr_name',width:40,align:'center'">작성자</th>
	                <th data-options="field:'wr_datetime',width:60,align:'center'">작성일</th>
	                <th field="detail3" width="30" formatter="formatDetail3" align="center">답변</th>
	                <!-- <th field="detail2" width="30" formatter="formatDetail2" align="center">바로가기</th> -->
	                <th field="detail" width="40" formatter="formatDetail" align="center">관리</th>
	            </tr>
	            </thead>
	        </table>
		</div>
    </div>
    <div data-options="region:'south'">
    </div>
</div>
<c:import url="/adm/page_bottom.do" />