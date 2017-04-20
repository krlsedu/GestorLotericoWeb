<%-- 
    Document   : consulta
    Created on : 04/08/2016, 18:18:37
    Author     : CarlosEduardo
--%>
<%@page import="com.cth.gestorlotericoweb.Consulta"%>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    if(sessionAt != null && !sessionAt.isNew()) {
        Consulta c = new Consulta(request);
        out.println(c.output);        
    } else {
        Login h = new Login();
        h.setLogin(request,true);
        out.println(h.output);
//response.sendRedirect("/index.jsp");
        response.setStatus(401);
        sessionAt.invalidate();
    }
%>