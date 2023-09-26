<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String dm_domain_id = request.getParameter("dm_domain_id") != null ? request.getParameter("dm_domain_id") : "";
%>
<c:set var="dm_domain_id" value="<%=dm_domain_id%>"/>
<c:import url="/adm/page_header.do" />
<c:choose>
	<c:when test='${pageContext.request.scheme eq "https"}'>
		<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
	</c:when>
	<c:when test='${pageContext.request.scheme eq "http"}'>
		<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
	</c:when>
</c:choose>
<div class="easyui-layout">
	<div id="form_wrap">
		<form id="fm" name="fm" enctype="multipart/form-data" autocomplete="off">
			<input type="hidden" name="dm_id" id="dm_id" value="" />
			<input type="hidden" name="dm_domain_id" value="<c:out value='${dm_domain_id}'/>" />
			<input type="hidden" name="type" id='type' value="insert" />
			<div class="title">
				<h1>환경설정관리</h1>
		        <div class="btnWrap">
		        	<button type="button" id="fnSave">저장</button>
		        	<button type="button" id="fnCancel" class="bt09">취소</button>
		        </div>
			</div>
			<div class="page">
				<h3>환경설정정보</h3>
				<p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>      
				<dl>
				    <dt>도메인주소<span class="required_value">*</span></dt>
				    <dd>
				    	<input class="wd90" name="dm_url" id="dm_url" type="text" maxlength="50" onkeyup="setUrlPattern(this);">
				    	<button type="button" class="btn" id="chkBefore" onclick="fnDupDomainUrl(this);">중복검사</button>
				    	<button type="button" class="btn" id="chkAfter" onclick="refresh(this);" style="display: none;">변경</button>
				    	<p class="noty">http://, https://를 제외하며, 최대 50자까지 입력가능합니다.</p>
				    </dd>
				</dl>
				<dl>
				    <dt>상단타이틀<span class="required_value">*</span></dt>
				    <dd>
				    	<input name="dm_title" id="dm_title" type="text" maxlength="50">
				    	<p class="noty">1자 이상 50자 이하로 입력해주세요. 예)디자인아이엠</p>
				    </dd>
				</dl>
				<dl>
	                <dt>레이아웃 선택<span class="required_value">*</span></dt>
	                <dd>
	                    <select id="dm_theme" name="dm_theme" class="easyui-combobox" panelHeight="auto"
	                            data-options="url: '/adm/select_layout_id.do',
	                                                method: 'get',
	                                                valueField: 'dm_id',
	                                                textField: 'dm_layout_nm',
	                                                editable: false">
	                    </select>
	                    <p class="noty">도메인의 스킨을 설정합니다.</p>
	                </dd>
	            </dl>
				<h3>로고설정</h3>
				<dl>
				    <dt>상단로고</dt>
				    <dd class="dm_image" id="dm_top_logo">
				    	<input name="dm_top_logo_file"  type="file" accept=".jpg, .jpeg, .gif, .png">
				    	<p class="noty">.jpg, .jpeg, .gif, .png 확장자 파일만 업로드 가능합니다.</p>
				    </dd>
				</dl>
				<dl>
				    <dt>하단로고</dt>
				    <dd class="dm_image" id="dm_bottom_logo" >
				    	<input name="dm_bottom_logo_file" type="file" accept=".jpg, .jpeg, .gif, .png">
				    	<p class="noty">.jpg, .jpeg, .gif, .png 확장자 파일만 업로드 가능합니다.</p>
				    </dd>
				</dl>
				<h3>주요SEO 최적화 설정</h3>
				<dl>
					<dt>메타태그 설명</dt>
					<dd>
						<input name="dm_meta_desc" id="dm_meta_desc" type="text" maxlength="100" onkeyup="setMetaTagPattern(this);">
						<p class="noty">최대 100자 까지 입력가능합니다. ex)홈페이지 제작,상세페이지,디자인</p>
					</dd>
				</dl>
				<dl>
					<dt>메타태그 키워드</dt>
					<dd>
						<input name="dm_meta_keyword" id="dm_meta_keyword" type="text" maxlength="100" onkeyup="setMetaTagPattern(this);">
						<p class="noty">최대 100자 까지 입력가능합니다. ex)홈페이지제작,상세페이지,디자인</p>
					</dd>
				</dl>
				<dl>
					<dt>네이버 인증키</dt>
					<dd>
						<input name="dm_naver_site_verification" id="dm_naver_site_verification" type="text" class="wd70">
                		<a href="https://searchadvisor.naver.com/" target="_blank" class="btn">사이트추가</a>
                		<a href="https://blog.naver.com/naver_search/220577772692" target="_blank" class="btn bt07">소유인증방법</a>
                		<p class="noty">네이버 검색 노출을 위해 사이트 소유자 인증입니다.</p>
					</dd>
				</dl>
				<dl>
					<dt>대표이미지</dt>
					<dd class="dm_image" id="dm_personal_image">
						<input name="dm_personal_image_file" type="file" accept=".jpg, .jpeg, .gif, .png">
						<p class="noty">.jpg, .jpeg, .gif, .png 확장자 파일만 업로드 가능합니다.</p>
					</dd>
				</dl>
				<h3>회사 정보</h3>
				<dl>
					<dt>상호명</dt>
				    <dd>
				    	<input name="dm_company_name" id="dm_company_name" type="text" maxlength="50">
				    	<p class="noty">최대 50자 까지 입력 가능합니다.</p>
				    </dd>
				</dl>
				<div class="half">
				    <dl>
				        <dt>사업자등록번호</dt>
				        <dd>
				            <input type="text" name="dm_company_number1" id="dm_company_number1" value="" maxlength="3" class="wd20" onkeyup="setNumberPattern(this);"> -
				            <input type="text" name="dm_company_number2" id="dm_company_number2" value="" maxlength="2" class="wd15" onkeyup="setNumberPattern(this);"> -
				            <input type="text" name="dm_company_number3" id="dm_company_number3" value="" maxlength="5" class="wd20" onkeyup="setNumberPattern(this);">
				            <p class="noty">ex)OOO-OO-OOOOO</p>
				        </dd>
				    </dl>
				    <dl>
				        <dt>통신판매신고번호</dt>
				        <dd>
				            <input type="text" name="dm_tel_company_number" id="dm_tel_company_number" value="" class="wd70" maxlength="20" onkeyup="setCompanyTelNumberPattern(this);">
				            <p class="noty">ex)제OOOO-OOOO-OOOO호</p>
						</dd>
				    </dl>
				</div>
				<dl>
					<dt>대표자명</dt>
				    <dd>
				    	<input name="dm_ceo" id="dm_ceo" type="text" maxlength="10">
				    	<p class="noty">최대 10자 까지 입력 가능합니다.</p>
				    </dd>
				</dl>
				<dl>
					<dt>대표이메일</dt>
				    <dd>
					    <input name="dm_ceo_email1" id="dm_ceo_email1" type="text" class="wd30"> @ <input name="dm_ceo_email2" id="dm_ceo_email2" type="text" class="wd30">
					    <select id="dm_ceo_email3" name="dm_ceo_email3" class="wd30">
					        <option value="self">직접입력</option>
					        <option value="naver.com">naver.com</option>
					        <option value="hanmail.net">hanmail.net</option>
					        <option value="daum.net">daum.net</option>
					        <option value="nate.com">nate.com</option>
					        <option value="hotmail.com">hotmail.com</option>
					        <option value="gmail.com">gmail.com</option>
					        <option value="icloud.com">icloud.com</option>
					    </select>
					    <p class="noty">회사 대표 이메일입니다. ex) web@diam.kr</p>
				    </dd>
				</dl>
				<dl>
					<dt>사업장 주소</dt>
				    <dd class="address">
				        <label for="dm_zip" class="sound_only">우편번호</label>
				        <input type="text" name="dm_zip" value="" id="dm_zip" class="wd10" maxlength="5" placeholder="우편번호" readonly="readonly">
				        <button type="button" class="btn" onclick="win_zip('fm', 'dm_zip', 'dm_addr1', 'dm_addr2', 'dm_addr3', 'dm_addr_jibeon');">주소 검색</button><br>
				        <input type="text" name="dm_addr1" value="" id="dm_addr1" placeholder="기본주소" readonly="readonly">
				        <label for="dm_addr1">기본주소</label>
				        <input type="text" name="dm_addr2" value="" id="dm_addr2" placeholder="상세주소" maxlength="50">
				        <label for="dm_addr2">상세주소</label>
				        <br>
				        <input type="text" name="dm_addr3" value="" id="dm_addr3" placeholder="기타주소" maxlength="50">
				        <label for="dm_addr3">기타주소</label>
				        <input type="hidden" name="dm_addr_jibeon" value="" id="dm_addr_jibeon">
				        <p class="noty">대표 사업장 주소를 입력합니다.</p>
				    </dd>
				</dl>
				<div class="half">
					<dl>
						<dt>대표전화</dt>
					    <dd>
					    	<input name="dm_tel" id="dm_tel" type="text" maxlength="14" onkeyup="setTelPattern(this);">
					    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
					    </dd>
					</dl>
					<dl>
						<dt>팩스번호</dt>
					    <dd>
					    	<input name="dm_fax" id="dm_fax" type="text" maxlength="14" onkeyup="setTelPattern(this);">
					    	<p class="noty">(-)은 자동입력되며, 최대 14자 까지 입력 가능합니다.</p>
					    </dd>
					</dl>
				</div>
			</div>
		</form>
	</div>	
