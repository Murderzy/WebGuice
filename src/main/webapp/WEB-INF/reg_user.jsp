<%@ page import="org.example.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" %><%
  String home = request.getContextPath() ;
  String regError = (String) request.getAttribute( "regError" ) ;
  String regOk = (String) request.getAttribute( "regOk" ) ;
  String userLogin = (String)request.getAttribute("userLogin");
  String userName = (String)request.getAttribute("userName");

%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<!-- Bootstrap Bundle JS (jsDelivr CDN) -->
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>

<div class="container"><div class="row justify-content-center"><div class="col-md-5"><div class="card">
  <h2 class="card-title text-center">Register</h2>
  <% if( regError != null ) { %><h3 class="card-title text-center reg-error"><%=regError%></h3><% } %>
  <% if( regOk != null ) { %><h3 class="card-title text-center reg-ok"><%=regOk%></h3><% } %>
  <div class="card-body py-md-4">
    <form method="post" action="" enctype="multipart/form-data">
      <div class="form-group">
        <input type="text"     class="form-control" name="userLogin"       placeholder=<% if( userLogin != null ) { %><%=userLogin%><% } else {%>"Login"<%}%>  /><br/>
        <input type="text"     class="form-control" name="userName"        placeholder=<% if( userName != null ) { %><%=userName%><% } else {%>"Name Surname"<%}%> /><br/>
        <input type="password" class="form-control" name="userPassword"    placeholder="Password" /><br/>
        <input type="password" class="form-control" name="confirmPassword" placeholder="Confirm password" /><br/>
        <input type="file"     class="form-control" name="userAvatar" />
      </div>
      <br/>
      <div class="d-flex flex-row align-items-center justify-content-between">
        <button type="submit" class="btn btn-primary">Create Account</button>
      </div>
    </form>
  </div>
</div></div></div></div>
