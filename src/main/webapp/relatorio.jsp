<%--
  Created by IntelliJ IDEA.
  User: CarlosEduardo
  Date: 07/05/2017
  Time: 17:51
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.cth.gestorlotericoweb.Relatorios"%>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    Relatorios relatorios = new Relatorios(request);
    if(sessionAt != null && !sessionAt.isNew()) {
        out.println(relatorios.output);
    } else {
        Login h = new Login(relatorios);
        out.println(h.output);
//response.sendRedirect("/index.jsp");
        response.setStatus(401);
        sessionAt.invalidate();
    }
%>
