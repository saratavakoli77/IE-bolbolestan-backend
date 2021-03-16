<%--
  Created by IntelliJ IDEA.
  User: 98919
  Date: 3/16/2021
  Time: 11:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Submit Failed</title>
    <style>
        h1 {
            color: rgb(207, 3, 3);
        }
    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/">Home</a>
<h1>
    Error:

</h1>
<br>
<h3>
    <c:forEach var="error" items="${errorList}">
        <tr>
            <td><c:out value="${error}"/></td>
        </tr>
    </c:forEach>

</h3>
</body>
</html>
