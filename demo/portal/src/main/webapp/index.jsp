<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
    pageEncoding="UTF-8"%>
<%
        String url = response.encodeRedirectURL("cms");
        response.sendRedirect(url);
%>
 