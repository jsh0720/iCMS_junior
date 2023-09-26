<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String wr_id = request.getParameter("wr_id") != null ? request.getParameter("wr_id") : "";
	String wr_board = request.getParameter("wr_board") != null ? request.getParameter("wr_board") : "";
%>
<c:set var="wr_id" value="<%=wr_id%>"/>
<c:set var="wr_board" value="<%=wr_board%>"/>
<c:import url="/adm/page_header.do" />
<style>
  	.tree-icon{
		display: none;
	}
</style>
<script>
function formatDetail(value,row){
    return '<a class="btn remove" data-id="'+row.wr_id+'">삭제</a>';
}

function formatDetail2(value, row) {
	var href = "javascript:modify('"+row.wr_id+"')";
	var str = '<a class="btn bt08" href="'+href+'">수정</a>';
	return str;
}
function formatDetail3(value, row) {
	var str = "";
	if (row.wr_comment == "1") {
		str += '<a class="btn reComment" data-id="'+row.wr_id+'">댓글</a> '
	}
	return str;
}

$(document).on("click", ".remove", function(){
	var id = $(this).data("id");
	
	$.messager.confirm("경고", "댓글과 대댓글까지 모두 삭제됩니다. 삭제하시겠습니까?", function (r) {
		if (r) {
			$.ajax({
				url: "/adm/deleteComment.do",
				type: "post",
				data: {wr_id : id},
				success: function(result) {
					$.messager.alert('경고', result.notice, 'warning');
					if (result.result == "success") {
						$("#tt").treegrid("reload");
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
	});
});

$(function () {	
	closeDialog();
	
	$('#tt').treegrid({
		emptyMsg:'댓글이 없습니다.',
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
	
    var board_id = "${wr_board}";
    if (board_id != null && board_id != "") {
		$.ajax({
			url: "/adm/get_board.do",
			data: {dm_id : board_id},
			type: "post",
			success: function(data) {
				if (data.result == "success") {
					if (data.rows.dm_use_comment_secret != "1") {
						$(".secretOption").remove();
					}
					if (data.rows.dm_is_comment != "1") {
						$(".btnWrap button").remove();
						$(".btnWrap").append("<b>댓글이 허용되지 않은 게시판입니다.<br>게시판 설정을 확인하세요.</b>");
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
});

function closeDialog() {
	$("#dm_domain_nm").val("");
	$("#dm_domain_id").val("");
	$("#dlg").dialog('close');
}

$(document).on("click", "#fnClose", function(){
	closeDialog();
});

//댓글 신규
$(document).on('click', "#fnNew", function () {
	$("#wr_parent").val("${wr_id}");
	$("#wr_content").val("");
	$("#wr_id").val("");
	$("#wr_option").prop("checked",false);
	openDialog();
});

//대댓글 신규
$(document).on("click", ".reComment", function(){
	var parent = $(this).data("id");
	$("#wr_id").val("");
	$("#wr_parent").val(parent);
	$("#wr_content").val("");
	$("#wr_option").prop("checked",false);
	openDialog();
});

//댓글 수정
function modify(id){
	var tg = $('#tt');
	var opts = tg.treegrid('options');
	var row = tg.treegrid('find', id);  // find the row by id
	var option = row.wr_option;
	
	$("#wr_content").val(unescapeHtml(row.wr_content));
	$("#wr_id").val(id);
	$("#wr_parent").val("");
	if (option == "secret") {
		$("#wr_option").prop("checked",true);
	} else {
		$("#wr_option").prop("checked",false);
	}
	openDialog();
}

function openDialog() {
	$("#dlg").dialog({
	    title : "&nbsp;&nbsp;"
	}).dialog('open');
}

function fnSave() {
	
	
	$(".modalBtnWrap").hide();
	var form = $("#frm").serialize();
	$.ajax({
		url: "/adm/upsertComment.do",
		data: form,
		type: "post",
		success: function(data){
			$.messager.alert("info", data.notice , "info");
			if (data.result == "success") {
				closeDialog();
				$("#tt").treegrid("reload");
			}
			$(".modalBtnWrap").show();
		}, error:function(request, status, error) {
			if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
	            $(".modalBtnWrap").show();
			}
        }
	});
}

</script>
<script type="text/javascript" src="https://www.jeasyui.com/easyui/datagrid-detailview.js"></script>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north', border:false">
        <div class="title">
            <h1>댓글 관리</h1>
            <div class="btnWrap">
                <button id="fnNew" class="bt01">신규댓글</button>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false" class="Contents">
         <table id="tt"
               class="easyui-treegrid" fit="true" border="false"
               data-options="lines: true,idField:'wr_id', treeField:'wr_content',rownumbers:true,singleSelect:true,url:'/adm/selectCommentList.do?wr_id=${wr_id}',method:'post',fitColumns:true,striped:false,selectOnCheck:false,checkOnSelect:false" fit="true">
            <thead>
            <tr>
                <th data-options="field:'wr_content',width:300,align:'left'">댓글 내용</th>
                <th data-options="field:'wr_name',width:40,align:'center'">작성자</th>
                <th data-options="field:'wr_datetime',width:60,align:'center'">작성일</th>
                <th field="detail3" width="30" formatter="formatDetail3" align="center">댓글</th>
                <th field="detail2" width="30" formatter="formatDetail2" align="center">수정</th>
                <th field="detail" width="30" formatter="formatDetail" align="center">삭제</th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'south'">
    </div>
</div>
<div id="dlg" class="easyui-dialog" style="width:800px; height: 400px;">
	<form id="frm">
		<input type="hidden" name="wr_id" id="wr_id" value="${wr_id }"/>
		<input type="hidden" name="wr_num" id="wr_num" value="-${wr_id }"/>
		<input type="hidden" name="wr_parent" id="wr_parent" value="${wr_id }"/>
		<input type="hidden" name="wr_board" id="wr_board" value="${wr_board }">
		<div class="page">
		    <h3 id="dialog_title">댓글 작성/수정</h3>
		    <dl class="secretOption">
		        <dt>비밀글 설정</dt>
		        <dd>
		            <label>사용 <input id="wr_option" name="wr_option" type="checkbox" value="secret"/></label>
		        </dd>
		    </dl>
		    <dl>
		        <dt>내용<span class="required_value">*</span></dt>
		        <dd>
		            <textarea id="wr_content" name="wr_content"></textarea>
		        </dd>
		    </dl>
		</div>
	</form>
	<div style="text-align: center;" class="modalBtnWrap">
	    <a href="javascript:void(0);" id="fnClose" class="easyui-linkbutton" style="width:120px;height:30px;">취소</a>
	    <a href="javascript:fnSave();" id="fnSave" class="easyui-linkbutton" style="width:120px;height:30px;color:white;">저장</a>
	</div>
</div>
<c:import url="/adm/page_bottom.do" />