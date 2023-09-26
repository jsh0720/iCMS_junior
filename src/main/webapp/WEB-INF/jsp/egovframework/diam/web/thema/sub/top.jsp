<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="layer_popup"></div>

<div class="page-bg"></div>
<header id="hd">
	<div id="header_top_banner"></div>
	<div id="hd_wrap">
		<div id="hd_logo">
			<c:choose>
				<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
					<a class="hd_logo_bk" href="<c:out value='${param.root }'/>/index.do">
						<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
					</a>
					<a class="hd_logo_wt" href="<c:out value='${param.root }'/>/index.do">
						<img src="<c:out value='${layout_path}'/>/images/logo_wt.png" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
					</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value='${param.root }'/>/index.do">
						<img src="/images/no_logo.jpg" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>">
					</a>
				</c:otherwise>
			</c:choose>
		</div>

		<div id="hd_home">
			<ul>
				<li>
					<a href="<c:out value='${param.root }'/>/index.do" class="home">
						<i class="material-icons">home</i>
					</a>
				</li>
				<li>
					<a href="#" class="bookmark">
						<i class="material-icons">bookmark</i>
					</a>
				</li>
			</ul>
		</div>
		
		<c:if test="${memberConfigVO.dm_is_member eq '1'}">
		<div id="hd_user">
			<ul>
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
		
		<div id="hd_contact">
			<ul>
				<c:choose>
					<c:when test="${CONFIG_INFO.dm_tel ne null}">
						<li>
							<a class="unit-group" href="tel:<c:out value='${CONFIG_INFO.dm_tel}'/>">
								<i class="unit-stamp fa fa-fw fa-phone" aria-hidden="true"></i>
								<span class="unit-label">Call</span>
								<span class="unit-field"><c:out value='${CONFIG_INFO.dm_tel}'/></span>
							</a>
						</li>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${CONFIG_INFO.dm_ceo_email ne null}">
						<li>
							<a class="unit-group" href="mailto:<c:out value='${CONFIG_INFO.dm_ceo_email}'/>">
								<i class="unit-stamp fa fa-fw fa-envelope-o" aria-hidden="true"></i>
								<span class="unit-label">Mail</span>
								<span class="unit-field"><c:out value='${CONFIG_INFO.dm_ceo_email}'/></span>
							</a>
						</li>
					</c:when>
				</c:choose>
			</ul>
		</div>

		<div id="hd_search">
			<div class="hd_search">
				<form onsubmit="return false" class="form-inline">
					<div class="form-group m-0">
						<label for="sch_stx" class="sr-only">검색어</label>
						<input type="text" name="stx" id="sch_stx" maxlength="20" class="form-control" placeholder="검색어를 입력하세요." />
					</div>
					<button type="submit" id="sch_submit" class="btn btn-outline-secondary ml-1" data-domain="<c:out value='${CONFIG_INFO.dm_domain_id}'/>">
						<i class="fa fa-fw fa-search" aria-hidden="true"></i>
						<span class="sr-only">검색</span>
					</button>
					<button type="button" id="sch_search" class="btn btn-outline-secondary ml-1">
						<i class="fa fa-fw fa-search" aria-hidden="true"></i>
						<span class="sr-only">열기</span>
					</button>
				</form>
			</div>
		</div>

		<div id="hd_gadget">
			<ul class="hd_gadget">
				<c:if test="${memberConfigVO.dm_is_member eq '1'}">
					<li class="hd_gadget_member">
						<button type="button" class="hd_gadget_btn hd_gadget_member_btn"><i class="di di-member"></i><span class="sr-only">회원</span></button>
						<ul class="hd_gadget_obj hd_gadget_member_obj">
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
									<a href="<c:out value='${param.root}'/>/web/logout.do">로그아웃</a>
								</c:otherwise>
							</c:choose>
						</ul>
					</li>
				</c:if>

                <li class="hd_gadget_search">
                    <button type="button" class="hd_gadget_btn hd_gadget_search_btn"><i class="di di-search02"></i><span class="sr-only">검색</span></button>
                    <div class="hd_gadget_obj hd_gadget_search_obj">
                        <div class="unified-search">
                            <label><input type="text" class="unified-search-keyword" placeholder="검색어를 입력하세요." /></label>
                            <button class="unified-search-submit" data-domain="<c:out value='${CONFIG_INFO.dm_domain_id}'/>"><i class="di di-search02"></i><span class="sr-only">검색</span></button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

        <div id="hd_mapper">
            <button type="button" data-toggle="sitemap">
                <span class="sr-only">사이트맵 열기</span>
            </button>
        </div>
        <div id="hd_toggle">
            <button type="button" class="offcanvas-toggle" data-toggle="offcanvas" data-target="#off">
                <span class="sr-only">Toggle navigation</span>
                <span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </span>
            </button>
        </div>
	</div>

	<div class="gnb-bg"></div>
	<div id="gnb">
		<div id="gnb_wrap">
			<h2 id="gnb_heading" class="sr-only">Menu</h2>
			<div id="gnb_container">
				<c:choose>
					<c:when test="${fn:length(menuList) > 0 }">
						<ul id="gnb_1dul">
							<c:forEach var="menu" items="${menuList}" varStatus="status">
								<c:if test="${menu.dm_depth eq 2 && menu.dm_menu_hidden ne '1'}">
									<li class="gnb_1dli">
										<c:choose>
											<c:when test="${menu.dm_link_type eq '1' }">
												<a href="<c:out value='${param.root }/index.do?contentId=${menu.dm_id }'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" /></a>											
											</c:when>
											<c:otherwise>
												<a href="<c:out value='${menu.dm_url}'/>" class="gnb_1da" target="<c:out value='${menu.dm_link_target}'/>"><c:out value="${menu.dm_menu_text}" /></a>											
											</c:otherwise>
										</c:choose>
										<ul class="gnb_2dul">
											<c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
												<c:if test="${menu.dm_id eq subMenu.dm_parent_id && subMenu.dm_menu_hidden ne '1'}">
													<li class="gnb_2dli">
														<c:choose>
															<c:when test="${subMenu.dm_link_type eq '1' }">
																<a href="<c:out value='${param.root }/index.do?contentId=${subMenu.dm_id }'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>																										
															</c:when>
															<c:otherwise>
																<a href="<c:out value='${subMenu.dm_url}'/>" class="gnb_2da" target="<c:out value='${subMenu.dm_link_target}'/>"><c:out value="${subMenu.dm_menu_text}" /></a>											
															</c:otherwise>
														</c:choose>
														<ul class="gnb_3dul">
														<c:forEach items="${menuList }" var="depth3">
															<c:if test="${subMenu.dm_id eq depth3.dm_parent_id && depth3.dm_menu_hidden ne '1'}">
																<li>
																	<c:choose>
																		<c:when test="${depth3.dm_link_type eq '1' }">
																			<a href="<c:out value='${param.root }/index.do?contentId=${depth3.dm_id }'/>" class="gnb_3da" target="${depth3.dm_link_target }"><c:out value="${depth3.dm_menu_text }"/></a>																																												
																		</c:when>
																		<c:otherwise>
																			<a href="<c:out value='${depth3.dm_url}'/>" class="gnb_3da" target="<c:out value='${depth3.dm_link_target}'/>"><c:out value="${depth3.dm_menu_text}" /></a>											
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
									</li>
								</c:if>
							</c:forEach>
						</ul>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</header>


