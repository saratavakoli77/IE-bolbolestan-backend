<%--
  Created by IntelliJ IDEA.
  User: ghazal
  Date: 15/03/2021
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Courses</title>
</head>
<body>
<ul>
    <li id="std_id">Student Id:<c:out value="${studentId}"/></li>
    <li>
        <a href="${pageContext.request.contextPath}/courses/">Select Courses</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/plan/">Submited plan</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/profile/">Profile</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/logout/">Log Out</a>
    </li>
</ul>
</body>
</html>
