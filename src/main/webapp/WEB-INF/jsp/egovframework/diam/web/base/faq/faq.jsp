<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>


<div>
	<ul class="bd_faq">
		<c:choose>
			<c:when test="${fn:length(faqList) > 0}">
				<c:forEach var="result" items="${faqList}" varStatus="status">
					<li class="accWrap">
						<h5 class="qs_tit">
							<a href="javascript:void(0);"> <strong><c:out value='${result.dm_question}' escapeXml="false"/></strong> <span>열기</span></a>
						</h5>
						<div class="answer">
							<p>
								<c:out value="${fn:replace(result.dm_answer, newLineChar, '<br/>')}" escapeXml="false"/>
							</p>
						</div>
					</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li class="accWrap">
					<h5>
						<strong>등록된 FAQ가 없습니다.</strong>
					</h5>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<script>
$(document).ready(function(){
    // 자주 묻는 질문
	$('.accWrap > h5 > a').on('click', function() {		
		if($(this).parent().parent().hasClass('trigger')){
			$(this).parent().parent().removeClass('trigger')
			$(this).children('span').text('열기')
			$(this).parent().next().slideUp(200);			
		}
		else{
			$(this).parent().parent().addClass('trigger')
			$(this).children('span').text('닫기');
			$(this).parent().next().slideDown(200);			
		}
	});	
});
</script>
