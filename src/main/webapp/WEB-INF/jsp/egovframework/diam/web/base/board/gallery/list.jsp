<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%
	Date today = new Date();
	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String curDate = transFormat.format(today);
%>
<%@ include file="base.jsp"%>
<c:set var="page" value="${param.page eq null ? 1 : param.page}"/>
<c:set var="curDate" value="<%=curDate%>"/>
<c:set var="ratio" value="${boardVO.dm_gallery_height / boardVO.dm_gallery_width * 100}"/>

<div class="board-decorate">
	<c:if test="${boardVO.dm_header_content ne null && boardVO.dm_header_content ne ''}">
		<c:out value="${boardVO.dm_header_content}" escapeXml="false"/>
	</c:if>
</div>

<div class="bbs bbs_list bbs_<c:out value='${boardVO.dm_skin }'/>" id="bbs_<c:out value='${boardVO.dm_table }'/>">
	
	<c:if test="${boardVO.dm_use_list_category eq '1' && boardVO.dm_use_category eq '1'}">
		<c:set var="category_array" value="${fn:split(boardVO.dm_category_list ,',') }"/>
		<c:if test="${fn:length(category_array) > 0}">
			<div class="bbs_cate">
				<ul>
					<li class="<c:if test='${empty searchDmWriteVO.search_cate}'>on</c:if>">
						<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${pageVO.dm_uid}'/>">전체</a>
					</li>
					<c:forEach var="result" items="${category_array}" varStatus="status">
						<c:if test="${result ne null && not empty fn:trim(result)}">
							<li class="<c:if test='${searchDmWriteVO.search_cate eq result}'>on</c:if>">
								<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${pageVO.dm_uid}'/>&search_cate=<c:out value='${result}'/>">
									<c:out value='${result}'/>
								</a>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>
	</c:if>
	<div class="bbs_leadin">
		<div class="bbs_count">
			<span>전체 <strong><c:out value='${writeListCnt}'/></strong> 건</span><span>(<c:out value='${page}'/>/<c:out value='${total_page}'/>page)</span>
		</div>
		<div class="bbs_order"></div>
	</div>
	<c:if test="${!empty noticeList }">
		<div class="bbs_listing">
			<table class="table">
				<caption><c:out value='${boardVO.dm_subject }'/></caption>
				<thead>
					<tr>
						<th class="td_num">번호</th>
						<th class="td_subject" style="text-align: center !important;">제목</th>
						<th class="td_name">작성자</th>
						<th class="td_date">작성일</th>
						<th class="td_hit">조회수</th>
						<c:if test="${boardVO.dm_is_comment eq '1'}">
							<th class="td_ico">댓글개수</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="notice" items="${noticeList }">
						<tr>
							<td><b class="is_noti">공지</b></td>
							<td class="td_subject">
								<a href="<c:out value='${param.root }'/>/index.do?command=view&wr_id=<c:out value="${notice.wr_id}"/>&<c:out value="${writeSearchQueryString}" escapeXml="false"/>">
									<c:out value='${notice.wr_subject}' escapeXml="false"/>
								</a>
								<c:if test="${dm_hit_url ne null && not empty dm_hit_url}">
									<c:if test="${notice.wr_hit >= boardVO.dm_hit_max}">
										<img src="<c:out value='${dm_hit_url}'/>" alt="UP" class="list_util" />
									</c:if>
								</c:if>
								<c:if test="${dm_new_url ne '' && dm_new_url ne null}">
									<fmt:parseDate var="tmpWriteDate" value="${notice.wr_datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:parseDate var="tmpCurrentDate" value="${curDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<c:set var="writeDate" value="${tmpWriteDate.time + (1000*60*60*boardVO.dm_new_time)}"/>
									<c:set var="currentDate" value="${tmpCurrentDate.time}"/>
									<c:if test="${currentDate <= writeDate}">
										<img src="<c:out value='${dm_new_url}'/>" alt="NEW" class="list_util" />
									</c:if>
								</c:if>
								<c:if test="${dm_file_icon ne '' && dm_file_icon ne null}">
									<c:set var="fileArrCnt" value="0"/>
									<c:set var="fileArr" value="${fn:split(notice.wr_file,'|') }"/>
									<c:forEach var="result2" items="${fileArr}" varStatus="status">
										<c:if test="${result2 ne '' && result2 ne null}">
											<c:set var="fileArrCnt" value="${fileArrCnt + 1}"/>
										</c:if>
									</c:forEach>
									<c:if test="${fileArrCnt > 0}">
										<img src="<c:out value='${dm_file_icon}'/>" alt="FILE" class="list_util" />
									</c:if>		
								</c:if>
							</td>
							<td class="td_name">
								<c:choose>
									<c:when test="${boardVO.dm_writer_type eq 'name'}">
										<c:choose>
											<c:when test="${boardVO.dm_writer_secret eq '2' }">
												<c:out value="${fn:substring(notice.wr_name, 0, fn:length(notice.wr_name) - 1)}"/>*
											</c:when>
											<c:when test="${boardVO.dm_writer_secret eq '3' }">
												<c:out value="${fn:substring(notice.wr_name, 0, 1)}"/><c:forEach begin="0" end="${fn:length(notice.wr_name) - 2}">*</c:forEach>
											</c:when>
											<c:when test="${boardVO.dm_writer_secret eq '4' }">
												<c:out value="${fn:substring(notice.wr_name, 0 , fn:length(notice.wr_name) - 2)}"/>**
											</c:when>
											<c:otherwise>
												<c:out value="${notice.wr_name}"/>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${boardVO.dm_writer_secret eq '2' }">
												<c:out value="${fn:substring(notice.mb_id, 0, fn:length(notice.mb_id) - 1)}"/>*
											</c:when>
											<c:when test="${boardVO.dm_writer_secret eq '3' }">
												<c:out value="${fn:substring(notice.mb_id, 0, 1)}"/><c:forEach begin="0" end="${fn:length(notice.mb_id) - 2}">*</c:forEach>
											</c:when>
											<c:when test="${boardVO.dm_writer_secret eq '4' }">
												<c:out value="${fn:substring(notice.mb_id, 0 , fn:length(notice.mb_id) - 2)}"/>**
											</c:when>
											<c:otherwise>
												<c:out value="${notice.mb_id}"/>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<fmt:parseDate var="wr_datetime" value="${notice.wr_datetime}" pattern="yyyy-mm-dd"/>
							<td class="td_date"><fmt:formatDate value="${wr_datetime}" pattern="yyyy-mm-dd"/></td>
							<td class="td_hit"><c:out value="${notice.wr_hit}"/></td>
							<c:if test="${boardVO.dm_is_comment eq '1' }">
								<td class="td_ico"><c:out value="${notice.com_count}"/></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>
	<div class="bbs_gallist">
		<c:choose>
			<c:when test="${fn:length(writeList) > 0}">
				<ul class="gall row row-cols-2 row-cols-sm-2 row-cols-md-3 row-cols-lg-4">
					<c:forEach var="result" items="${writeList}" varStatus="status">
						<li class="cell col">
							<a href="<c:out value='${param.root }'/>/index.do?command=view&wr_id=<c:out value='${result.wr_id}'/>&<c:out value='${writeSearchQueryString}' escapeXml='false'/>">
								<div class="cell_thumb">
									<c:choose>
										<c:when test="${empty result.thumb }">
											<div class="thumb-wrap noimg" style="width: 100%; padding-top: ${ratio }%; background-image: url('/images/no_image.png');">
												<img src="/images/no_image.png" alt="no image">
											</div>
										</c:when>
										<c:otherwise>
											<div class="thumb-wrap" style="width: 100%; padding-top: ${ratio }%; background-image: url('<c:out value='${result.thumb}'/>');">
												<img src="<c:out value='${result.thumb}'/>" alt="<c:out value='${result.wr_subject}' escapeXml='false'/>" onerror="this.src='/images/no_image.png'">
											</div>
										</c:otherwise>
									</c:choose>
								</div>
								<!-- //.cell_thumb -->
								
								<div class="cell_subject"><c:out value="${result.wr_subject}" escapeXml='false'/></div>
								
								<div class="cell_icon mt10">
									<c:if test="${dm_hit_url ne null && not empty dm_hit_url}">
										<c:if test="${result.wr_hit >= boardVO.dm_hit_max}">
											<img src="<c:out value='${dm_hit_url}'/>" alt="UP" />
										</c:if>
									</c:if>
									<c:if test="${dm_new_url ne '' && dm_new_url ne null}">
										<fmt:parseDate var="tmpWriteDate" value="${result.wr_datetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
										<fmt:parseDate var="tmpCurrentDate" value="${curDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										<c:set var="writeDate" value="${tmpWriteDate.time + (1000*60*60*boardVO.dm_new_time)}"/>
										<c:set var="currentDate" value="${tmpCurrentDate.time}"/>
										<c:if test="${currentDate <= writeDate}">
											<img src="<c:out value='${dm_new_url}'/>" alt="NEW" />
										</c:if>
									</c:if>
									<c:if test="${dm_file_icon ne '' && dm_file_icon ne null}">
										<c:set var="fileArrCnt" value="0"/>
										<c:set var="fileArr" value="${fn:split(result.wr_file,'|') }"/>
										<c:forEach var="result2" items="${fileArr}" varStatus="status">
											<c:if test="${result2 ne '' && result2 ne null}">
												<c:set var="fileArrCnt" value="${fileArrCnt + 1}"/>
											</c:if>
										</c:forEach>
										<c:if test="${fileArrCnt > 0}">
											<img src="<c:out value='${dm_file_icon}'/>" alt="FILE" />
										</c:if>
									</c:if>
								</div>
							</a>
						</li>
					</c:forEach>
				</ul>
			</c:when>
			<c:otherwise>
				<p class="empty">게시글이 없습니다.</p>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="bbs_paging">
		<c:out value='${pagingStr}' escapeXml="false"/>
	</div>
	
	<div class="bbs_search">
		<fieldset>
			<h3>검색</h3>
			<form id="search_form" action="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${pageVO.dm_uid}'/>&command=list" method="post" >
				<input type="hidden" name="search_cate" value="<c:out value='${searchDmWriteVO.search_cate}' escapeXml='false'/>"/>
				<label for="search_type" class="sr-only">검색항목</label>
				<select id="search_type" name="search_type" class="sch_select">
					<option value="">전체</option>
					<option value="wr_subject"<c:if test='${searchDmWriteVO.search_type eq "wr_subject" }'>selected="selected"</c:if>>제목</option>
					<option value="wr_content"<c:if test='${searchDmWriteVO.search_type eq "wr_content" }'>selected="selected"</c:if>>내용</option>
				</select>
				<label for="search_value" class="sr-only">검색어</label>
				<input type="text" name="search_value" id="search_value" class="sch_input" value="<c:out value='${searchDmWriteVO.search_value}' escapeXml='false'/>" maxlength="20" autocomplete="off" placeholder="검색어를 입력해주세요"/>
				<button type="submit" class="sch_button"><span>검색</span></button>
			</form>
		</fieldset>
	</div>
	
	<div class="bbs_listbtn">
		<c:if test="${is_write eq true}">
			<a href="<c:out value='${param.root }'/>/index.do?command=write&<c:out value='${writeSearchQueryString}' escapeXml='false'/>" class="btn_write">작성하기</a>
		</c:if>
	</div>
</div>

<div class="board-decorate">
	<c:if test="${boardVO.dm_footer_content ne null && boardVO.dm_footer_content ne ''}">
		<c:out value="${boardVO.dm_footer_content}" escapeXml="false"/>
	</c:if>
</div>