<%-- 
    Document   : consulta
    Created on : 04/08/2016, 18:18:37
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.OCR"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    OCR c = new OCR(request);
        out.println(c.output);   
    /*if(sessionAt != null && !sessionAt.isNew()) {
             
    } else {
        response.sendRedirect("/index.jsp");
    }*/
%>