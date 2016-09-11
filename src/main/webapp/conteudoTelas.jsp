<%-- 
    Document   : conteudoTelas
    Created on : 11/09/2016, 17:25:14
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.ConteudoTelas"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    if(sessionAt != null && !sessionAt.isNew()) {
        out.println(ConteudoTelas.getHtmlTela(request));        
    } else {
        response.sendRedirect("/index.jsp");
    }
%>
