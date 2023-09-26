<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
	#findPwResult p {
		margin-bottom: 10px;
		font-size:25px;
	}
	#findPwResult {
		display:none;
		text-align: center;
	}
	strong {
		color:red;
	}
	.highlighter {
		background-color: #D9FCDB;
	}
</style>
<div id="mbFindPW" class="mb_form">
    <h3>비밀번호 찾기</h3>
    <div class="mb_inner mb_find mb_find_pw">
        <div class="alert alert-secondary">띄어쓰기 및 대소문자 구분해서 정확하게 입력해주세요.</div>

        <form id="fm" name="fm" method="post" autocomplete="off">
        	<input type="hidden" name="command" value="pw" />
	        <fieldset class="">
	            <legend class="">비밀번호 찾기</legend>
	            <div class="">
	                <div class="form-group">
	                    <label for="dm_id">아이디</label>
	                    <input type="text" name="dm_id" id="dm_id" class="form-control" placeholder="아이디" />
	                </div>
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
	            <button type="button" class="btn btn_submit" onclick="fnFindInfo('pw');">비밀번호 찾기</button>
	        </div>
        </form>
	</div>
	<div id="findPwResult">
   		<h5>입력하신 정보의 비밀번호를 아래와 같이 변경했습니다.<br><strong>반드시 로그인 후 비밀번호를 변경하세요.</strong></h5>
   		<br>
   		<section>
   		</section>
   		<br>
    </div>
	<div class="lnk_wrap">
	    <a href="/index.do?contentId=bb93875fb9475c5b7218c891afea12da1d4888df92b6741b0c8f5a52977bd3ff">로그인</a>
	    <a href="/index.do?contentId=d8e57533a55dff78a384c42eeeab2b8591ff3056fe8b5cde6ad0f50d2d396e92">아이디 찾기</a>
	    <a href="/index.do?contentId=1bcc0f44a1fad12c44345edd9d2c7e82291ddaeb1f3652669a2ccf4f4b5dbb46">비밀번호 찾기</a>
	    <a href="/index.do?contentId=ccb6d378c4f7ce1fb3294b51471fb31f91f4fcad76fa79a56f61b689b31b2adc">회원가입</a>
	</div>

</div>
