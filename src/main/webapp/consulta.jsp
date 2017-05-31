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
    Consulta consulta = new Consulta(request);
    if(sessionAt != null && !sessionAt.isNew()) {
        out.println(consulta.output);
    } else {
        Login h = new Login(consulta);
        out.println(h.output);
//response.sendRedirect("/index.jsp");
        response.setStatus(401);
        sessionAt.invalidate();
    }
%>