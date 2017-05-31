
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isErrorPage="true" %>
<%
    request.getSession(false).invalidate();
%>
<strong>Desculpe</strong>, ocorreu um erro! :(<br><br>
<div class="alert alert-danger" role="alert" style="text-align: center">
<table class="table">
    <thead>
    <tr>
    </tr>
    </thead>
    <tbody>
        <tr class="danger">
            <th scope="row">Erro:</th>
            <td>${pageContext.exception}</td>
        </tr>
        <tr class="warning">
            <th scope="row">URI:</th>
            <td>${pageContext.errorData.requestURI}</td>
        </tr>
        <tr class="danger">
            <th scope="row"><b>Status code:</b></th>
            <td>${pageContext.errorData.statusCode}</td>
        </tr>
        <tr class="warning">
            <th scope="row"><b>Stack trace:</b></th>
            <td>
                <c:forEach var="trace"
                         items="${pageContext.exception.stackTrace}">
                        <p style="font-size: 8pt">${trace}</p>
                </c:forEach>
            </td>
        </tr>
    </tbody>
</table>
</div>