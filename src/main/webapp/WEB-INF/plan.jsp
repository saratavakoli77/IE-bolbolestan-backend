<%@ page import="java.util.List" %>
<%@ page import="bolbolestan.course.DaysOfWeek" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="bolbolestan.weeklySchedule.WeeklyScheduleModel" %>
<%@ page import="bolbolestan.bolbolestanExceptions.StudentNotFoundException" %>
<%@ page import="bolbolestan.bolbolestanExceptions.OfferingNotFoundException" %>
<%@ page import="bolbolestan.middlewares.Authentication" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<%--  Created by IntelliJ IDEA.--%>
<%--  User: 98919--%>
<%--  Date: 3/16/2021--%>
<%--  Time: 8:59 PM--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%--&ndash;%&gt;--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Plan</title>
    <style>
        table{
            width: 100%;
            text-align: center;

        }
        table, th, td{
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>

<a href="${pageContext.request.contextPath}/">Home</a>
<li id="code">Student Id: ${Authentication.getAuthenticated().getStudentId()}</li>
<br>
<table>
    <tr>
        <th></th>
        <th>7:30-9:00</th>
        <th>9:00-10:30</th>
        <th>10:30-12:00</th>
        <th>14:00-15:30</th>
        <th>16:00-17:30</th>
    </tr>

    <%
        HashMap<String, Object> plan = new HashMap<>();
        try {
            plan = new WeeklyScheduleModel().getWeeklySchedulePlan(Authentication.getAuthenticated().getStudentId());
        } catch (StudentNotFoundException | OfferingNotFoundException e) {
            e.printStackTrace();
        }
        List<DaysOfWeek> daysOfWeeks = Arrays.asList(DaysOfWeek.values());
        List<String> hours = new WeeklyScheduleModel().classHours();
        for (DaysOfWeek dayOfWeekEnum: daysOfWeeks) {
            String dayOfWeek = dayOfWeekEnum.name();
    %>
    <tr>
        <td><%=dayOfWeek%></td>
        <td><%=((HashMap<String, String>) plan.get(dayOfWeek)).get(hours.get(0))%></td>
        <td><%=((HashMap<String, String>) plan.get(dayOfWeek)).get(hours.get(1))%></td>
        <td><%=((HashMap<String, String>) plan.get(dayOfWeek)).get(hours.get(2))%></td>
        <td><%=((HashMap<String, String>) plan.get(dayOfWeek)).get(hours.get(3))%></td>
        <td><%=((HashMap<String, String>) plan.get(dayOfWeek)).get(hours.get(4))%></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
