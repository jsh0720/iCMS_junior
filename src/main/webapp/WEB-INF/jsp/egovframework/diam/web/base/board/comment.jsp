<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rsa.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/jsbn.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/prng4.js"></script>
<script type="text/javascript" src="/js/egovframework/diam/rsa/rng.js"></script>
<script>
function updateComment(wr_id, nonMemberAt) {
	if (nonMemberAt == true) {
		<c:choose>
			<c:when test="${is_admin eq true}">
				executeCommentModify(wr_id, "<c:out value='${pageVO.dm_board_id}'/>");
			</c:when>
			<c:otherwise>
				passwordPopup("<c:out value='${param.root}'/>",wr_id, "<c:out value='${pageVO.dm_uid}'/>", "<c:out value='${pageVO.dm_board_id}'/>", "<c:out value='${writeSearchQueryString}'/>", "comment_modify");
			</c:otherwise>
		</c:choose>	
	} else {
		executeCommentModify(wr_id, "<c:out value='${pageVO.dm_board_id}'/>");			
	}	
}

function deleteComment(wr_id, nonMemberAt){
	if (nonMemberAt == true) {
		<c:choose>
			<c:when test="${is_admin eq true}">
				if (confirm("정말 삭제하시겠습니까?")) {
					executeDelete("<c:out value='${param.root}'/>",wr_id, "<c:out value='${pageVO.dm_board_id}'/>", "<c:out value='${writeSearchQueryString}'/>");
				}
			</c:when>
			<c:otherwise>
				passwordPopup("<c:out value='${param.root}'/>",wr_id, "<c:out value='${pageVO.dm_uid }'/>", "<c:out value='${pageVO.dm_board_id}'/>", "<c:out value='${writeSearchQueryString}'/>", "delete");
			</c:otherwise>
		</c:choose>
	} else {
		if (confirm("정말 삭제하시겠습니까?")) {
			executeDelete("<c:out value='${param.root}'/>",wr_id, "<c:out value='${pageVO.dm_board_id}'/>", "<c:out value='${writeSearchQueryString}'/>");
		}
	}
} 

function writeComment() {
	if (confirm("댓글을 등록하시겠습니까?")) {
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
	    
	    var form_data = $("#comment_fm").serialize();
	    $("#wr_password").val("");
	    $.ajax({
	        url : '/write/set_comment.do',
	        dataType : 'json',
	        type : 'post',
	        async : false,
	        data : form_data,
	        success : function (data) {
	            alert(data.notice);
	            if (data.result == 'success') {
	            	location.reload();                
	            }
	        }, error:function(request, status, error) {
	            alert(request.responseJSON.notice);
	        }
	    });
	}	
}
function deniedDelete() {
	alert("댓글이 있는 댓글은 삭제할 수 없습니다.");
	return;
}

