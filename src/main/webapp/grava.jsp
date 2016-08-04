<%@page import="com.cth.gestorlotericoweb.Grava"%>
<%
    Grava grava = new Grava(request);
    response.sendRedirect("app?it="+request.getParameter("it")+"&id="+grava.id);
%>
