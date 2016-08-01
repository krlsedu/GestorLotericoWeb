<%@page import="com.cth.gestorlotericoweb.Home"%>

<%
    Home h = new Home(request.getParameter("it"));
    h.setHome();
    out.println(h.output);
%>
