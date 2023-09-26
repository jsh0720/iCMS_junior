<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
	<c:when test="${fn:contains(CONFIG_INFO.dm_url, '/') }">
		<c:set value="/${fn:split(CONFIG_INFO.dm_url, '/')[1]}" var="root"/>	
	</c:when>
	<c:otherwise>
		<c:set value="" var="root"/>
	</c:otherwise>
</c:choose>
<div class="unified-search-result">
	<div class="search-result">
		<h3>검색어 <strong>"<c:out value="${search_value }"/>"</strong> 에 대한 검색 결과입니다.</h3>
	</div>

	<div class="search-result-body">
		<c:forEach items="${writeList }" var="data">
			<c:if test="${data.total > 0 }">
			<div class="search-result-wrap">
				<h4><c:out value="${data.dm_subject }"/> <small>(총 <c:out value="${data.total }"/>건)</small></h4>
				<table class="table">
					<caption><c:out value="${data.dm_subject }"/> 검색 결과 테이블</caption>
					<thead>
						<tr>
							<!-- <th>구분</th> -->
							<th class="td_subject">제목</th>
							<th>작성자</th>
							<th>내용</th>
							<th>작성일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${data.write }" var="item">
							<c:choose>
								<c:when test="${item.wr_is_comment eq 1 }">
									<c:set var="comment" value="댓글"/>
								</c:when>
								<c:otherwise>
									<c:set var="comment" value="게시글"/>
								</c:otherwise>
							</c:choose>
							<tr>
								<%-- <td class="td_type">${comment}</td> --%>
								<td class="td_subject"><a href="<c:out value='${root }'/>/index.do?command=view&contentId=<c:out value='${data.dm_uid }'/>&wr_id=<c:out value='${item.wr_id}'/>"><c:out value="${item.wr_subject }" escapeXml="false"/></a></td>
								<td class="td_content"><c:out value="${item.wr_content }" escapeXml="false"/></td>
								<td class="td_name">
									<c:choose>
										<c:when test='${data.dm_writer_type eq "name"}'>
											 <c:choose>
												<c:when test="${data.dm_writer_secret eq '2' }">
													<c:out value="${fn:substring(item.wr_name, 0, fn:length(item.wr_name) - 1)}"/>*
												</c:when>
												<c:when test="${data.dm_writer_secret eq '3' }">
													<c:out value="${fn:substring(item.wr_name, 0, 1)}"/><c:forEach begin="0" end="${fn:length(item.wr_name) - 2}">*</c:forEach>
												</c:when>
												<c:when test="${data.dm_writer_secret eq '4' }">
													<c:out value="${fn:substring(item.wr_name, 0 , fn:length(item.wr_name) - 2)}"/>**
												</c:when>
												<c:otherwise>
													<c:out value="${item.wr_name}"/>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${data.dm_writer_secret eq '2' }">
													<c:out value="${fn:substring(item.mb_id, 0, fn:length(item.mb_id) - 1)}"/>*
												</c:when>
												<c:when test="${data.dm_writer_secret eq '3' }">
													<c:out value="${fn:substring(item.mb_id, 0, 1)}"/><c:forEach begin="0" end="${fn:length(item.mb_id) - 2}">*</c:forEach>
												</c:when>
												<c:when test="${data.dm_writer_secret eq '4' }">
													<c:out value="${fn:substring(item.mb_id, 0 , fn:length(item.mb_id) - 2)}"/>**
												</c:when>
												<c:otherwise>
													<c:out value="${item.mb_id}"/>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</td>
								<td class="td_date"><c:out value="${item.wr_datetime }"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			</c:if>
		</c:forEach>
	</div>
</div>