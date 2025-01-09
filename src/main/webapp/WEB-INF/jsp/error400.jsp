<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 25.10.2024
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>400</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" >
</head>
<body>
<header>
    <%@include file="header.jsp"%>
</header>

<main>
    <div>
        <span class=".error">
            If you are here thus '(400)Bad Request' was detected.Check your logs.
            And try to repeat your action again.
        </span>
    </div>
</main>
<footer>
    <%@ include file="footer.jsp"%>
</footer>
</body>
</html>
