CACHE MANIFEST
<%@page import="java.util.Date"%>
# <%=new java.util.Date() %>
<%
if (Math.random() < 0.2) {
  response.sendError(404);
  return;
}

response.setContentType("text/cache-manifest");

int sMaxAge = 5;
Date now = new Date();
response.setDateHeader("Date", now.getTime());

if (sMaxAge == 0) {
  response.setDateHeader("Expires", 0);
  response.setHeader("Pragma", "no-cache");
  response.setHeader("Cache-Control", "no-cache, must-revalidate");
} else {
  response.setDateHeader("Expires", now.getTime() + 1000L * sMaxAge);
  response.setHeader("Cache-Control", "public, s-maxage=" + sMaxAge);
}

%>

CACHE:
/Appcache.html
#/Appcache.html?gwt.codesvr=127.0.0.1:9997
/AppCacheInstaller.html
/appcache/appcache.nocache.js
/slow.jsp?a=1
/slow.jsp?a=2
/slow.jsp?a=3
/slow.jsp?a=4
/slow.jsp?a=5
