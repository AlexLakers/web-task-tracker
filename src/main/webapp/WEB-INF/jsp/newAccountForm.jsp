<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 15.01.2025
  Time: 22:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@ include file="header.jsp" %>
</header>
<main>
    <h2>Registration form</h2>
    <c:if test="${not empty requestScope.validErrors}">
        <c:forEach var="validError" items="${requestScope.validErrors}">
            <span class=".error">${validError}</span><br>
        </c:forEach>
    </c:if>
    <form action="${pageContext.request.contextPath}/registration" method="post">

        <label for="firstNameId">firstname<br>
            <input type="text" name="firstName" id="firstNameId" required>
        </label><br>

        <label for="lastNameId">lastname<br>
            <input type="text" name="lastName" id="lastNameId" required>
        </label><br>

        <label for="emailId">email<br>
            <input type="email" name="email" id="emailId" required>
        </label><br>

        <label for="rawPasswordId">password<br>
            <input type="text" name="rawPassword" id="rawPasswordId" required>
        </label><br>

        <label for="birthdayId">
            <input type="date" name="birthday" id="birthdayId" required>
        </label><br>
        <button type="submit">Registration</button>
    </form>
</main>
<footer>
    <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
