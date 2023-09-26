<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="base.jsp"%>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
var oEditors = [];
$(function () {
	<c:if test="${boardVO.dm_use_dhtml_editor eq 1}">
	nhn.husky.EZCreator.createInIFrame({ 
		oAppRef : oEditors, 
		elPlaceHolder : "wr_content", //저는 textarea의 id와 똑같이 적어줬습니다.
		sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
		fCreator : "createSEditor2",
		htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
			bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
			bUseModeChanger : true 
		}
	});
	</c:if>
	
	$('.filebox .upload-hidden').on('change', function(){// 값이 변경되면
		if(window.FileReader){ // modern browser
		    var filename = $(this)[0].files[0].name;
		} else { // old IE
		    var filename = $(this).val().split('/').pop().split('\\').pop(); // 파일명만 추출
		}

		// 추출한 파일명 삽입
		$(this).siblings('.upload-name').val(filename);
	});		
	
	$("#btn_write").on('click', function(){
		if (confirm("저장하시겠습니까?")) {
			$("#writeVO").submit();
			$("#wr_password").val("");			
		}
	});
});

function checkForm() {
	<c:if test="${boardVO.dm_use_dhtml_editor eq 1}">
	oEditors.getById["wr_content"].exec("UPDATE_CONTENTS_FIELD", []); //textarea의 id를 적어줍니다.
	var cont = $("#wr_content").val();
	if (cont == "<p>&nbsp;</p>") {
		alert("내용을 입력해주세요.");
		return false;
	}
	</c:if>
	
	<c:if test="${DiamLoginVO.id eq null}">
		var wr_password = $("#wr_password").val();
    	if (wr_password != "" && wr_password != null) {
    		var rsa = new RSAKey();
    		rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
    		$("#wr_password").val(rsa.encrypt(wr_password));
    	} else {
    		alert("비회원으로 등록 시에는 비밀번호를 입력해주세요.");
    		return false;
    	}
	</c:if>
}
</script>
<p aria-hidden="true" class="mb15"><span class="required">*</span>표시된 입력값은 필수입력값입니다.</p>
<div class="bbs bbs_post bbs_<c:out value='${boardVO.dm_skin }'/>" id="bbs_<c:out value='${boardVO.dm_table }'/>">
	<form action="<c:out value='${param.root }'/>/write/set_reply.do" name="writeVO" id="writeVO" method="post" enctype="multipart/form-data" onsubmit="return checkForm();">
		<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
		<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
		<input type="hidden" name="dm_table" value="<c:out value='${boardVO.dm_table}'/>"/>
		<input type="hidden" name="wr_id" value="${writeVO.wr_id}"/>
		<input type="hidden" name="wr_board" id="wr_board" value="${boardVO.dm_id}"/>
		<input type="hidden" name="dm_id" id="dm_id" value="${boardVO.dm_id}"/>
		<input type="hidden" name="writeQueryString" value="<c:out value='${writeSearchQueryString}' escapeXml='false'/>"/>
		<table class="table-form">
			<caption><c:out value='${boardVO.dm_subject}'/>게시글 답변등록 테이블 입니다.</caption>
			<colgroup>
	            <col style="width:15%" />
	            <col />
	        </colgroup>
	        <tbody>
	        	<c:if test="${is_admin || boardVO.dm_use_secret eq '1'}">
	        		<tr>
	        			<th>옵션</th>
	        			<td>
		        			<div class="option">
								<c:if test='${boardVO.dm_use_secret eq "1"}'>
									<div class="custom-control custom-checkbox mr-3">
										<input type="checkbox" name="wr_option" value="secret" id="secret" class="custom-control-input">
										<label class="custom-control-label" for="secret"><em></em>비밀글</label>
				                	</div>	                
					            </c:if>	
		        			</div>
						</td>
	        		</tr>
	        	</c:if>
				<tr>
					<th><label for="wr_subject">제목 <span class="required">필수</span></label></th>
					<td><input type="text" name="wr_subject" id="wr_subject" class="form-control"/></td>
				</tr>
				
				<c:choose>
					<c:when test="${DiamLoginVO.id eq null}">
						<tr>
							<th><label for="wr_name">작성자<span class="required">*</span></label></th>
							<td>
								<input type="text" name="wr_name" id="wr_name" class="form-control"/>
							</td>
						</tr>
					</c:when>
					<c:otherwise>					
						<input type="hidden" name="wr_name" id="wr_name" value="${DiamLoginVO.name}"/>
					</c:otherwise>
				</c:choose>
				
				<c:if test='${DiamLoginVO.id eq null}'>
					<tr class="write_password">
						<th><label for="wr_password">비밀번호<span class="required">*</span></label></th>
						<td>
							<input type="password" name="wr_password" id="wr_password" class="form-control" autocomplete="new-password"/>
						</td>
					</tr>
				</c:if>
				
				<tr>
					<th><label for="wr_content">내용<span class="required">*</span></label></th>
					<td>
						<c:choose>
							<c:when test="${boardVO.dm_use_dhtml_editor eq 1}">
								<textarea id="wr_content" class="form-control" name="wr_content" placeholder="내용" style="width:100%"><c:out value="${dm_basic_content_editor}"/></textarea>
							</c:when>
							<c:otherwise>
								<textarea id="wr_content" class="form-control" name="wr_content" rows="5" placeholder="내용" style="width:100%"><c:out value="${dm_basic_content_normal}" escapeXml="false"/></textarea>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				
				<c:if test="${boardVO.dm_use_link eq 1 && is_link eq true}">
					<c:forEach var="result" begin="0" end="1" varStatus="status">
						<tr>
							<th>
								<label for="wr_link<c:out value='${status.index + 1}'/>">
									<i class="fa fa-link fa-fw" aria-hidden="true"></i> 링크  #<c:out value='${status.index + 1}'/>
								</label>
							</th>
							<td>
								<input type="text" name="wr_link<c:out value='${status.index + 1}'/>" id="wr_link<c:out value='${status.index + 1}'/>" class="form-control" size="50">
							</td>
						</tr>
					</c:forEach>
				</c:if>
				
				<c:if test="${boardVO.dm_use_file eq 1 && is_file eq true}">
					<c:if test='${boardVO.dm_upload_count > 0 }'>
						<c:forEach var="result" begin="0" end="${boardVO.dm_upload_count - 1}" varStatus="status">
							<tr>
								<th>첨부파일<c:out value='${status.index + 1}'/></th>
								<td>
									<div class="filebox">
										<input class="upload-name" value="파일선택" disabled="disabled"/>
										<label for="file${status.index}">업로드</label>
										<input type="file" name="file" id="file${status.index}" class="upload-hidden"/>
									</div>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>
	        </tbody>
		</table>
		<div class="bbs_captcha">
		
		</div>
		<div class="bbs_postbtn">
			<a href="<c:out value='${param.root }'/>/index.do?<c:out value='${writeSearchQueryString}'/>" class="btn_cancel">취소</a>
			<button type="button" id="btn_write" class="btn_submit">전송</button>
		</div>
	</form>
</div>