<!-- class in -->
<div id="off" class="">
	<div id="off_wrap">
		<h2 id="off_heading" class="">
			<c:choose>
				<c:when test="${CONFIG_INFO.dm_top_logo ne null && not empty CONFIG_INFO.dm_top_logo}">
					<a href="<c:out value='${param.root }'/>/index.do">
						<img src="<c:out value='${CONFIG_INFO.dm_top_logo}'/>" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>" onerror="this.src='<c:out value='${layout_path}'/>/images/logo.png'"/>
					</a>
				</c:when>
				<c:otherwise>
					<img src="/images/no_logo.jpg" alt="<c:out value='${CONFIG_INFO.dm_site_name}'/>">
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
                                    <ul class="<c:if test='${childCnt ne null }'>off_2dul</c:if>">
                                        <c:forEach var="subMenu" items="${menuList}" varStatus="subStatus">
                                            <c:if test="${menu.dm_id eq subMenu.dm_parent_id && subMenu.dm_menu_hidden ne '1'}">
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
                                                            <c:if test="${subMenu.dm_id eq childMenu.dm_parent_id && childMenu.dm_menu_hidden ne '1'}">
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
</div>

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
		
		<!-- 메뉴별 비주얼 이미지 설정 -->
		<%-- <c:if test="${pageVO.dm_main_content ne '1'}">
			<c:if test="${empty name}">
				<c:set var="name" value="00"/>
			</c:if>
			<c:catch var="e">
				<c:import url="/thema/basic/images/title/${name}.jpg" var="imgUrl" />
			</c:catch>
		</c:if> --%>
		
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
					<div class="segment" style="display:block;">
						<ul>
							<c:out value="${now_menu_navi }" escapeXml="false"/>
						</ul>
					</div>
				</c:if>
				<!-- //.segment -->
				<c:if test="${exist eq 'exist' }">
					<div class="menutab">
						<div class="dep-wrap">
							<div class="home"><a href="<c:out value='${param.root }'/>/index.do"><span class="sr-only">홈</span></a></div>
							<c:forEach var="entry" items="${now_menu }" varStatus="status">
								<c:if test="${entry.key ne '1' && status.index < 3}">
									<fmt:parseNumber var="i" value="${entry.key }" type="number" />
									<c:set var="parseInt" value="${i-1 }"/>
									<div class="dep dep<c:out value='${parseInt}'/>">
										<div><h3><c:out value="${entry.value.dm_menu_text}"/><i class="di di-arr-bot"></i></h3></div>
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
					</div>
				</c:if>
				<!-- //.menutab -->
				<!-- //#titler -->
				</div>
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

