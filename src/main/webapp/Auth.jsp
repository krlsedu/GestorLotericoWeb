
<%@page import="com.cth.gestorlotericoweb.Auth"%>
<% 
        Auth h = new Auth(request);
        h.auth();
        out.println(h.output);
%>