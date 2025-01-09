<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 17.10.2024
  Time: 18:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<c:choose>
    <c:when test="${empty sessionScope.account}">
        <a href="${pageContext.request.contextPath}/login"> <button type="button">Login</button></a><br/>
        <a href="${pageContext.request.contextPath}/registration"><button type="button">Registration</button></a>
    </c:when>
    <c:when test="${not empty sessionScope.account}">
        <a href="${pageContext.request.contextPath}/logout"><button type="button">Logout</button></a>
        <a href="${pageContext.request.contextPath}/tasks"><button type="button">tasks</button></a>
        <a href="${pageContext.request.contextPath}/"><button type="button">main</button></a>
    </c:when>
</c:choose>
<hr>
</body>
</html>
