<%@page import="com.cth.gestorlotericoweb.banco.UpgradeDb"%>
<%
    UpgradeDb db = new UpgradeDb(request);    
    out.print(db.getOutput());
%>