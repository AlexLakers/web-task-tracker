<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 24.10.2024
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Result</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@include file="header.jsp" %>
</header>

<main>
    <div>
        <span>
            The operation ${requestScope.action} for task with id=${requestScope.taskId} is ${requestScope.result}
        </span>
        <a href="${header.Referer}">
            <button type="button">Back to previous</button>
        </a>
    </div>
</main>
<footer>
    <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
