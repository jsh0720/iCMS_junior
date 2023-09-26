<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/adm/page_header.do" />
<style>
	.cover input, progress {
		accent-color: gray;
		color: gray;
	}
</style>
<div class="easyui-layout" style="width:700px;height:350px;" fit="true">
    <div data-options="region:'north', border:false">
    	<div class="title">
            <h1>환경설정</h1>
            <div class="btnWrap">
                <button id="fnSave">저장</button>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false" class="Contents">
    	<div class="easyui-layout" fit="true" data-options="border:false">
            <div data-options="region:'center', border:true">
            	<form id="fm" method="post" autocomplete="off">
            		<input type="hidden" id="dm_id" name="dm_id">
            		<div class="page">
                        <h3><span id="title">회원제 설정</span></h3>
                        <dl>
                            <dt>회원제 사용여부</dt>
                            <dd>
                                <select id="dm_is_member" name="dm_is_member" class="easyui-combobox" panelHeight="auto"
		                            data-options="url: '/adm/select_code.do?dm_code_group=1001',
		                                                method: 'get',
		                                                valueField: 'dm_code_value',
		                                                textField: 'dm_code_name',
		                                                editable: false">
	                    		</select>
                            </dd>
                        </dl>

                        <h3>회원가입 항목 설정</h3>
                        <table class="tb2 ujo8C">
                            <colgroup><col width="30%"><col span="2"><col width="30%"><col span="2"></colgroup>
                            <thead>
                            <tr>
                                <th>항목</th>
                                <th>사용</th>
                                <th>필수</th>
                                <th>항목</th>
                                <th>사용</th>
                                <th>필수</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="cover">
                            	<th>이메일</th>
                                <td><input name="dm_use_email" type="checkbox" id="dm_use_email" value="1"></td>
                                <td><input name="dm_require_email" type="checkbox" id="dm_require_email" value="1"></td>
                                <th>휴대폰번호</th>
                                <td><input name="dm_use_hp" type="checkbox" id="dm_use_hp" value="1"></td>
                                <td><input name="dm_require_hp" type="checkbox" id="dm_require_hp" value="1"></td>
                            </tr>
                            <tr>
                                <th>성별</th>
                                <td><input name="dm_use_sex" type="checkbox" id="dm_use_sex" value="1"></td>
                                <td><input name="dm_require_sex" type="checkbox" id="dm_require_sex" value="1"></td>
								<th>생년월일</th>
                                <td><input name="dm_use_birth" type="checkbox" id="dm_use_birth" value="1"></td>
                                <td><input name="dm_require_birth" type="checkbox" id="dm_require_birth" value="1"></td>
                            </tr>
                            <tr>
                                <th>닉네임</th>
                                <td><input name="dm_use_nick" type="checkbox" id="dm_use_nick" value="1"></td>
                                <td><input name="dm_require_nick" type="checkbox" id="dm_require_nick" value="1"></td>
                                <th>주소</th>
                                <td><input name="dm_use_addr" type="checkbox" id="dm_use_addr" value="1"></td>
                                <td><input name="dm_require_addr" type="checkbox" id="dm_require_addr" value="1"></td>
                            </tr>
                            <tr>
                                <th>홈페이지</th>
                                <td><input name="dm_use_homepage" type="checkbox" id="dm_use_homepage" value="1"></td>
                                <td><input name="dm_require_homepage" type="checkbox" id="dm_require_homepage" value="1"></td>
                                <th>추천인제도</th>
                                <td><input name="dm_use_recom" type="checkbox" id="dm_use_recom" value="1"></td>
                                <td><input name="dm_require_recom" type="checkbox" id="dm_require_recom" value="1"></td>
                            </tr>
                            <tr>
                                <th>전화번호</th>
                                <td><input name="dm_use_tel" type="checkbox" id="dm_use_tel" value="1"></td>
                                <td><input name="dm_require_tel" type="checkbox" id="dm_require_tel" value="1"></td>
                                <th></th>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th>자기소개</th>
                                <td><input name="dm_use_introduce" type="checkbox" id="dm_use_introduce" value="1"></td>
                                <td><input name="dm_require_introduce" type="checkbox" id="dm_require_introduce" value="1"></td>
                                <th></th>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr style="display: none;">
                                <th><input name="dm_member_txt_1_name" type="text" value="" class="wd100" id="dm_member_txt_1_name"></th>
                                <td><input name="dm_use_member_txt_1" type="checkbox" value="y" id="dm_use_member_txt_1"></td>
                                <td><input name="dm_require_member_txt_1" type="checkbox" value="y" id="dm_require_member_txt_1"></td>
                                <th><input name="dm_member_txt_2_name" type="text" class="wd100" value="" id="dm_member_txt_2_name"></th>
                                <td><input name="dm_use_member_txt_2" type="checkbox" value="y" id="dm_use_member_txt_2"></td>
                                <td><input name="dm_require_member_txt_2" type="checkbox" value="y" id="dm_require_member_txt_2"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
            	</form>
            </div>
		</div>
    </div>
