<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
	var root = "<c:out value='${param.root}'/>";
	if (root == '') {
		root = "<c:out value='${root}'/>"
	}
	<c:choose>
		<c:when test="${message == 'member.login.notlogin'}">
			alert("로그인 상태가 아닙니다.");
			document.location.href = root + "/index.do";
		</c:when>
				
		<c:when test="${message == 'member.logout.success'}">
			alert("로그아웃 하였습니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.logout.fail'}">
			alert("로그아웃 중 오류가 발생하였습니다. 관리자에게 문의주세요.");
			document.location.href = root + "/index.do";
		</c:when>				
		
		<c:when test="${message == 'member.encrypt.error'}">
			alert("암호화 키 생성오류입니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:when>	
		
		<c:otherwise>
			alert("알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:otherwise>
	</c:choose>		
</script>