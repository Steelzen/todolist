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
<div class="flex flex-col justify-center items-center my-10 mx-10">
    <h1 class="text-3xl flex-grow"><%= "TO-DO List" %>
    </h1>
    <h3 class="relative">By Taehyung, Kwon</h3>
</div>
<div class="flex flex-col flex-wrap items-center w-full">
        <div class="flex flex-row flex-wrap items-center justify-center p-4 ">
<h1 class="text-xl">Welcome, <%= session.getAttribute("username") %></h1>
    <form action="logout" method="post" class="ml-4">
        <button type="submit" class="shadow-md rounded bg-slate-400 text-white hover:bg-slate-700 w-16 h-8">Log out</button>
    </form>
        </div>
    <div class="w-full max-w-2xl px-4">
    <form action="tasks" method="post" class="flex mb-4">
        <input type="text" name="task" class="flex-grow w-96 float-none" placeholder="Task.." required minlength="3">
        <button type="submit" class="bg-slate-400 text-white w-16 hover:bg-slate-700">Add</button>
    </form>
    <ul id="task-list" class="w-full">
        <c:forEach var="row" items="${tasks}">
            <c:choose>
                <c:when test="${row.done}">
                    <li id="${row.id}" class="done flex items-center justify-between cursor-pointer hover:bg-slate-300 h-10 px-4"><p class="task flex-grow float-none text-lg">${row.task} </p> <span class="delete-task p-0 m-0 text-xl cursor-pointer hover:text-red-400">&#xD7;</span></li>
                </c:when>
                <c:otherwise>
                    <li id="${row.id}" class="flex items-center justify-between cursor-pointer hover:bg-slate-300 h-10 px-4"><p class="task flex-grow float-none text-lg">${row.task} </p> <span class="delete-task p-0 m-0 text-xl cursor-pointer hover:text-red-400">&#xD7;</span></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
    </div>
</div>
<script><%@include file="/WEB-INF/script.js"%></script>
</body>
</html>
