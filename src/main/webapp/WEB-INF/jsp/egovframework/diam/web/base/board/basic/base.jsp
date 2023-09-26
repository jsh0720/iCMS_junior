<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="egovframework.diam.cmm.util.CommonUtil" %>
<%
	CommonUtil commonUtil = new CommonUtil();
	Map<String, Object> param = new HashMap<String, Object>();
	param.put("contentId", request.getParameter("contentId"));
	param.put("search_value", request.getParameter("search_value"));
	param.put("search_type", request.getParameter("search_type"));
	param.put("search_cate", request.getParameter("search_cate"));
	param.put("page", request.getParameter("page"));
	
	String writeSearchQueryString = commonUtil.convertParam(param);
	
	request.setAttribute("writeSearchQueryString", writeSearchQueryString);
%>