<%@page import="com.cth.gestorlotericoweb.Login"%>

<%
    Login h = new Login();
    h.setLogin();
    out.println(h.output);
%>