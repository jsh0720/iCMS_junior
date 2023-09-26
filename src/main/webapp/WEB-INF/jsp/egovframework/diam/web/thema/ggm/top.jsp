<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${memberConfigVO.dm_is_member eq '1'}">
<div id="hd_user">
	<ul class="container">
		<c:choose>
			<c:when test="${DiamLoginVO.id eq null}">
				<c:choose>
					<c:when test="${login_uid eq null}">
					<li><a href="javascript:alert('준비중입니다.');">로그인</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${login_uid}'/>">로그인</a></li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${member_uid eq null}">
						<li><a href="javascript:alert('준비중입니다.');">회원가입</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid}'/>&command=terms">회원가입</a></li>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<li><a href="<c:out value='${param.root}'/>/web/logout.do">로그아웃</a></li>
				<c:if test="${!DiamLoginVO.is_admin}">
					<c:choose>
						<c:when test="${member_uid eq null}">
							<li><a href="javascript:alert('준비중입니다.');">정보수정</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid}'/>&command=modify&dm_no=<c:out value='${DiamLoginVO.dm_no}'/>">정보수정</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
	 		</c:otherwise>
		</c:choose>
	</ul>
</div>
</c:if>

<div id="wrap">
	<header id="header">
		<div class="container">
			<h1>
				<c:choose>
					<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
						<a class="hd_logo_bk" href="<c:out value='${param.root }'/>/index.do">
							<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
						</a>
					</c:when>
					<c:otherwise>
						<a href="<c:out value='${param.root }'/>/index.do">
							<img src="<c:out value='${layout_path}'/>/images/logo.png" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>">
						</a>
					</c:otherwise>
				</c:choose>
			</h1>
			<nav id="gnb">
				<h2 id="gnb_heading" class="sr-only">Menu</h2>
				<c:choose>
					<c:when test="${fn:length(menuList) > 0 }">
						<ul id="gnb_1dul">
							<c:forEach var="menu" items="${menuList}" varStatus="status">
								<c:if test="${menu.dm_depth eq 2 && menu.dm_menu_hidden ne '1'}">
									<li class="li_menu_${menu.dm_menu_order}">
										<c:choose>
											<c:when test="${menu.dm_link_type eq '1' }">
												<a href="<c:out value='${param.root }/index.do?contentId=${menu.dm_id }'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" /></a>											
											</c:when>
											<c:otherwise>
												<a href="<c:out value='${menu.dm_url}'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" /></a>											
											</c:otherwise>
										</c:choose>
										<div class="gnb_2dul_con">
											<ul class="gnb_2dul">
												<c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
													<c:if test="${menu.dm_id eq subMenu.dm_parent_id}">
														<c:if test="${subMenu.dm_link_data eq pageVO.dm_uid }">
															<script>
																$(function(){
																	$(".li_menu_"+ "${menu.dm_menu_order}").addClass("active");
																});
															</script>
														</c:if>
														<li>
															<c:choose>
																<c:when test="${subMenu.dm_link_type eq '1' }">
																	<a href="<c:out value='${param.root }/index.do?contentId=${subMenu.dm_id }'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>																										
																</c:when>
																<c:otherwise>
																	<a href="<c:out value='${subMenu.dm_url}'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>											
																</c:otherwise>
															</c:choose>
														</li>
													</c:if>
												</c:forEach>
											</ul>
										</div>
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</c:when>
				</c:choose>
			</nav>
		</div>
		
		<button class="offcanvas-toggle" data-toggle="offcanvas" data-target="#off" onclick="void(0);"><span class="sr_only">전체메뉴</span></button>
	</header>

	<!-- class in -->
	<div id="off" class="">
		<div id="off_wrap">
			<div class="m_off">
				<h2 id="off_heading" class="">
					<c:choose>
						<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
							<a href="<c:out value='${param.root }'/>/index.do">
								<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" />
							</a> 
						</c:when>
						<c:otherwise>
							<img src="/images/no_logo.jpg" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/> 샘플 로고">
						</c:otherwise>
					</c:choose>
					
					<button type="button" class="offcanvas-toggle is-open" data-toggle="offcanvas" data-target="#off">
						<span class="sr-only">Toggle navigation</span>
						<span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</span>
					</button>
				</h2>
				<!-- //#off_heading -->
				
				<div id="off_container">
					<c:if test="${memberConfigVO.dm_is_member eq '1'}">
						<ul id="off_member">
							<c:choose>
								<c:when test="${DiamLoginVO.id eq null}">
									<c:choose>
										<c:when test="${login_uid eq null}">
											<li><a href="javascript:alert('준비중입니다.');">로그인</a></li>
										</c:when>
										<c:otherwise>
											<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${login_uid}'/>">로그인</a></li>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${member_uid eq null}">
											<li><a href="javascript:alert('준비중입니다.');">회원가입</a></li>
										</c:when>
										<c:otherwise>
											<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid}'/>&command=terms">회원가입</a></li>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:if test="${!DiamLoginVO.is_admin}">
										<c:choose>
											<c:when test="${member_uid eq null}">
												<li><a href="javascript:alert('준비중입니다.');">정보수정</a></li>
											</c:when>
											<c:otherwise>
												<li><a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${member_uid}'/>&command=modify&dm_no=<c:out value='${DiamLoginVO.dm_no}'/>">정보수정</a></li>
											</c:otherwise>
										</c:choose>
									</c:if>
									<li><a href="<c:out value='${param.root}'/>/web/logout.do">로그아웃</a></li>
								</c:otherwise>
							</c:choose>
						</ul>
					</c:if>
					<div id="off_search">
						<div class="unified-search">
							<label><input type="text" class="unified-search-keyword" placeholder="검색어를 입력하세요."></label>
							<button class="unified-search-submit">검색</button>
						</div>
					</div>
					<c:choose>
		                <c:when test="${fn:length(menuList) > 0 }">
		                    <ul id="off_1dul">
		                        <c:forEach var="menu" items="${menuList}" varStatus="status">
		                            <c:if test="${menu.dm_depth eq '2' && menu.dm_menu_hidden ne '1'}">
		                                <c:forEach var="check" items="${menuList}" varStatus="cnt">
		    
		                                    <c:if test="${menu.dm_id eq check.dm_parent_id && menu.dm_menu_hidden ne '1' }">
		                                        <c:if test="${menu.dm_id eq check.dm_parent_id}">
		                                            <c:set var="childCnt" value="${childCnt+1 }"></c:set>
		                                        </c:if>
		                                        <c:if test="${pageVO.dm_uid eq check.dm_link_data}">
		                                            <c:set var="active" value="${active+1}" />
		                                        </c:if>
		                                    </c:if>
		                                </c:forEach>
		                                <!-- off_1dli_active -->
		                                <li class="off_1dli <c:if test="${childCnt ne null }">off_1dli_family</c:if><c:if test="${active ne null }"> off_1dli_active</c:if>">
		    								<c:choose>
												<c:when test="${menu.dm_link_type eq '1' }">
				                                    <a href="<c:out value='${param.root }/index.do?contentId=${menu.dm_id }'/>" class="off_1da" target="<c:out value='${menu.dm_link_target}'/>">
				                                        <c:out value="${menu.dm_menu_text}" />
				                                    </a>																																																							
												</c:when>
												<c:otherwise>
													<a href="<c:out value='${menu.dm_url}'/>" class="off_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" /></a>											
												</c:otherwise>
											</c:choose>
		                                    <ul class="<c:if test="${childCnt ne null }">off_2dul</c:if>">
		                                        <c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
		                                            <c:if test="${menu.dm_id eq subMenu.dm_parent_id}">
		                                                <li class="off_2dli <c:if test='${subMenu.dm_link_data eq pageVO.dm_uid }'>off_2dli_active</c:if><c:if test='${fn:substring(subMenu.dm_url,0,4) eq "http"}'>ex</c:if>">
		                                                    <c:choose>
																<c:when test="${subMenu.dm_link_type eq '1' }">
				                                                    <a href="<c:out value='${param.root }/index.do?contentId=${subMenu.dm_id }'/>" class="off_2da" target="<c:out value='${subMenu.dm_link_target}'/>">
				                                                        <c:out value="${subMenu.dm_menu_text}" />
				                                                    </a>																																																	
																</c:when>
																<c:otherwise>
																	<a href="<c:out value='${subMenu.dm_url}'/>" class="off_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>											
																</c:otherwise>
															</c:choose>
		                                                    <ul class="off_3dul">
		                                                        <c:forEach var="childMenu" items="${menuList}" varStatus="subStatus">
		                                                            <c:if test="${subMenu.dm_id eq childMenu.dm_parent_id }">
		                                                                <li class="off_3dli <c:if test='${childMenu.dm_link_data eq pageVO.dm_uid }'>off_3dli_active</c:if>">
		                                                                    <c:choose>
																				<c:when test="${childMenu.dm_link_type eq '1' }">
				                                                                    <a href="<c:out value='${param.root }/index.do?contentId=${childMenu.dm_id }'/>"  class="off_3da">
				                                                                   		<c:out value="${childMenu.dm_menu_text }"/>
				                                                                    </a>
																				</c:when>
																				<c:otherwise>
																					<a href="<c:out value='${childMenu.dm_url}'/>" class="off_3da" target="<c:out value='${childMenu.dm_link_target}'/>"><c:out value="${childMenu.dm_menu_text}" /></a>											
																				</c:otherwise>
																			</c:choose>
		                                                                </li>
		                                                            </c:if>
		                                                        </c:forEach>
		                                                    </ul>
		                                                    
		                                                </li>
		                                            </c:if>
		                                        </c:forEach>
		                                        <!-- off_2dli_active -->
		                                    </ul>
		                                </li>
		                                <c:remove var="active" />
		                                <c:remove var="childCnt" />
		                            </c:if>
		                        </c:forEach>
		                    </ul>
		                </c:when>
		            </c:choose>
				</div>
			</div>
			<div class="pc_off">
				<button type="button" class="offcanvas-toggle is-open" data-toggle="offcanvas" data-target="#off">
					<span class="sr-only">Toggle navigation</span>
					<span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</span>
				</button>
				<div class="pc_off_wrap">
					<div class="pc_off_logo">
						<c:choose>
							<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
								<a href="<c:out value='${param.root }'/>/index.do">
									<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" />
								</a> 
							</c:when>
							<c:otherwise>
								<img src="/images/no_logo.jpg" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/> 샘플 로고">
							</c:otherwise>
						</c:choose>
					</div>
					<c:choose>
		                <c:when test="${fn:length(menuList) > 0 }">
		                    <ul class="pc_off_1dul">
		                        <c:forEach var="menu" items="${menuList}" varStatus="status">
		                            <c:if test="${menu.dm_depth eq '2' && menu.dm_menu_hidden ne '1'}">
		                                <c:forEach var="check" items="${menuList}" varStatus="cnt">
		                                    <c:if test="${menu.dm_id eq check.dm_parent_id && menu.dm_menu_hidden ne '1' }">
		                                        <c:if test="${menu.dm_id eq check.dm_parent_id}">
		                                            <c:set var="childCnt" value="${childCnt+1 }"></c:set>
		                                        </c:if>
		                                        <c:if test="${pageVO.dm_uid eq check.dm_link_data}">
		                                            <c:set var="active" value="${active+1}" />
		                                        </c:if>
		                                    </c:if>
		                                </c:forEach>
		                                <!-- off_1dli_active -->
		                                <li class="pc_off_1dli <c:if test="${childCnt ne null }">off_1dli_family</c:if><c:if test="${active ne null }"> off_1dli_active</c:if>">
		                                	<c:choose>
		                                		<c:when test="${menu.dm_link_type eq '1'}">
				                                    <a href="<c:out value='${param.root }/index.do?contentId=${menu.dm_id }'/>" class="pc_off_1da" target="<c:out value='${menu.dm_link_target}'/>">
				                                        <c:out value="${menu.dm_menu_text}" />
				                                    </a>		                                		
		                                		</c:when>
		                                		<c:otherwise>
				                                    <a href="<c:out value='${menu.dm_url }'/>" class="pc_off_1da" target="<c:out value='${menu.dm_link_target}'/>">
				                                        <c:out value="${menu.dm_menu_text}" />
				                                    </a>                                		
		                                		</c:otherwise>
		                                	</c:choose>
		                                    <ul class="<c:if test="${childCnt ne null }">off_2dul</c:if>">
		                                        <c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
		                                            <c:if test="${menu.dm_id eq subMenu.dm_parent_id}">
		                                                <li class="off_2dli <c:if test='${subMenu.dm_link_data eq pageVO.dm_uid }'>off_2dli_active</c:if><c:if test='${fn:substring(subMenu.dm_url,0,4) eq "http"}'>ex</c:if>">
		                                                    <c:choose>
																<c:when test="${subMenu.dm_link_type eq '1' }">
				                                                    <a href="<c:out value='${param.root }/index.do?contentId=${subMenu.dm_id }'/>" class="off_2da" target="<c:out value='${subMenu.dm_link_target}'/>">
				                                                        <c:out value="${subMenu.dm_menu_text}" />
				                                                    </a>																																																	
																</c:when>
																<c:otherwise>
																	<a href="<c:out value='${subMenu.dm_url}'/>" class="off_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>											
																</c:otherwise>
															</c:choose>
		                                                    <ul class="off_3dul">
		                                                        <c:forEach var="childMenu" items="${menuList}" varStatus="subStatus">
		                                                            <c:if test="${subMenu.dm_id eq childMenu.dm_parent_id }">
		                                                                <c:choose>
																			<c:when test="${childMenu.dm_link_type eq '1' }">
			                                                                    <a href="<c:out value='${param.root }/index.do?contentId=${childMenu.dm_id }'/>"  class="off_3da">
			                                                                   		<c:out value="${childMenu.dm_menu_text }"/>
			                                                                    </a>
																			</c:when>
																			<c:otherwise>
																				<a href="<c:out value='${childMenu.dm_url}'/>" class="off_3da" target="<c:out value='${childMenu.dm_link_target}'/>"><c:out value="${childMenu.dm_menu_text}" /></a>											
																			</c:otherwise>
																		</c:choose>
		                                                            </c:if>
		                                                        </c:forEach>
		                                                    </ul>
		                                                    
		                                                </li>
		                                            </c:if>
		                                        </c:forEach>
		                                        <!-- off_2dli_active -->
		                                    </ul>
		                                </li>
		                                <c:remove var="active" />
		                                <c:remove var="childCnt" />
		                            </c:if>
		                        </c:forEach>
		                    </ul>
		                </c:when>
		            </c:choose>
	            </div>
            	<div class="pc_off_copy">Copyright (c) <c:out value="${CONFIG_INFO.dm_site_name}" />. All Rights Reserved.</div>
	        </div><!--  //.pc_off -->
		</div><!-- #off_wrap -->
	</div><!-- #off -->

	<div id="pop">
		<div id="pop_wrap">
		</div>
	</div>
	<div id="vis">
		<div id="vis_wrap">
			<c:forEach items="${menuList}" var="item">
				<c:if test="${pageVO.dm_uid eq item.dm_link_data }">
					<c:if test="${item.dm_depth eq 2 }">
						<c:set var="name" value="0<c:out value='${item.dm_menu_order }'/>"/>				
					</c:if>
					<c:if test="${item.dm_depth eq 3 }">
						<c:forEach items="${menuList }" var="sub">
							<c:if test="${item.dm_parent_id eq sub.dm_id }">
								<c:set var="name" value="0<c:out value='${item.dm_menu_order }'/>"/>
							</c:if>
						</c:forEach>
					</c:if>
					<c:if test="${item.dm_depth eq 4 }">
						<c:forEach items="${menuList }" var="sub">
							<c:if test="${item.dm_parent_id  eq sub.dm_id}">
								<c:forEach items="${menuList }" var="subsub">
									<c:if test="${sub.dm_parent_id eq subsub.dm_id}">
										<c:set var="name" value="0<c:out value='${item.dm_menu_order }'/>"/>
									</c:if>
								</c:forEach>
							</c:if>
						</c:forEach>
					</c:if>
				</c:if>
			</c:forEach>
			<c:choose>
				<c:when test="${pageVO.dm_main_content ne '1'}">
					<!-- 메뉴별 비주얼 이미지 설정 -->
					<%-- <div class="titler" id="titler" style="background-image: url('/thema/basic/images/title/${empty imgUrl ? '00' : name}.jpg');"> --%>
					<div class="titler" id="titler" style="background-image: url('/thema/basic/images/title/00.jpg');">
					<c:forEach items="${menuList}" var="item">
						<c:if test="${item.dm_link_data eq pageVO.dm_uid}">
							<c:set var="exist" value="exist"/>
						</c:if>
					</c:forEach>
					<c:choose>
						<c:when test="${exist ne 'exist'}">
							<h2 class="leader"><c:out value="${pageVO.dm_page_name }"/></h2>
						</c:when>
						<c:otherwise>
							<h2 class="leader"><c:out value="${now_menu['2'].dm_menu_text }"/></h2>
						</c:otherwise>
					</c:choose>
					<!-- //.leader -->
					<div class="helper">
						<c:forEach items="${menuList}" var="item">
							<c:if test="${item.dm_link_data eq pageVO.dm_uid}">
								<c:out value="${item.dm_menu_desc}"/>
							</c:if>
						</c:forEach>
					</div>
					<!-- //.helper -->
						
					<c:if test="${exist eq 'exist' }">
						<div class="segment">
							<ul>
								<c:out value="${now_menu_navi }" escapeXml="false"/>
							</ul>
						</div>
					</c:if>
					<!-- //.segment -->
	           		<c:forEach items="${menuList}" var="item">
						<c:if test="${pageVO.dm_uid eq item.dm_link_data }">
	<%--  						<c:if test="${item.dm_depth eq 2 }">
								<c:set var="name" value="0${item.dm_menu_order }"/>
							</c:if> --%>
							<c:if test="${item.dm_depth eq 3 }">
								<c:forEach items="${menuList }" var="sub">
									<c:if test="${item.dm_parent_id eq sub.dm_id }">
										<c:set var="name" value="0${sub.dm_menu_order}0${item.dm_menu_order}"/>
									</c:if>
								</c:forEach>
							</c:if>
	 						<c:if test="${item.dm_depth eq 4 }">
								<c:forEach items="${menuList }" var="sub">
									<c:if test="${item.dm_parent_id  eq sub.dm_id}">
										<c:forEach items="${menuList }" var="subsub">
											<c:if test="${sub.dm_parent_id eq subsub.dm_id}">
												<c:set var="name" value="0${subsub.dm_menu_order }0${sub.dm_menu_order}0${item.dm_menu_order }"/>
											</c:if>
										</c:forEach>
									</c:if>
								</c:forEach>
							</c:if>
						</c:if>
					</c:forEach>
	              	<c:if test="${pageVO.dm_main_content ne '1'}">
						<c:if test="${empty name}">
							<c:set var="name" value="00"/>
						</c:if>
						<c:catch var="e">
							<c:import url="/thema/basic/images/title/${name}.jpg" var="imgUrl" />
						</c:catch>
					</c:if>
					<div class="titler-wrap">
	 					<c:choose>
							<c:when test="${empty imgUrl}">
								<div class="titler" id="titler" style="background-image: url('/thema/basic/images/title/00.jpg');">
							</c:when>
							<c:otherwise>
								<div class="titler" id="titler" style="background-image: url('/thema/basic/images/title/${name}.jpg');">
							</c:otherwise>
						</c:choose>
						</div>
					</div>
					<!-- //.titler-wrap -->
		            <c:if test="${exist eq 'exist' }">
						<div class="menutab">
				      		<div class="menutab_wrap">
								<div class="dep-wrap">
									<div class="home"><a href="<c:out value='${param.root }'/>/index.do"><span class="sr-only">홈</span><img src="${layout_path}/images/icon_home.png" alt="home" /></a></div>
									<c:forEach var="entry" items="${now_menu }" varStatus="status">
										<c:if test="${entry.key ne '1' && status.index < 3}">
											<fmt:parseNumber var="i" value="${entry.key }" type="number" />
											<c:set var="parseInt" value="${i-1 }"/>
											<div class="dep dep<c:out value='${parseInt}'/>">
												<h3><a href="javascript:void(0);"><c:out value="${entry.value.dm_menu_text}"/></a></h3>
												<ul>
													<c:forEach items="${menuList}" var="item">
														<c:if test="${item.dm_parent_id eq entry.value.dm_parent_id && item.dm_menu_hidden ne '1'}">
															<li class="<c:if test='${entry.value.dm_id eq item.dm_id }'>on</c:if>">
																<c:choose>
																	<c:when test="${item.dm_link_type eq '1' }">
																		<a href="<c:out value='${param.root }'/>/index.do?contentId=<c:out value='${item.dm_id}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text }"/></a>
																	</c:when>
																	<c:otherwise>
																		<a href="<c:out value='${item.dm_url}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text}" /></a>											
																	</c:otherwise>
																</c:choose>
															</li>
														</c:if>
													</c:forEach>
												</ul>
											</div>
										</c:if>
									</c:forEach>
								</div>
								<div class="menutab_icon">
                                  	<ul>
									  <li class="li_url" ><a href="javascript:void(0)" onclick="copyURL();" title="URL 공유"><img src="${layout_path}/images/icon_link.png" alt="URL 공유"></a></li>
									  <li class="li_print"><a href="javascript:void(0)" onclick="printPage();" title="프린트 하기"><img src="${layout_path}/images/icon_print.png" alt="프린트"></a></li>
									</ul>
                                </div>
							</div>
						</div>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${fn:length(mainVisualList) > 0}">
							<div class="swiper-container" id="mainSlider">
								<ul class="swiper-wrapper">
									<c:forEach var="result" items="${mainVisualList}" varStatus="status">
										<c:choose>
											<c:when test="${result.dm_visual_link ne null && not empty result.dm_visual_link}">
												<li class="swiper-slide" style="background-image: url(<c:url value='/resources/main/${result.dm_visual_name}' />);">
													<a href="<c:out value='${result.dm_visual_link}'/>" target="<c:out value='${result.dm_visual_link_type}'/>">
														<img src="<c:url value='/resources/main/${result.dm_visual_name}' />" alt="<c:out value='${result.dm_visual_alt}' />" >
													</a>
												</li>
											</c:when>
											<c:otherwise>
												<li class="swiper-slide" style="background-image: url(<c:url value='/resources/main/${result.dm_visual_name}' />);">
													<img src="<c:url value='/resources/main/${result.dm_visual_name}' />" alt="<c:out value='${result.dm_visual_alt}' />">
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</ul>
								<div class="swiper-button-next"></div>
								<div class="swiper-button-prev"></div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="no-img" style="background-image: url('<c:out value="${layout_path}"/>/images/no_visual.png');">
								<img src="<c:out value='${layout_path}'/>/images/no_visual.png" alt=""/>
							</div>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
