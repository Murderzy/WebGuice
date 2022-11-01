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
<jsp:include page="/WEB-INF/menu.jsp"/>
<jsp:include page="<%=pageBody%>" />
<jsp:include page="/WEB-INF/footer.jsp" />
</body>
</html>