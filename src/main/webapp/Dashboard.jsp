<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: kwontaehyung
  Date: 06/09/2023
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style><%@include file="/WEB-INF/output.css"%></style>
</head>
<body>
<div class="flex justify-center my-10 mx-10">
    <h1 class="text-xl"><%= "TO-DO List" %>
    </h1>
</div>
<div class="flex flex-col flex-wrap items-center ">
<h1>Welcome, <%= session.getAttribute("username") %></h1>
    <form action="logout" method="post" class="mb-2">
        <button type="submit" class="shadow-md rounded bg-slate-400 text-white w-16 h-8">Log out</button>
    </form>
    <form action="tasks" method="post">
        <input type="text" name="task" placeholder="Enter task" required minlength="3">
        <button type="submit" class="shadow-md rounded bg-slate-400 text-white w-16 h-8">Enter</button>
    </form>
    <ul id="task-list">
        <c:forEach var="row" items="${tasks}">
            <c:choose>
                <c:when test="${row.done}">
                    <li id="${row.id}" class="done cursor-pointer hover:bg-slate-300"><span class="task">${row.task} time: ${row.time}</span> <span class="delete-task cursor-pointer hover:bg-violet-600 hover:text-white">&#xD7;</span></li>
                </c:when>
                <c:otherwise>
                    <li id="${row.id}" class="cursor-pointer hover:bg-slate-300"><span class="task">${row.task} time: ${row.time}</span> <span class="delete-task cursor-pointer hover:bg-violet-600 hover:text-white">&#xD7;</span></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</div>
<script><%@include file="/WEB-INF/script.js"%></script>
</body>
</html>
