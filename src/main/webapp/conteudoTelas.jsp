<%-- 
    Document   : conteudoTelas
    Created on : 11/09/2016, 17:25:14
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.ConteudoTelas"%>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    if(sessionAt != null && !sessionAt.isNew()) {
        out.println(ConteudoTelas.getHtmlTela(request));
    } else {
        Login h = new Login();
        h.setLogin(request,true);
        out.println(h.output);
//response.sendRedirect("/index.jsp");
        response.setStatus(401);
        sessionAt.invalidate();
    }
%>
