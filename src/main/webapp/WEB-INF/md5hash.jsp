<% String userTextHash = (String)request.getSession().getAttribute("userTextHashmd"); %>
<form method="post" action="">
    <input type="text" name="text"/>
    <input type="submit" value="Hash"/>
</form>

<% if( userTextHash != null ) { %>
<p>TextHash - <%=userTextHash%></p>
<% } %>