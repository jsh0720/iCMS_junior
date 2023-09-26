<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

		<c:if test="${pageVO.dm_main_content ne '1'}">
				</div>
			</div>
		</c:if>
	</div>
</div>

<div id="ask">
	<div id="ask_wrap">
	</div>
</div>

<div id="kit">
    <div id="kit_wrap">
    </div>
</div>

<div id="stv">
    <div id="stv_wrap">
    </div>
</div>

<div id="hub">
	<div id="hub_wrap">
	</div>
</div>

<footer class="section fp-auto-height" id="footer">
	<div class="container">
	    <ul>
			<c:if test="${CONFIG_INFO.dm_policy_status eq '1' }">
				<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${clause_uid }'/>&command=policy" data-clause="provision">이용약관</a></li>
			</c:if>
			<c:if test="${CONFIG_INFO.dm_private_status eq '1' }">
				<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${clause_uid }'/>&command=privacy" data-clause="privacy">개인정보처리방침</a></li>
			</c:if>
			<c:if test="${CONFIG_INFO.dm_reject_status eq '1' }">
				<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${clause_uid }'/>&command=reject" data-clause="rejection">이메일무단수집거부</a></li>
			</c:if>
		</ul>
	 	<div>
	     	<h1>
	         	<c:choose>
					<c:when test="${CONFIG_INFO.dm_bottom_logo ne null && not empty CONFIG_INFO.dm_bottom_logo}">
						<a class="ft_logo_bk" href="<c:out value='${param.root }'/>/index.do">
							<img src="<c:out value='${CONFIG_INFO.dm_bottom_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
						</a>
					</c:when>
					<c:otherwise>
						<a class="ft_logo_bk" href="<c:out value='${param.root }'/>/index.do">
							<img src="<c:out value='${layout_path}'/>/images/logo.png" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>">
						</a>
					</c:otherwise>
				</c:choose>
	     	</h1>
	     	<address>
		         <span>ADDRESS <c:out value="${CONFIG_INFO.dm_zip}" escapeXml="false"/> <em><c:out value="${CONFIG_INFO.dm_addr1}" escapeXml="false"/></em> <c:out value="${CONFIG_INFO.dm_addr2}" escapeXml="false"/> <em><c:out value="${CONFIG_INFO.dm_addr3}" escapeXml="false"/></em></span>
		         <span>TEL <c:out value="${CONFIG_INFO.dm_tel}" escapeXml="false"/></span>
	             <p>COPYRIGHT (C) <c:out value="${CONFIG_INFO.dm_site_name}" />. ALL RIGHTS RESERVED.</p>
	        </address>
	    </div>
	</div>
</footer>

<div class="dm-sitemap" id="dm-sitemap">
	<div class="dm-sitemap-header">
		<c:choose>
			<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
				<a class="hd_logo_bk" href="<c:out value='${param.root }'/>/index.do">
					<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
				</a>
				
			</c:when>
			<c:otherwise>
				<a class="hd_logo_bk" href="<c:out value='${param.root }'/>/index.do">
					<img src="<c:out value='${layout_path}'/>/images/logo.png" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" >
				</a>
			</c:otherwise>
		</c:choose>
		<button type="button" class="dm-sitemap-close"><span class="sr-only">닫기</span></button>
	</div>
	<div class="dm-sitemap-body">
		<c:choose>
			<c:when test="${fn:length(menuList) > 0 }">
				<ul>
					<c:forEach var="menu" items="${menuList}" varStatus="status">
						<c:if test="${menu.dm_depth eq 2 && menu.dm_menu_hidden ne '1'}">
							<li>
								<c:choose>
									<c:when test="${menu.dm_link_type eq '1' }">
										<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${menu.dm_id}'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" />
											<span></span>
											<span class="dm-sitemap-menu-circle"><i></i><i></i></span>
										</a>
									</c:when>
									<c:otherwise>
										<a href="<c:out value='${menu.dm_url}'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" />
											<span></span>
											<span class="dm-sitemap-menu-circle"><i></i><i></i></span>
										</a>
									</c:otherwise>
								</c:choose>
								<ul>
									<c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
										<c:if test="${menu.dm_id eq subMenu.dm_parent_id}">
											<li class="">
												<c:choose>
													<c:when test="${subMenu.dm_link_type eq '1' }">
														<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${subMenu.dm_id}'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>
													</c:when>
													<c:otherwise>
														<a href="<c:out value='${subMenu.dm_url }'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>
													</c:otherwise>
												</c:choose>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</c:when>
		</c:choose>
	</div>
	<div class="dm-sitemap-footer">
		Copyright (c) 2023, (주)디자인아이엠. All Rights Reserved.
	</div>
</div>
</body>
</html>