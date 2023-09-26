<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:choose>
	<c:when test="${!empty CONFIG_INFO.dm_url}">
		<c:choose>
			<c:when test="${fn:contains(CONFIG_INFO.dm_url, '/') }">
				<c:set value="/${fn:split(CONFIG_INFO.dm_url, '/')[1]}" var="root"/>
			</c:when>
			<c:otherwise>
				<c:set value="" var="root"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:set value="" var="root"/>
	</c:otherwise>
</c:choose>
<c:import url="/web/frame_header.do" />
<c:import url="/web/frame_top.do">
	<c:param name="root" value="${root}"/>
</c:import>
<c:if test="${!empty popupVO}">
	<c:forEach var="popup" items="${popupVO}" varStatus="status">
		<c:if test="${popup.dm_popup_img ne null && popup.dm_popup_img ne ''}">
			<script>openPopup("/popup.do?dm_id=<c:out value='${popup.dm_id}'/>&layout_folder=<c:out value='${layoutVO.dm_layout_folder}'/>",'',"<c:out value='${popup.dm_id}'/>","<c:out value='${popup.dm_popup_left}'/>","<c:out value='${popup.dm_popup_top}'/>","<c:out value='${popup.dm_popup_width}'/>","<c:out value='${popup.dm_popup_height}'/>","<c:out value='${popup.dm_popup_type}'/>",'false');</script>
		</c:if>
	</c:forEach>
</c:if>
<c:import url="/web/frame_body.do">
	<c:param name="root" value="${root}"/>
</c:import>
<c:import url="/web/frame_bottom.do">
	<c:param name="root" value="${root}"/>
</c:import>