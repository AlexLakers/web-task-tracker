<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>The start page</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@include file="/WEB-INF/jsp/header.jsp" %>
</header>

<main>
    <div <%--style="color: blue"--%>>
    <span>
        This is task manager and bla-bla-bla
    </span>
    </div>
</main>
<footer>
    <%@include file="/WEB-INF/jsp/footer.jsp" %>
</footer>
</body>
</html>