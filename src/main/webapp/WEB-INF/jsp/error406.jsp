<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 19.11.2024
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>501</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" >
</head>
<body>
<header>
    <%@include file="header.jsp"%>
</header>

<main>
    <div>
        <span class=".error">
            If you are here thus '(406)Not Acceptable' was detected.Check your logs.
            And try to communicate with administrator.
        </span>
    </div>
</main>
<footer>
    <%@ include file="footer.jsp"%>
</footer>
</body>
</html>
