<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 24.01.2025
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Updating task</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@include file="header.jsp" %>
</header>
<main>
    <h2>The Updating task form</h2>
    <form action="${pageContext.request.contextPath}/tasks/update" method="post">
        <label for="idId">
            <input type="hidden" name="id" value="${param.id}" id="idId"/>
        </label>
        <label for="performerId">The performer:
            <select name="performer" id="performerId" >
                <option disabled selected value>empty</option>
                <c:forEach var="account" items="${applicationScope.accounts}">
                    <option value=${account.id}>${account.fullName}</option>
                </c:forEach>
            </select>
        </label><br>
        <label for="endDateId">The deadline:
            <input type="date" name="endDate" id="endDateId" >
        </label><br>
        <label for="priorityId">The priority:
            <c:forEach var="priority" items="${applicationScope.priorities}">
                <input type="radio" name="priority" value="${priority}" id="priorityId">${priority}<br>
            </c:forEach>
        </label><br>
        <label for="statusId">The status:<br>
            <c:forEach var="status" items="${applicationScope.status}">
                <input type="radio" name="status" value="${status}" id="statusId">${status}<br>
            </c:forEach>
        </label><br>
        <label for="descriptionId">The description:
            <input type="text" name="description" id="descriptionId" ><br>
        </label><br>
        <button type="submit">Update</button>
    </form>
</main>
<footer>
    <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
