<%--
  Created by IntelliJ IDEA.
  User: 98919
  Date: 3/16/2021
  Time: 8:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 10%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/">Home</a>
<ul>
    <li id="std_id">Student Id: ${studentProfile["student"].id}</li>
    <li id="first_name">First Name: ${studentProfile["student"].name}</li>
    <li id="last_name">Last Name: ${studentProfile["student"].secondName}</li>
    <li id="birthdate">Birthdate: ${studentProfile["student"].birthDate}</li>
    <li id="gpa">GPA: ${studentProfile["gpa"]}</li>
    <li id="tpu">Total Passed Units: ${studentProfile["tpu"]}</li>
</ul>
<table>
    <tr>
        <th>Code</th>
        <th>Grade</th>
    </tr>
    <c:forEach var="offering" items="${studentProfile[\"courses\"]}">
        <tr>
            <td><c:out value="${offering.code}"/></td>
            <td><c:out value="${offering.grade}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
