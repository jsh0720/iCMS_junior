<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/se2/js/HuskyEZCreator.js' />"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
var oEditors = [];
$(function(){
	$("#RSAModulus").val(window.opener.document.getElementById("RSAModulus").value);
	$("#RSAExponent").val(window.opener.document.getElementById("RSAExponent").value);	
});

function modifyComment() {        
	if (confirm("댓글을 수정하시겠습니까?")) {
		<c:if test="${commentVO.mb_id eq '비회원'}">
			var wr_password = $("#wr_password").val();
	    	if (wr_password != "" && wr_password != null) {
	    		var rsa = new RSAKey();
	    		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
	    		$("#wr_password").val(rsa.encrypt(wr_password));
	    	}
		</c:if>
		
	    var form_data = $("#commentVO").serialize();
	    $("#wr_password").val("");
	    $.ajax({
	        url : '/write/set_comment.do',
	        dataType : 'json',
	        type : 'post',
	        data : form_data,
	        success : function (data) {
	            alert(data.notice);
	            if (data.result == 'success' || data.result == "expired") {
	            	window.opener.location.reload();
	            	self.close();
	            }
	        }, error:function(request, status, error) {
	            alert(request.responseJSON.notice);
	        }
	    });
	}
}

function chk_cancel() {
	if (confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")) {
		self.close();
	}
}
</script>

<style>
table{border-collapse:collapse;border-spacing:0;}
tr {
	margin: 0;
	padding: 0;
	border: 0;
	word-break: keep-all;
	font-style: inherit;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
	font-weight: 400;
	letter-spacing:-0.05em;
	text-size-adjust: none;
	line-height: 1.6;
}
.modify_title {font-size:17px; text-align:center;} 
.modify_table {width:100%; border-top:2px solid #333; border-bottom:1px solid #999; margin-bottom:15px;}
.modify_table tr {border-bottom:1px solid #ddd;}
.modify_table tr th {padding:15px 0; text-align:center; color:#111; font-size:15px; font-weight:700; word-break:keep-all; background:#fff;}
.modify_table tr td {padding:15px 20px; line-height:1.5; word-break:keep-all;}
.modify_table tr td textarea {width:100%;}

.modify_btn {display:table; margin:0 auto;}
.modify_btn a {display:inline-block; font-size:14px; font-weight:300; color:#fff; border:1px solid #c6c6c6; padding:7px 15px; text-decoration:none; margin:0 3px;}
.modify_btn a.confirm {background:#4b449c;}
.modify_btn a.cancel {background:#23272b;}

.noty {margin:5px 0 0 0; font-size:14px;}
.required_text {text-align:right; margin:0 0 5px 0; font-size:13px; font-weight:600;}
.required_value {color:#FF1515; font-size:15px; vertical-align:middle; padding-left:4px; font-weight:bold;}
</style>

<form:form commandName="commentVO">
	<input type="hidden" id="RSAModulus"/>
	<input type="hidden" id="RSAExponent"/>
	<input type="hidden" name="type" id="type" value="update"/>
	<input type="hidden" name="dm_id" value="<c:out value='${boardVO.dm_id}'/>"/>
	<form:hidden path="wr_id"/>
	<form:hidden path="wr_subject"/>
	
	<h1 class="modify_title">댓글수정</h1>
	<p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
	<table class="modify_table">
		<tbody>
			<c:if test="${boardVO.dm_use_comment_secret eq 1}">
				<c:if test="${writeOriVO.mb_id ne '비회원' && commentVO.reply_mb_id ne '비회원'}">
					<tr>
						<th><label for="wr_option">비밀글 사용</label></th>
						<td><form:checkbox path="wr_option" value="secret"/><label for="wr_option">비밀글</label></td>
					</tr>				
				</c:if>
			</c:if>
			<c:choose>
				<c:when test="${commentVO.mb_id eq '비회원'}">
					<tr>
						<th><label for="wr_password">비밀번호</label></th>
						<td>
							<form:password path="wr_password" autocomplete="new-password"/>
							<p class="noty">★ 비밀번호 변경시에만 비밀번호를 입력해주세요.</p>
						</td>
					</tr>
					<tr>
						<th><label for="wr_name">작성자<span class="required_value">*</span></label></th>
						<td><form:input path="wr_name"/></td>
					</tr>					
				</c:when>
				<c:otherwise>
					<tr>
						<th>작성자</th>
						<td>
							<c:out value="${commentVO.wr_name}"/>
							<form:hidden path="wr_name"/>
						</td>
					</tr>					
				</c:otherwise>
			</c:choose>
			<tr>
				<th><label for="wr_content" class="sound">내용<span class="required_value">*</span></label></th>
				<td>
					<textarea id="wr_content" name="wr_content" rows="5" placeholder="댓글 내용을 입력해주세요."><c:out value="${commentVO.wr_content}" escapeXml="false"/></textarea>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="modify_btn">
		<a href="javascript:modifyComment()" class="confirm">수정</a>
		<a href="javascript:chk_cancel();" class="cancel">닫기</a>
	</div>
</form:form>