</div>
<script>
function refresh(ele) {
	$(ele).toggle();
	$("#chkBefore").toggle();
	$("#dm_url").attr("readonly", false);
}

function fnDupDomainUrl(ele) {
	var dm_url = $("#dm_url").val();
	var dm_id = $("#dm_id").val();
	if (dm_url == "") {
		$.messager.alert('경고', "도메인을 입력해주시기 바랍니다.", 'warning');
		$("#dm_url").focus();
	} else {
		$.ajax({
			url: "/adm/dup_domain_url.do",
			dataType: "json",
			type: "post",
			data:{dm_id : dm_id, dm_url : dm_url},
			success: function(response){
				if(response.result == "success"){
					$(ele).toggle();
					$("#chkAfter").toggle();
					$("#dm_url").attr("readonly", true);
					$.messager.alert('경고', response.notice, 'info');
				} else {
					$.messager.alert('경고', response.notice, 'warning');
					$("#dm_id").focus();
				}
			}, error : function(request, status, error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					$.messager.alert("error", request.responseJSON.notice, "warning");
					$("#dm_id").focus();
				}
			}
		});
	}
}

function selectedDg() {
	var dm_domain_id = '<c:out value="${dm_domain_id}"/>';
	if (dm_domain_id != '') {
		$.ajax({
			url : "/adm/get_env.do",
			type : "POST",
			cache : false,
			async : true,
			dataType : "json",
			data: "dm_domain_id="+'<c:out value="${dm_domain_id}"/>',
			success : function (data) {
				if(data.result == "success") {
					if (data.rows != null && data.rows != "") {
						var dm_company_number = "";
						var dm_email = "";
						var dm_customer_email = "";
						var dm_customer_daily_start_time = "";
						var dm_customer_daily_end_time = "";
						var dm_customer_weekend_start_time = "";
						var dm_customer_weekend_end_time = "";
						var dm_customer_break_start_time = "";
						var dm_customer_break_end_time = "";
	                 		
						if (data.rows.dm_company_number) {
						    dm_company_number = data.rows.dm_company_number.split("-");
						}
						
						if (data.rows.dm_ceo_email) {
						    dm_email = data.rows.dm_ceo_email.split("@");
						}
						if (data.rows.dm_customer_email) {
							dm_customer_email = data.rows.dm_customer_email.split("@");
						}
						
						if (data.rows.dm_top_logo) {
						    $("#dm_top_logo").prepend('<div><img src="'+data.rows.dm_top_logo+'" alt="'+data.rows.dm_top_logo_name+'"/><label for="dm_del_top_logo"><input type="checkbox" name="dm_del_top_logo" value="1" />삭제</label><input type="hidden" name="dm_top_logo" value="'+data.rows.dm_top_logo+'" /><input type="hidden" name="dm_top_logo_name" value="'+data.rows.dm_top_logo_name+'" /></div>');
						}

						if (data.rows.dm_bottom_logo) {
						    $("#dm_bottom_logo").prepend('<div><img src="'+data.rows.dm_bottom_logo+'" alt="'+data.rows.dm_bottom_logo_name+'"/><label for="dm_del_bottom_logo"><input type="checkbox" name="dm_del_bottom_logo" value="1" />삭제</label><input type="hidden" name="dm_bottom_logo" value="'+data.rows.dm_bottom_logo+'" /><input type="hidden" name="dm_bottom_logo_name" value="'+data.rows.dm_bottom_logo_name+'" /></div>');
						}
						
						if (data.rows.dm_personal_image) {
							$("#dm_personal_image").prepend('<div><img src="'+data.rows.dm_personal_image+'" alt="'+data.rows.dm_personal_image_original_name+'"/><label for="dm_del_personal_image"><input type="checkbox" name="dm_del_personal_image" value="1" />삭제</label><input type="hidden" name="dm_personal_image" value="'+data.rows.dm_personal_image+'" /><input type="hidden" name="dm_personal_image_original_name" value="'+data.rows.dm_personal_image_original_name+'" /></div>');
						}

						$("#dm_id").val(data.rows.dm_id);
						$("#type").val('update');
						$("#dm_url").val(data.rows.dm_url);
						$("#dm_url").attr("readonly", true);
						$("#chkBefore").toggle();
						$("#chkAfter").toggle();
						$("#dm_title").val(unescapeHtml(data.rows.dm_title));
						$("#dm_company_name").val(unescapeHtml(data.rows.dm_company_name));
						$("#dm_company_number1").val(dm_company_number[0]);
						$("#dm_company_number2").val(dm_company_number[1]);
						$("#dm_company_number3").val(dm_company_number[2]);
						$("#dm_tel_company_number").val(data.rows.dm_tel_company_number);
						$("#dm_ceo").val(data.rows.dm_ceo);
						$("#dm_ceo_email1").val(dm_email[0]);
						$("#dm_ceo_email2").val(dm_email[1]);
						$("#dm_zip").val(data.rows.dm_zip);
						$("#dm_addr1").val(data.rows.dm_addr1);
						$("#dm_addr2").val(unescapeHtml(data.rows.dm_addr2));
						$("#dm_addr3").val(unescapeHtml(data.rows.dm_addr3));
						$("#dm_addr_jibeon").val(data.rows.dm_addr_jibeon);
						$("#dm_tel").val(data.rows.dm_tel);
						$("#dm_fax").val(data.rows.dm_fax);
						$("#dm_meta_desc").val(unescapeHtml(data.rows.dm_meta_desc));
						$("#dm_meta_keyword").val(unescapeHtml(data.rows.dm_meta_keyword));
						$("#dm_naver_site_verification").val(unescapeHtml(data.rows.dm_naver_site_verification));
						
						$("#dm_theme").combobox({
							onLoadSuccess: function(){
								$("#dm_theme").combobox("setValue", data.rows.dm_theme);
							}
						});
					}
				} else {
					window.opener.$.messager.alert('경고', data.notice, 'warning');
				    self.close();
				}
			}, error:function(request,status,error) {
				if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
					window.opener.$.messager.alert('경고',request.responseJSON.notice,'warning');
					self.close();
				}
			}
		});		
	}
}