<div id="main">
	<div id="container">
		<c:if test="${pageVO.dm_main_content ne '1'}">
			<div class="contentMenu">
				<c:forEach var="entry" items="${now_menu }" varStatus="status">
					<c:if test="${status.last }">
						<h3><c:out value="${entry.value.dm_menu_text}"/></h3>					
					</c:if>
				</c:forEach>
			</div>
			<div class="contentWrap">
				<c:if test="${exist eq 'exist' }">
					<div class="heading">
						<c:forEach var="entry" items="${now_menu }" varStatus="status">
							<c:if test="${status.last }">
								<h3><c:out value="${entry.value.dm_menu_text}"/></h3>
								<p class="description"><c:out value="${entry.value.dm_menu_desc }"/></p>
							</c:if>
						</c:forEach>
					</div>
					<c:if test="${fn:length(now_menu) > 3}">
						<div class="tablist">
							<c:forEach var="entry" items="${now_menu }" varStatus="status">
								<c:if test="${status.count eq 4 }">
									<ul class="tablist_3d">
										<c:forEach items="${menuList}" var="item">
											<c:if test="${item.dm_parent_id eq entry.value.dm_parent_id  && item.dm_menu_hidden ne '1'}">
												<li class="<c:if test="${entry.value.dm_id eq item.dm_id }">on</c:if>" >
													<c:choose>
														<c:when test="${item.dm_link_type eq '1' }">
															<a href="<c:out value='${param.root}'/>/index.do?contentId=<c:out value='${item.dm_id}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text }"/></a>
														</c:when>
														<c:otherwise>
															<a href="<c:out value='${item.dm_url}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text}" /></a>											
														</c:otherwise>
													</c:choose>
												</li>
											</c:if>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${status.count eq 5 }">
									<ul class="tablist_4d">
										<c:forEach items="${menuList}" var="item">
											<c:if test="${item.dm_parent_id eq entry.value.dm_parent_id  && item.dm_menu_hidden ne '1'}">
												<li class="<c:if test="${entry.value.dm_id eq item.dm_id }">on</c:if>" >
													<c:choose>
														<c:when test="${item.dm_link_type eq '1' }">
															<a href="<c:out value='${param.root}'/>/index.do?contentId=<c:out value='${item.dm_id}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text }"/></a>
														</c:when>
														<c:otherwise>
															<a href="<c:out value='${item.dm_url}'/>" target="<c:out value='${item.dm_link_target}'/>"><c:out value="${item.dm_menu_text}" /></a>											
														</c:otherwise>
													</c:choose>
												</li>
											</c:if>
										</c:forEach>
									</ul>
								</c:if>
							</c:forEach>
						</div>
					</c:if>
				</c:if>
				<div class="contentContainer">
		</c:if>
<script>
	$(function(){
		var target = $(".gnb_3dul");
		for (var i = 0; i < target.length; i++) {
			if ($(target[i]).children().length == 0) {
				$(target[i]).remove();
			}
		}
	});
</script>

<%--<div class="overlay"></div>
 <c:if test="${pageVO.dm_main_content ne '1'}">
    <c:import url="/web/frame_top.sub.do" />
</c:if> --%>