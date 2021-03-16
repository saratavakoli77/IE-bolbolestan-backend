<%--
  Created by IntelliJ IDEA.
  User: ghazal
  Date: 15/03/2021
  Time: 19:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page import="bolbolestan.tools.DateParser" %>
<%@ page import="bolbolestan.offering.OfferingEntity" %>
<%@ page import="bolbolestan.tools.ListParser" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Courses</title>
    <style>
        .course_table {
            width: 100%;
            text-align: center;
        }
        .search_form {
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<li id="code">Student Id: <c:out value="${studentId}"/></li>
<li id="units">Total Selected Units: <c:out value="${units}"/></li>

<br>

<table>
    <tr>
        <th>Code</th>
        <th>Class Code</th>
        <th>Name</th>
        <th>Units</th>
        <th></th>
    </tr>

    <c:forEach var="offering" items="${weeklyScheduleOfferings}">
    <tr>
        <td><c:out value="${offering.code}"/></td>
        <td><c:out value="${offering.classCode}"/></td>
        <td><c:out value="${offering.name}"/></td>
        <td><c:out value="${offering.units}"/></td>
        <td>
            <form action="" method="POST" >
                <input id="form_action" type="hidden" name="action" value="remove">
                <input id="form_course_code" type="hidden" name="course_code" value="${offering.code}">
                <input id="form_class_code" type="hidden" name="class_code" value="${offering.classCode}">
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>

    </c:forEach>
</table>


<br>

<form action="" method="POST">
    <button type="submit" name="action" value="submit">Submit Plan</button>
    <button type="submit" name="action" value="reset">Reset</button>
</form>

<br>

<%--<form class="search_form" action="" method="POST">--%>
<%--    <label>Search:</label>--%>
<%--    <input type="text" name="search" value="">--%>
<%--    <button type="submit" name="action" value="search">Search</button>--%>
<%--    <button type="submit" name="action" value="clear">Clear Search</button>--%>
<%--</form>--%>



<%--<br>--%>

<table class="course_table">
    <tr>
        <th>Code</th>
        <th>Class Code</th>
        <th>Name</th>
        <th>Units</th>
        <th>Signed Up</th>
        <th>Capacity</th>
        <th>Type</th>
        <th>Days</th>
        <th>Time</th>
        <th>Exam Start</th>
        <th>Exam End</th>
        <th>Prerequisites</th>
        <th></th>
    </tr>
    <c:forEach var="offering" items="${offeringEntities}">
        <%
            OfferingEntity offeringEntity = (OfferingEntity) pageContext.getAttribute("offering");
        %>
    <tr>
        <td><c:out value="${offering.code}"/></td>
        <td><c:out value="${offering.classCode}"/></td>
        <td><c:out value="${offering.name}"/></td>
        <td><c:out value="${offering.units}"/></td>
        <td><c:out value="${offering.registered}"/></td>
        <td><c:out value="${offering.capacity}"/></td>
        <td><c:out value="${offering.type}"/></td>
        <td><%= ListParser.getStringFromListOfDaysOfWeek(offeringEntity.getClassTimeDays())%></td>
        <td><%= DateParser.getStringFromDates(offeringEntity.getClassTimeStart(), offeringEntity.getClassTimeEnd())%></td>
        <td><%= DateParser.getStringFromExamDate(offeringEntity.getExamTimeStart())%></td>
        <td><%= DateParser.getStringFromExamDate(offeringEntity.getExamTimeEnd())%></td>
        <td><%= ListParser.getStringFromList(offeringEntity.getPrerequisites())%></td>

        <td>
            <form action="" method="POST" >
                <input id="form_action" type="hidden" name="action" value="add">
                <input id="form_class_code" type="hidden" name="course_code" value=${offering.code}>
                <input id="form_class_code" type="hidden" name="class_code" value=${offering.classCode}>
                <button type="submit">Add</button>
            </form>
        </td>
    </tr>
    </c:forEach>
</table>
</body>
</html>