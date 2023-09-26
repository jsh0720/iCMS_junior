<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="mbAgree" class="mb_join">
    <h3>약관동의</h3>
    <div class="mb_inner mb_agree">
        <form onsubmit="return false">
        <div class="agree_docs">
            <h4>이용약관</h4>
            <div class="agree_text clause">
                ${CONFIG_INFO.dm_policy_text}
            </div>
            <div class="custom-control custom-checkbox agree_check">
                <input type="checkbox" name="dm_agree1" id="dm_agree1" class="custom-control-input dm_check" value="이용약관" />
                <label for="dm_agree1" class="custom-control-label" >위 이용약관에 동의합니다.</label>
            </div>
        </div>
        <div class="agree_docs">
            <h4 class="">개인정보처리방침</h4>
            <div class="agree_text clause">
                ${CONFIG_INFO.dm_private_text}
            </div>
            <div class="custom-control custom-checkbox agree_check">
                <input type="checkbox" name="dm_agree2" id="dm_agree2" class="custom-control-input dm_check"  value="개인정보처리방침" />
                <label for="dm_agree2" class="custom-control-label" >위 개인정보처리방침에 동의합니다.</label>
            </div>
        </div>
        <div class="btn_wrap">
            <button type="button" class="btn btn_submit dm_confirm" data-member-type="1">회원가입</button>
        </div>
        </form>
    </div>
</div>

<script>
$(function() {
    $('.dm_check').on('click', function() {
        var $btn = $('.dm_confirm'),
            $chk = $('.dm_check'),
            _len = $chk.length,
            _chk = $chk.filter(':checked').length;
        if (_len === _chk) {
            $btn.addClass('active');
        } else {
            $btn.removeClass('active');
        }
    });
    $('.dm_confirm').on('click', function () {
        var $this = $(this),
            _type = $this.data('memberType') || '1',
            $agree1 = $('#dm_agree1'),
            $agree2 = $('#dm_agree2');

        if (!$agree1.is(':checked')) {
            alert ("이용약관에 동의해주세요");
            $agree1.focus();
            return false;
        }

        if (!$agree2.is(':checked')) {
            alert ("개인정보 처리방침에 동의해주세요");
            $agree2.focus();
            return false;
        }
        location.href = '?contentId=a2774b40dfb15ea74a975301858011f29f9345c581ab9981c708e7958c600a93&command=join';
        return false;
    });
});
</script>
