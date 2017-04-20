<%@ page import="java.io.*,java.util.*" %>
<%@page import="com.cth.gestorlotericoweb.Login"%>
<%@page import="com.cth.gestorlotericoweb.Home"%>
<%@page import="com.cth.gestorlotericoweb.parametros.Parametros"%>
<%    
    //Date date = new Date();
    //long tempoInativo = (date.getTime() - session.getLastAccessedTime())/1000;
    //if(tempoInativo>session.getMaxInactiveInterval()){
        Login h = new Login();
        h.setLogin(request,false);
        out.println(h.output);
        HttpSession sessionAt = request.getSession(false);
        sessionAt.invalidate();
    //}else{
        //Login h = new Login();
        //h.setLogin();    
        //out.println(h.output);
      //  response.sendRedirect("app");
    // }
%>