<script>
	$(function() {
		var menus = $("#gnb_1dul").children("li");
		if (menus) {
			var len = menus.length / 2;
			var left = new Array();
			var right = new Array();
			
			$.each(menus, function(i, obj) {
				if (i < Math.ceil(len)) {
					left.push($(obj).prop("outerHTML"));
				} else {
					right.push($(obj).prop("outerHTML"));
				}
			});
			
			var leftDom = document.createElement("ul");
			var rightDom = document.createElement("ul");
			
			leftDom.setAttribute("id", "gnb_1dul_left");
			leftDom.innerHTML = left.join('');
			
			rightDom.setAttribute("id", "gnb_1dul_right");
			rightDom.innerHTML = right.join('');
			
			$("#gnb_1dul").after(rightDom);
			$("#gnb_1dul").after(leftDom);
			$("#gnb_1dul").remove();
		}
	});
</script>

<div id="main">
	<div id="container">
		<c:if test="${pageVO.dm_main_content ne '1'}">
			<div class="contentMenu">
				<h3>${pageVO.dm_page_name }</h3>
			</div>
			<div class="contentWrap">
				<div class="heading sr-only">
					<h2>${pageVO.dm_page_name }</h2>
					<p class="description"></p>
				</div>
				<div class="contentContainer">
		</c:if>


<%--<div class="overlay"></div>
 <c:if test="${pageVO.dm_main_content ne '1'}">
    <c:import url="/web/frame_top.sub.do" />
</c:if> --%>