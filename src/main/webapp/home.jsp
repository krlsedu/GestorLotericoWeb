<%@page import="com.cth.gestorlotericoweb.parametros.Parametros"%>
<%@page import="com.cth.gestorlotericoweb.Home"%>
<%@page import="com.cth.gestorlotericoweb.parametros.Parametros"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    HttpSession sessionAt = request.getSession(false);
    request.setCharacterEncoding("UTF-8");
    if(sessionAt != null && !sessionAt.isNew()){
        Home h = new Home(request);
        h.setHome();
        out.println(h.output);
    }else{
        response.sendRedirect("index.jsp");
    }
%>
