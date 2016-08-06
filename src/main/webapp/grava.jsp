<%@page import="com.cth.gestorlotericoweb.Grava"%>
<%
    Grava grava = new Grava(request);
    out.print(grava.id);
%>