</div>

<script>
function selectedConfig() {
	$.ajax({
		url : "/adm/get_member_config.do",
		type : "POST",
		success : function (data) {
			if(data.result == "success") {
				$("#type").val("update");
				$("#dm_id").val(data.rows.dm_id);
				$("#dm_is_member").combobox('reload', '/adm/select_code.do?dm_code_group=1001&selected='+data.rows.dm_is_member);
				$("#dm_is_adult").combobox('reload', '/adm/select_code.do?dm_code_group=1001&selected='+data.rows.dm_is_adult);
				delete data.rows.dm_create_dt;
				delete data.rows.dm_create_id;
				delete data.rows.dm_modify_dt;
				delete data.rows.dm_modify_id;
				delete data.rows.dm_is_member;
				delete data.rows.dm_id;
				
				var keySet = Object.keys(data.rows);
				$.each(keySet, function(i, obj) {
					if (data.rows[obj]) {
						$("#"+obj).prop("checked",true);
					}
				});
				
			} else if (data.result == "fail"){
				$.messager.alert('경고', data.notice, 'warning');
			}
		}, error:function(request,status,error) {
			if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
			}
		}
	});
}

$(document).on("change", "#dm_use_nick, #dm_use_birth, #dm_use_sex, #dm_use_homepage, #dm_use_addr, #dm_use_recom, #dm_use_tel, #dm_use_introduce", function(){
	var ele = $(this);
	var flag = $(this).is(":checked");
	if (!flag) {
		var target = $(ele).closest("td").next().children("input");
		$(target).prop("checked", false);
	}
});

$(document).on("change", "#dm_require_nick, #dm_require_birth, #dm_require_sex, #dm_require_homepage, #dm_require_addr, #dm_require_recom, #dm_require_tel, #dm_require_introduce", function(){
	var ele = $(this);
	var flag = $(this).is(":checked");
	if (flag) {
		var target = $(ele).closest("td").prev().children("input");
		$(target).prop("checked", true);
	}
});

$(document).on("click", "#dm_require_hp, #dm_require_email, #dm_use_email, #dm_use_hp", function(e){
	e.preventDefault;
	$.messager.alert("안내", "이메일, 휴대폰 번호의 경우<br>아이디/비밀번호 찾기에 이용되어<br>필수 입력사항입니다.", "warning");
	return false;
});

function fnSave() {
	$(".btnWrap").hide();
	var form = $("#fm")[0];
    var formData = new FormData(form);
    
	$.ajax({
		url : "/adm/set_member_config.do",
		data : formData,
		dataType : "json",
		type : "POST",
		contentType: false,
        processData: false,
        success : function (data) {
        	if(data.result == "success") {
        		alert(data.notice);
    			location.reload();
    		} else {
    			$.messager.alert('경고', data.notice, 'warning');
    			$(".btnWrap").show();
    		}
        }, error : function(request, status, error) {
        	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert('경고',request.responseJSON.notice,'warning');
	            $(".btnWrap").show();
			}
        }
	});
}

$(function(){
	selectedConfig();
	$("#fnSave").off().on('click', function () {
    	if(confirm("정말로 저장하시겠습니까?")){
        	fnSave();
    	}
    });
	
	$(document).on("keyup", ".level", function() { 
    	$(this).val($(this).val().replace(/[^a-zA-Zㄱ-ㅎ가-힣0-9]/g, "")); 
    });
});
</script>

<c:import url="/adm/page_bottom.do" />