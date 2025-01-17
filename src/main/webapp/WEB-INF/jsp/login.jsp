<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 15.01.2025
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>The login</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@ include file="header.jsp" %>
</header>
<main>
    <h2>Login form</h2>
    <form action="${pageContext.request.contextPath}login" method="post">

        <label for="emailId">
            <input type="email" name="email" id="emailId" value="${requestScope.enteredLogin}">
        </label><br>

        <label for="passwordId">
            <input type="password" name="password" id="passwordId">
        </label><br>

        <button type="submit">Login</button>
    </form>
</main>
<footer>
    <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