function comment_reply(wr_id) {
    $("#comment_form").remove();
    $("#comment_fm").remove();
    $('.comment_form').removeClass('comment_form');
   
    $(".comment_reply_fm"+wr_id).addClass("comment_form").html(comment_fm);
    $(".comment_reply_fm"+wr_id+" h4").text("댓글의 댓글 쓰기");
    $(".comment_form input[name='mode']").val('1');
    $('#comment_content').val(''); // IE TEXTAREA Placeholder Patch
    $("#wr_id").val(wr_id);
}
</script>
<script>
jQuery(function($) {
    //댓글열기
    $(".cmt_btn").click(function(e){
        e.preventDefault();
        $(this).toggleClass("cmt_btn_op");
        $(".bbs_comment").toggle();
    });
});
</script>
<button type="button" class="cmt_btn"><i class="material-icons" aria-hidden="true">comment</i> 댓글</button>
<div class="bbs_comment">
	<h3>댓글 목록</h3>
	<ul class="comment_ul">
		<c:choose>
			<c:when test="${not empty commentList}">
				<c:forEach var="comment" items="${commentList}" varStatus="status">
					<li class="comment_li" >
						<div class="comment_box">
							<h4>${comment.wr_name}님의 댓글</h4>
							<div class="comment_header">
								<dl class="comment_wr_name" id="wr_name_<c:out value='${comment.wr_id}'/>">
									<dt>작성자</dt>
									<dd>
										<c:choose>
											<c:when test="${boardVO.dm_writer_type eq 'name'}">
												<c:choose>
													<c:when test="${boardVO.dm_writer_secret eq '2' }">
														<c:out value="${fn:substring(comment.wr_name, 0, fn:length(comment.wr_name) - 1)}"/>*
													</c:when>
													<c:when test="${boardVO.dm_writer_secret eq '3' }">
														<c:out value="${fn:substring(comment.wr_name, 0, 1)}"/><c:forEach begin="0" end="${fn:length(comment.wr_name) - 2}">*</c:forEach>
													</c:when>
													<c:when test="${boardVO.dm_writer_secret eq '4' }">
														<c:out value="${fn:substring(comment.wr_name, 0 , fn:length(comment.wr_name) - 2)}"/>**
													</c:when>
													<c:otherwise>
														<c:out value="${comment.wr_name}"/>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${boardVO.dm_writer_secret eq '2' }">
														<c:out value="${fn:substring(comment.mb_id, 0, fn:length(comment.mb_id) - 1)}"/>*
													</c:when>
													<c:when test="${boardVO.dm_writer_secret eq '3' }">
														<c:out value="${fn:substring(comment.mb_id, 0, 1)}"/><c:forEach begin="0" end="${fn:length(comment.mb_id) - 2}">*</c:forEach>
													</c:when>
													<c:when test="${boardVO.dm_writer_secret eq '4' }">
														<c:out value="${fn:substring(comment.mb_id, 0 , fn:length(comment.mb_id) - 2)}"/>**
													</c:when>
													<c:otherwise>
														<c:out value="${comment.mb_id}"/>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</dd>
								</dl>
								<dl class="comment_wr_date">
									<dt>작성일</dt>
									<dd><c:out value='${comment.wr_datetime}'/></dd>
								</dl>
							</div>
							<div class="comment_content" id="content_<c:out value='${comment.wr_id}'/>">
								<p>
									<c:choose>
						    			<c:when test="${comment.wr_option eq 'secret'}">
						    				<c:choose>
						    					<c:when test="${is_admin eq true || DiamLoginVO.id eq comment.mb_id || DiamLoginVO.id eq comment.ori_mb_id || (DiamLoginVO.id ne null && DiamLoginVO.id eq comment.reply_mb_id)}">
						    						<c:out value="${fn:replace(comment.wr_content, newLineChar , '<br/>')}" escapeXml="false"/>
						    					</c:when>
						    					<c:otherwise>
						    						비밀글입니다
						    					</c:otherwise>
						    				</c:choose>
						    			</c:when>
						    			<c:otherwise>
						    				<c:out value="${fn:replace(comment.wr_content, newLineChar, '<br/>')}" escapeXml="false"/>
						    			</c:otherwise>
						    		</c:choose>
								</p>
							</div>
							
							<div class="comment_footer">
								<div class="comment_button">
									<c:if test="${is_comment eq true}">
										<c:if test='${comment.wr_comment < 2}'>
							    			<a class="btn_write" href="javascript:comment_reply('<c:out value="${comment.wr_id}"/>');">댓글쓰기</a>
							    		</c:if>
							    	</c:if>
							    	<c:choose>
							    		<c:when test="${comment.mb_id eq '비회원'}">
							    			<c:if test="${is_admin || DiamLoginVO.id eq null}">
							    				<a class="btn_modify" href="javascript:updateComment('<c:out value="${comment.wr_id}"/>', true);">댓글수정</a>
							    				<c:choose>
							    					<c:when test="${empty comment.children }">
											    		<a class="btn_delete" href="javascript:deleteComment('<c:out value="${comment.wr_id}"/>', true);">댓글삭제</a>
							    					</c:when>
							    					<c:otherwise>
							    						<a class="btn_delete" href="javascript:deniedDelete();">댓글 삭제</a>
							    					</c:otherwise>
							    				</c:choose>
							    			</c:if>
							    		</c:when>
							    		<c:otherwise>
							    			<c:if test="${is_admin eq true || comment.mb_id eq DiamLoginVO.id}">
							    				<a class="btn_modify" href="javascript:updateComment('<c:out value="${comment.wr_id}"/>', false);">댓글수정</a>
									    		<c:choose>
							    					<c:when test="${empty comment.children }">
											    		<a class="btn_delete" href="javascript:deleteComment('<c:out value="${comment.wr_id}"/>', true);">댓글삭제</a>
							    					</c:when>
							    					<c:otherwise>
							    						<a class="btn_delete" href="javascript:deniedDelete();">댓글 삭제</a>
							    					</c:otherwise>
							    				</c:choose>
							    			</c:if>
							    		</c:otherwise>
							    	</c:choose>	
								</div>
							</div>
							<div class="comment_reply_fm<c:out value='${comment.wr_id}'></c:out>"></div>
						</div>
					</li>
					<c:if test="${!empty comment.children }">
						<c:forEach items="${comment.children }" var="item">
						<li class="comment_li reply" >
							<div class="comment_box">
								<h4>${item.wr_name}님의 댓글</h4>
								<div class="comment_header">
									<dl class="comment_wr_name" id="wr_name_<c:out value='${item.wr_id}'/>">
										<dt>작성자</dt>
										<dd>
											<c:choose>
												<c:when test="${boardVO.dm_writer_type eq 'name'}">
													<c:choose>
														<c:when test="${boardVO.dm_writer_secret eq '2' }">
															<c:out value="${fn:substring(item.wr_name, 0, fn:length(item.wr_name) - 1)}"/>*
														</c:when>
														<c:when test="${boardVO.dm_writer_secret eq '3' }">
															<c:out value="${fn:substring(item.wr_name, 0, 1)}"/><c:forEach begin="0" end="${fn:length(item.wr_name) - 2}">*</c:forEach>
														</c:when>
														<c:when test="${boardVO.dm_writer_secret eq '4' }">
															<c:out value="${fn:substring(item.wr_name, 0 , fn:length(item.wr_name) - 2)}"/>**
														</c:when>
														<c:otherwise>
															<c:out value="${item.wr_name}"/>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${boardVO.dm_writer_secret eq '2' }">
															<c:out value="${fn:substring(item.mb_id, 0, fn:length(item.mb_id) - 1)}"/>*
														</c:when>
														<c:when test="${boardVO.dm_writer_secret eq '3' }">
															<c:out value="${fn:substring(item.mb_id, 0, 1)}"/><c:forEach begin="0" end="${fn:length(item.mb_id) - 2}">*</c:forEach>
														</c:when>
														<c:when test="${boardVO.dm_writer_secret eq '4' }">
															<c:out value="${fn:substring(item.mb_id, 0 , fn:length(item.mb_id) - 2)}"/>**
														</c:when>
														<c:otherwise>
															<c:out value="${item.mb_id}"/>
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</dd>
									</dl>
									<dl class="comment_wr_date">
										<dt>작성일</dt>
										<dd><c:out value='${item.wr_datetime}'/></dd>
									</dl>
								</div>
								<div class="comment_content" id="content_<c:out value='${item.wr_id}'/>">
									<p>
										<c:choose>
							    			<c:when test="${item.wr_option eq 'secret'}">
							    				<c:choose>
							    					<c:when test="${is_admin eq true || DiamLoginVO.id eq item.mb_id || DiamLoginVO.id eq item.ori_mb_id || (DiamLoginVO.id ne null && DiamLoginVO.id eq item.reply_mb_id)}">
							    						<c:out value="${fn:replace(item.wr_content, newLineChar , '<br/>')}" escapeXml="false"/>
							    					</c:when>
							    					<c:otherwise>
							    						비밀글입니다
							    					</c:otherwise>
							    				</c:choose>
							    			</c:when>
							    			<c:otherwise>
							    				<c:out value="${fn:replace(item.wr_content, newLineChar, '<br/>')}" escapeXml="false"/>
							    			</c:otherwise>
							    		</c:choose>
									</p>
								</div>
								
								<div class="comment_footer">
									<div class="comment_button">
										<c:if test="${is_comment eq true}">
											<c:if test='${item.wr_comment < 2}'>
								    			<a class="btn_write" href="javascript:comment_reply('<c:out value="${item.wr_id}"/>');">댓글쓰기</a>
								    		</c:if>
								    	</c:if>
								    	<c:choose>
								    		<c:when test="${item.mb_id eq '비회원'}">
								    			<c:if test="${is_admin || DiamLoginVO.id eq null}">
								    				<a class="btn_modify" href="javascript:updateComment('<c:out value="${item.wr_id}"/>', true);">댓글수정</a>
										    		<a class="btn_delete" href="javascript:deleteComment('<c:out value="${item.wr_id}"/>', true);">댓글삭제</a>
								    			</c:if>
								    		</c:when>
								    		<c:otherwise>
								    			<c:if test="${is_admin eq true || item.mb_id eq DiamLoginVO.id}">
								    				<a class="btn_modify" href="javascript:updateComment('<c:out value="${item.wr_id}"/>', false);">댓글수정</a>
										    		<a class="btn_delete" href="javascript:deleteComment('<c:out value="${item.wr_id}"/>', false);">댓글삭제</a>		
								    			</c:if>
								    		</c:otherwise>
								    	</c:choose>	
									</div>
								</div>
								<div class="comment_reply_fm<c:out value='${item.wr_id}'></c:out>"></div>
							</div>
						</li>
						</c:forEach>
					</c:if>
				</c:forEach>
			</c:when>		
		</c:choose>
	</ul>
