<%@ page contentType="text/html;charset=UTF-8" %><%
    String pageBody = "/WEB-INF/" + request.getAttribute( "pageBody" ) ;
    String home = request.getContextPath() ;
%>
<!doctype html >
<html>
<head>
    <meta charset="UTF-8" />
    <title>JSP basics</title>
</head>
<body>
    <a href="<%=home%>/md5hash">MD5Hash</a>
    <a href="<%=home%>/sha1hash">SHA1Hash</a>
