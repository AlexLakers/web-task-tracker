<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 19.10.2024
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>401</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" >
</head>
<body>
<header>
    <%@include file="header.jsp"%>
</header>

<main>
    <div>
        <span class=".error">
            If you are here thus '(401)Unauthorized Error' was detected.Check your logs.
        </span>
    </div>
</main>
<footer>
    <%@ include file="footer.jsp"%>
</footer>
</body>
</html>
