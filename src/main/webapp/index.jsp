<%@ page import="java.io.*,java.util.*" %>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%
    
    Date date = new Date();
    long tempoInativo = (date.getTime() - session.getCreationTime())/1000;
    if(tempoInativo>session.getMaxInactiveInterval()){
        Login h = new Login();
        h.setLogin();    
        out.println(h.output);
    }else{
        Login h = new Login();
        h.setLogin();    
        out.println(h.output);
        //response.sendRedirect("app");
    }
%>