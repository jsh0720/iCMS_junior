<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	var root = "<c:out value='${param.root}'/>";
	if (root == '') {
		root = "<c:out value='${root}'/>"
	}
	
	<c:choose>
		<c:when test="${message == 'member.success.join'}">
			alert("회원가입이 완료되었습니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.success.modify'}">
			alert('<c:out value="${notice}"/>');
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.info.notfound'}">
			alert("회원 정보가 없습니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.info.admin'}">
			alert("관리자 계정은 정보 수정이 불가능합니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.encrypt.error'}">
			alert("암호화 키 생성오류입니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.admin.notleave'}">
			alert("관리자 계정은 회원탈퇴가 불가능합니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.fault.access'}">
			alert("잘못된 접근입니다.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.already.login'}">
			alert("로그인 중에는 회원가입을 할 수 없습니다. 로그아웃 후 진행해주세요.");
			document.location.href = root + "/index.do";
		</c:when>
		
		<c:when test="${message == 'member.not.login'}">
			alert("로그인이 필요한 서비스입니다. 로그인 후 진행해주세요.");
			document.location.href = root + "/index.do";
		</c:when>		
		
		<c:when test="${message == 'member.invalid.command'}">
			alert("유효하지 않는 명령값입니다.");
			document.location.href = root + "/index.do";
		</c:when>

		<c:when test="${message == 'member.success.leave'}">
			alert("회원탈퇴하였습니다. 그동안 이용해주셔서 감사합니다.");
			document.location.href = root + "/index.do";
		</c:when>
				
		<c:when test="${message == 'member.same.password'}">
			alert("이전과 동일한 비밀번호로는 변경불가합니다. 이전 비밀번호와 다른 비밀번호를 입력해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.id.duplicate'}">
			alert("중복된 아이디입니다. 다른 아이디를 입력해주세요.");
			history.back(-1);
		</c:when>
		<c:when test="${message == 'member.email.duplicate'}">
			alert("중복된 이메일입니다. 다른 이메일를 입력해주세요.");
			history.back(-1);
		</c:when>
		<c:when test="${message == 'member.nick.duplicate'}">
			alert("중복된 닉네임입니다. 다른 닉네임를 입력해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.notenter.password'}">
			alert("패스워드를 입력해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.invalid.password'}">
			alert("패스워드 형식에 맞지않습니다. 영어 대/소문자,숫자,특수문자를 1개이상 포함하여 8자 이상 입력해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.fail.validate'}">
			alert("<c:out value='${notice}'/>");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.encrypt.expired'}">
			alert("암호화 키가 만료되었습니다. 새로고침 후 다시 진행해주세요.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.sql.error'}">
			alert("SQL 구문오류가 발생하였습니다.");
			history.back(-1);
		</c:when>
		
		<c:when test="${message == 'member.other.error'}">
			alert("회원정보 등록 중 오류가 발생하였습니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:when>
		
		<c:otherwise>
			alert("알 수 없는 오류가 발생하였습니다. 관리자에게 문의주세요.");
			history.back(-1);
		</c:otherwise>
	</c:choose>
		
</script>