function fnSave() {
	if ($.trim($("#dm_title").val()) == "") {
		$.messager.alert("경고","상단타이틀을 입력해주세요.","warning");
		$("#dm_title").focus();
		return;
	}
	
	if ($("#dm_url").attr("readonly") != "readonly") {
		$.messager.alert("경고","도메인주소 중복 검사 후 진행 가능합니다.","warning");
		$("#dm_url").focus();
		return;
	}
	
    $(".btnWrap").hide();
	
    var form = $("#fm")[0];
    var formData = new FormData(form);

    $.ajax({
        url : "/adm/set_config_env.do",
        data : formData,
        dataType: "json",
        type : "post",
        contentType: false,
        processData: false,
        success : function (data) {
            var icon = 'warning';
            if(data.result == "success") {
            	window.opener.$.messager.alert('알림', data.notice, 'info');
            	window.opener.$("#dg").datagrid('reload');
                self.close();
            } else {
            	$.messager.alert('경고', data.notice, 'warning');
            	$(".btnWrap").show();
            }                   
        }, error:function(request,status,error) {
         	if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseJSON.notice, "warning");
	        	$(".btnWrap").show();
			}
        }
    });
}

$(function () {
    $("#fnSave").off().on('click', function () {
    	if(confirm("정말로 저장하시겠습니까?")){
        	fnSave();
    	}
    });

    $("#fnCancel").off().on('click', function(){
		if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
			self.close();
		}
	});
        
    $(document).on("keyup", "#dm_tel", function() { 
    	$(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") ); 
    });
    
    $(document).on("keyup", "#dm_fax", function() { 
    	$(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") ); 
    });
    
    $("#dm_ceo_email3").off().on('change', function(){
    	var selector = $(this).val();
        if (selector != "self") {
            $("#dm_ceo_email2").val(selector);
            $("#dm_ceo_email2").attr('readonly', true);
        } else {
        	$("#dm_ceo_email2").val("");
            $("#dm_ceo_email2").attr('readonly', false);
        }
    });
    
    selectedDg();
});
</script>
<c:import url="/adm/page_bottom.do" />