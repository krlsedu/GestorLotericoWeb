<%@page import="com.cth.gestorlotericoweb.Grava"%>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    HttpSession sessionAt = request.getSession(false);
    Grava grava = new Grava(request);
    if(sessionAt != null && !sessionAt.isNew()) {
    	grava.exec();
        out.println(grava.id);
    } else {
        Login h = new Login(grava);
        out.println(h.output);
//response.sendRedirect("/index.jsp");
        response.setStatus(401);
        sessionAt.invalidate();
    }
%>