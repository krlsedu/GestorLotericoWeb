<%-- 
    Document   : consulta
    Created on : 04/08/2016, 18:18:37
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.Consulta"%>
<%
    Consulta c = new Consulta(request);
    out.println(c.output);
%>