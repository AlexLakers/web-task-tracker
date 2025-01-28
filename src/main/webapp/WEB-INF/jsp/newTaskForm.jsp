<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 24.01.2025
  Time: 22:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Creating a new Task</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@include file="header.jsp" %>
</header>
<main>
    <h2>New Task</h2>
    <c:if test="${not empty requestScope.validErrors}">
        <c:forEach var="validError" items="${requestScope.validErrors}">
            <span class=".error">${validError}</span><br>
        </c:forEach>
    </c:if>
    <form action="${pageContext.request.contextPath}/tasks/create" method="post">
        <label for="nameId">The task name:
            <input type="text" name="name" id="nameId" required>
        </label><br>
        <label for="performerId">The performer:
            <select name="performer" id="performerId" required>
                <c:forEach var="account" items="${applicationScope.accounts}">
                    <option value=${account.id}>${account.fullName}</option>
                </c:forEach>
            </select>
        </label><br>
        <label for="endDateId">The deadline:
            <input type="date" name="endDate" id="endDateId" required>
        </label><br>
        <label for="priorityId">The priority:
            <c:forEach var="priority" items="${applicationScope.priorities}">
                <input type="radio" name="priority" value="${priority}" id="priorityId">${priority}<br>
            </c:forEach>
        </label><br>
        <%--  <c:if test="${not empty applicationScope.update}">
              <label for="statusId">The status:<br>
                  <c:forEach var="status" items="${applicationScope.status}">
                      <input type="radio" name="status" value="${status}" id="statusId">${status}<br>
                  </c:forEach>
              </label><br>
          </c:if>--%>
        <label for="descriptionId">The description:
            <input type="text" name="description" id="descriptionId" required><br>
        </label><br>
        <button type="submit">Create</button>
    </form>
</main>
<footer>
    <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
