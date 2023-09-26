<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%=request.getRequestURI()%>
<ul class="Tabs mt30">
	<li <% if (request.getRequestURI().contains("/accessor_env/")) { out.print(" class='on'"); } %> ><a onclick="location.href='/cms/console/ui/statistics/visitor_env_u.html'">운영체제 현황</a></li>
	<li <% if (request.getRequestURI().contains("/visitor_env_u/")) { out.print(" class='on'"); } %> ><a onclick="location.href='/cms/console/ui/statistics/visitor_env_u.html'">운영체제 현황</a></li>
   
</ul>
<script>var widthSize = $('.Contents').width();</script>