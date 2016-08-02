<%@page import="com.cth.gestorlotericoweb.Home"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    Date date = new Date();
    long tempoInativo = (date.getTime() - session.getCreationTime())/1000;
    if(tempoInativo>session.getMaxInactiveInterval()){
        response.sendRedirect("/index.jsp");
    }
    Home h = new Home(request);
    h.setHome();
    out.println(h.output);
%>
