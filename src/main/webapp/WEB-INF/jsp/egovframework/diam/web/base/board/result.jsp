<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js' />"></script>
<script type="text/javascript">
	var root = "<c:out value='${param.root}'/>";
	if (root == '') {
		root = "<c:out value='${root}'/>"
	}
	<c:choose>
		<c:when test="${message == 'board.success.write'}">
			alert("게시물이 정상적으로 등록되었습니다.");
			document.location.href = root + "/index.do?<c:out value='${param.writeQueryString}' escapeXml='false' />";
		</c:when>
		
		<c:when test="${message == 'board.success.modify'}">
			alert("게시물이 정상적으로 수정되었습니다.");
			document.location.href = root + "/index.do?<c:out value='${param.writeQueryString}' escapeXml='false' />";
		</c:when>
		
		<c:when test="${message == 'board.success.delete'}">
			alert("게시물이 정상적으로 삭제되었습니다.");
			document.location.href = root + "/index.do?" + unescapeHtml("<c:out value='${deleteSearchMap}' escapeXml='false' />");
		</c:when>
		
		<c:when test="${message == 'board.comment.delete'}">
			alert("댓글이 정상적으로 삭제되었습니다.");
			document.location.href = root + "/index.do?command=view&wr_id=<c:out value='${wr_id}'/>&<c:out value='${deleteSearchMap}' escapeXml='false' />";
		</c:when>		
		
		<c:when test="${message == 'board.list.auth'}">
			alert("글목록 권한이 없습니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'board.read.auth'}">
			alert("글 상세보기 권한이 없습니다.");
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
		</c:when>	
		
		<c:when test="${message == 'board.write.auth'}">
			alert("글 쓰기 권한이 없습니다");
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
		</c:when>	
		
		<c:when test="${message == 'board.modify.auth'}">
			alert("글 수정 권한이 없습니다.");
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
		</c:when>
		
		<c:when test="${message == 'board.modify.notice'}">
			alert("답글이 있는 게시글은 공지로 등록할 수 없습니다.");
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
		</c:when>
		
		<c:when test="${message == 'board.reply.auth'}">  
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
			alert("글 답변 권한이 없습니다.");
		</c:when>
		
		<c:when test="${message == 'board.reply.notice'}">  
			document.location.href = root + "/index.do?contentId=<c:out value='${dm_uid}'/>";
			alert("공지 게시글에는 답변을 작성할 수 없습니다.");
		</c:when>
		
		<c:when test="${message == 'board.success.reply'}">
			alert("답변글이 정상적으로 등록되었습니다.");
			document.location.href = root + "/index.do?<c:out value='${param.writeQueryString}' escapeXml='false' />";
		</c:when>
		
		<c:when test="${message == 'board.paging.error'}">
			alert("페이지/페이지당 행개수 데이터가 올바르지 않습니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'board.invalid.command'}">
			alert("유효하지 않는 명령값입니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'board.notfound.info'}">
			alert("게시판 정보가 없습니다.");
			document.location.href = root + "/index.do";
		</c:when>	
		
		<c:when test="${message == 'board.file.error'}">
			alert("파일 업로드 중 오류가 발생하였습니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.sql.error'}">
			alert("SQL 구문오류가 발생하였습니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.other.error'}">
			alert("게시글 CRUD 작업 중 오류가 발생하였습니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.invalid.password'}">
			alert("비밀번호가 누락되었습니다. 비밀번호를 입력해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.notfound.write'}">
			alert("게시글 정보가 없습니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.prohibit.ext'}">
			alert("업로드가 금지된 확장자입니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.fail.validate'}">
			alert("<c:out value='${notice}'/>");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.fault.access'}">
			alert("잘못된 접근입니다.");
			history.back(-1);
		</c:when>
			
		<c:when test="${message == 'board.encrypt.expired'}">
			alert("암호화 키가 만료되었습니다. 새로고침 후 다시 진행해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'board.encrypt.error'}">
			alert("암호화 키 생성오류입니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:when>		
		
		<c:otherwise>
			alert("알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:otherwise>
	</c:choose>
</script>