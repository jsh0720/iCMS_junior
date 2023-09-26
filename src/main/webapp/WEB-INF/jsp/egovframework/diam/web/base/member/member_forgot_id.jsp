<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	#findIdResult p {
		margin-bottom: 10px;
		font-size:25px;
	}
	#findIdResult {
		display: none;
		text-align: center;
	}
</style>
<div id="mbFindID" class="mb_form">
    <h3>아이디 찾기</h3>
    <div class="mb_inner mb_find mb_find_id">
        <div class="alert alert-secondary">띄어쓰기 및 대소문자 구분해서 정확하게 입력해주세요.</div>

        <form id="fm" name="fm" method="post" autocomplete="off">
	        <input type="hidden" name="command" value="id" />
	        <fieldset class="">
	            <legend class="">아이디 찾기</legend>
	            <div class="">
	                <div class="form-group">
	                    <label for="dm_name">이름</label>
	                    <input type="text" name="dm_name" id="dm_name" class="form-control" placeholder="이름" />
	                </div>
	                <div class="form-group">
	                    <label for="dm_email">이메일</label>
	                    <input type="text" name="dm_email" id="dm_email" class="form-control" placeholder="이메일" />
	                </div>
	                <div class="form-group">
	                    <label for="dm_hp">휴대폰 번호</label>
	                    <input type="text" name="dm_hp" id="dm_hp" class="form-control" placeholder="휴대폰" maxlength="14" onkeyup="setTelPattern(this);">
	                </div>
	            </div>
	        </fieldset>
	        <div class="btn_wrap">
	            <button type="button" class="btn btn_submit search" onclick="fnFindInfo('id');">아이디 찾기</button>
	        </div>
        </form>
	</div>
	<div id="findIdResult">
		<br>
    	<h4>입력하신 정보로 검색한 결과입니다.</h4>
    	<br>
    	<section>
    	</section>
    	<br>
    </div>
    <div class="lnk_wrap">
		<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${login_uid }'/>">로그인</a>
		<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid }'/>&command=forgot_pw">비밀번호 찾기</a>
		<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid }'/>&command=terms">회원가입</a>
    </div>
</div>