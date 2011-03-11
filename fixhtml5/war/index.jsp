<!doctype html>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <link type="text/css" rel="stylesheet" href="main.css">

    <title>fixhtml5</title>
    
    <script type="text/javascript" language="javascript" src="fixhtml5/fixhtml5.nocache.js"></script>
  </head>

  <body>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>

    <div class="onebar">
    <%
    UserService us = UserServiceFactory.getUserService();
    HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
    String thisurl = req.getRequestURL().toString();
    if (req.getQueryString() != null) {
      thisurl += "?" + req.getQueryString();
    }
    if (us.isUserLoggedIn()) {
      String url = us.createLogoutURL(thisurl);
      %><a href="<%= url %>">logout</a><%
    } else {
      String url = us.createLoginURL(thisurl);
      %><a href="<%= url %>">login</a><%
    }
    %>
    </div>
    <div id="wait" class="wait">Initializing. Please wait...</div>
  </body>
</html>
