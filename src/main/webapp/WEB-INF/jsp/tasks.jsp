<%--
  Created by IntelliJ IDEA.
  User: alexlakers
  Date: 24.01.2025
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%! private static boolean isAdmin;%>--%>
<html>
<head>
  <title>Tasks</title>
  <link href="../../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
  <%@include file="header.jsp" %>
</header>

<main>
  <div>
    <form action="${pageContext.request.contextPath}/tasks" method="post">
      <label for="performerId">performer<br>
        <select name="performer" id="performerId">
          <option disabled selected value>empty</option>
          <c:forEach var="account" items="${applicationScope.accounts}">
            <option value=${account.id}>${account.fullName}</option>
          </c:forEach>
        </select>
      </label><br>
      <label for="creationDateId">The creation date of task:<br>
        <input type="date" name="creationDate" id="creationDateId">
      </label><br>
      <label for="deadLineDateId">The deadLine date of task:<br>
        <input type="date" name="deadLineDate" id="deadLineDateId">
      </label><br>
      <label for="priorityId">The task priority:<br>
        <c:forEach var="priority" items="${applicationScope.priorities}">
          <input type="radio" name=priority value="${priority}" id="priorityId">${priority}
          <br>
        </c:forEach>
      </label><br>
      <label for="statusId"> The task status:<br>
        <c:forEach var="status" items="${applicationScope.status}">
          <input type="radio" name="status" value="${status}" id="statusId">${status}<br>
        </c:forEach>
      </label><br>
      <label for="limitId">The limit of files:<br>
        <input type="text" name="limit" id="limitId">
      </label><br>
      <button type="submit">Find</button>
    </form>
    <hr>
    <a href="${pageContext.request.contextPath}tasks/create">
      <button>Create</button>
    </a>

  </div>


  <div>
    <c:if test="${not empty requestScope.tasks}">
      <table>
        <caption>The list of tasks</caption>
        <tr>
          <th>id</th>
          <th>name</th>
          <th>costumer</th>
          <th>performer</th>
          <th>creation date</th>
          <th>deadline date</th>
          <th>description</th>
          <th>priority</th>
          <th>status</th>
        </tr>

        <c:forEach var="task" items="${requestScope.tasks}">
          <tr>
            <td>"${task.id}"</td>
            <td>"${task.name}"</td>
            <td>"${task.costumer}"</td>
            <td>"${task.performer}"</td>
            <td>"${task.creationDate}"</td>
            <td>"${task.deadLineDate}"</td>
            <td>"${task.description}"</td>
            <td>"${task.priority}"</td>
            <td>"${task.status}"</td>

            <c:forEach var="role" items="${sessionScope.account.roles}">
              <c:if test="${role eq 'ADMIN'}">
                <c:set var="currentRole" value="ADMIN"/>
              </c:if>

            </c:forEach>

            <c:if test="${ pageScope.currentRole eq 'ADMIN'}">
            <td><a href="${pageContext.request.contextPath}tasks/delete?id=${task.id}">
              <button type="button">Delete</button>
            </a>
            <td><a href="${pageContext.request.contextPath}tasks/update?id=${task.id}">
              <button type="button">Update</button>
            </a>
              </c:if>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>
</main>
<footer>
  <%@ include file="footer.jsp" %>
</footer>
</body>
</html>
