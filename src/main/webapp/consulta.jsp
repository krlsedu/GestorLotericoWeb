<%-- 
    Document   : consulta
    Created on : 04/08/2016, 18:18:37
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.Consulta"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    if(sessionAt != null && !sessionAt.isNew()) {
        Consulta c = new Consulta(request);
        out.println(c.output);        
    } else {
        response.sendRedirect("/index.jsp");
    }
%>