</div>

<c:if test="${is_comment eq true}">
	<div class="comment_form" id="comment_form">
		<h4>댓글쓰기</h4>
		<form id="comment_fm" onsubmit="return false" autocomplete="off">
			<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
			<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
			<input type="hidden" name="type" id="type" value="insert"/>
			<input type="hidden" name="mode" value="" />
			<input type="hidden" name="wr_id" id="wr_id" value="<c:out value='${writeVO.wr_id}'/>"/>
			<input type="hidden" name="dm_id" value="<c:out value='${boardVO.dm_id}'/>"/>
			<input type="hidden" name="wr_subject" value="사용자페이지 댓글"/>
			
			<div class="comment_write comment_write_info">
				<c:choose>
					<c:when test="${DiamLoginVO.id eq null }">
						<dl class="cmf_name">
							<dt><label for="wr_name">작성자</label></dt>
							<dd><input type="text" name="wr_name" id="wr_name" placeholder="작성자명"/></dd>
						</dl>
						<dl class="cmf_pass">
							<dt><label for="wr_password">비밀번호</label></dt>
							<dd><input type="password" name="wr_password" id="wr_password" autocomplete="new-password" placeholder="비밀번호"/></dd>
						</dl>
					</c:when>
					<c:otherwise>
						<dl class="cmf_name">
							<dt><label for="">작성자명</label></dt>
							<dd><c:out value='${DiamLoginVO.name}'></c:out></dd>
							<dd><input type="hidden" name="wr_name" id="wr_name" value="<c:out value='${DiamLoginVO.name}'></c:out>"></dd>
							
						</dl>
					</c:otherwise>
				</c:choose>
				<c:if test="${boardVO.dm_use_comment_secret eq 1}">
					<c:if test="${writeVO.mb_id ne '비회원'}">
						<dl class="cmf_secret">
							<dt><label for="wr_option">비밀댓글</label></dt>
							<dd>
								<input type="checkbox" id="comment_secret" name="wr_option" value="secret"/>
								<label for="comment_secret">비밀댓글</label>
							</dd>
						</dl>
					</c:if>
				</c:if>
			</div>
			<div class="comment_write comment_write_form">
				<dl>
					<dt><label for="wr_content" class="">내용</label></dt>
					<dd>
						<textarea name="wr_content" id="wr_content"></textarea>
					</dd>
				</dl>
			</div>
			<div class="comment_write_btn">
				<button type="button" class="btn_comment" onclick="writeComment();">댓글작성</button>
			</div>
		</form>
	</div>
</c:if>

<script>
	var comment_fm = $("#comment_form").html();
	var password_input = $(".comment_write_info").html();
</script>

