<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes">
<meta name="format-detection" content="telephone=no, address=no, email=no" />
<meta name="Author" content="<c:out value='${CONFIG_INFO.dm_site_name}' escapeXml="false"/> | <c:out value='${CONFIG_INFO.dm_url}'/>"/>
<meta name="Generator" content="iCMS1.0.0" />
<meta name="publisher" content="<c:out value='${CONFIG_INFO.dm_site_name}' />" />
<meta name="naver-site-verification" content="${CONFIG_INFO.dm_naver_site_verification }" />

<meta property="og:type" content="website" />
<meta property="og:title" content="<c:out value="${CONFIG_INFO.dm_title}" escapeXml="false"/>" />
<meta property="og:image" content="<c:out value="${CONFIG_INFO.dm_personal_image}" escapeXml="false"/>" />
<meta property="og:url" content="<c:out value="${CONFIG_INFO.dm_url}" escapeXml="false"/>" />
<meta property="og:description" content="<c:out value="${CONFIG_INFO.dm_meta_desc}" escapeXml="false"/>" />

<link rel="canonical" href="http://${CONFIG_INFO.getDm_url()}">
<c:set var="titleText" value="${CONFIG_INFO.dm_title }"/>
<c:if test='${pageVO.dm_main_content ne 1 }'><c:set var="titleText" value="${titleText} > ${pageVO.dm_page_name}"/></c:if>
<title><c:out value="${titleText}" escapeXml="false"/></title>
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons&display=block" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100;300;400;500;700;900&display=swap" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap">
<link rel="stylesheet" href="<c:url value='/fonts/Pretendard/pretendard.css' />">
<link rel="stylesheet" href="<c:url value='/fonts/Diam/diam.css' />">
<link rel="stylesheet" href="<c:url value='${layout_path}/css/animate.css' />">
<link rel="stylesheet" href="<c:url value='${layout_path}/css/spinkit.css' />">
<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/font-awesome-5.15.3/css/all.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/font-awesome-4.7.0/css/font-awesome.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/jquery-ui-1.13.2/jquery-ui.css'/>"/>
<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/bootstrap-4.6.0/css/bootstrap.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/swiper-5.4.5/swiper.min.css'/>"/>
<link rel="stylesheet" href="<c:url value='${layout_path}/css/common.css' />">
<link rel="stylesheet" href="<c:url value='${layout_path}/css/base.css' />">
<link rel="stylesheet" href="<c:url value='${layout_path}/css/diam.css' />">

<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery/code.jquery.com_jquery-3.7.0.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery-scrolla/js/jquery.scrolla.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/se2/js/HuskyEZCreator.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/jquery-ui-1.13.2/jquery-ui.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/bootstrap-4.6.0/js/popper.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/bootstrap-4.6.0/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/swiper-5.4.5/swiper.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/swiper-5.4.5/swiper-animation.umd.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/validator.js'/>"></script>

<script type="text/javascript" src="<c:url value='${layout_path}/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='${layout_path}/js/diam.js'/>"></script>
<script type="text/javascript" src="<c:url value='${layout_path}/js/main.js'/>"></script>
<script type="text/javascript" src="<c:url value='${layout_path}/js/page.js'/>"></script>


<c:choose>
	<c:when test="${pageVO.dm_page_type eq 'BOARD'}">
        <link rel="stylesheet" href="<c:url value='${layout_path}/css/bbs.css' />">
		<script type="text/javascript" src="<c:url value='${layout_path}/js/bbs.js' />"></script>
    </c:when>
    <c:when test="${pageVO.dm_page_type eq 'PAGE'}">
    	<c:choose>
    		<c:when test="${pageVO.dm_main_content eq '1'}">
		        <link rel="stylesheet" href="<c:url value='${layout_path}/css/main.css' />">
				<script type="text/javascript" src="<c:url value='${layout_path}/js/main.js' />"></script>
				<link rel="stylesheet" href="<c:url value='/js/egovframework/diam/slickslider/slick.css' />">
				<script type="text/javascript" src="<c:url value='/js/egovframework/diam/slickslider/slick.js' />"></script>
		    </c:when>
		    <c:otherwise>
		    	<link rel="stylesheet" href="<c:url value='${layout_path}/css/page.css' />">
				<script type="text/javascript" src="<c:url value='${layout_path}/js/page.js' />"></script>		
		    </c:otherwise>
    	</c:choose>
    </c:when>
    <c:otherwise>
    	<link rel="stylesheet" href="<c:url value='${layout_path}/css/page.css' />">
		<script type="text/javascript" src="<c:url value='${layout_path}/js/page.js' />"></script>
    </c:otherwise>
</c:choose>
<script type="text/javascript" src="<c:url value='/js/egovframework/diam/common.js'/>"></script>
<%-- <script type="text/javascript" src="<c:url value='${layout_path}/js/common.js' />"></script> --%>
</head>
<body>