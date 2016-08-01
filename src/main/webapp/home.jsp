<%@page import="com.cth.gestorlotericoweb.Home"%>

<%
    Home h = new Home(request.getParameter("name"));
    h.setHome();
    out.println(h.output);
%>
