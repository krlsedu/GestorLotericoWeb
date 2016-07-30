<%@page import="com.cth.gestorlotericoweb.Home"%>

<%
     Home h = new Home();
     h.setHome();
     out.println(h.output);
%>