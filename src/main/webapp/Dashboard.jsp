<%--
  Created by IntelliJ IDEA.
  User: kwontaehyung
  Date: 06/09/2023
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style><%@include file="/WEB-INF/output.css"%></style>
    <script><%@include file="/WEB-INF/script.js"%></script>
</head>
<body>
<div class="flex justify-center my-10 mx-10">
    <h1 class="text-xl"><%= "TO-DO List" %>
    </h1>
</div>
<div class="flex flex-col flex-wrap items-center ">
<h1>Welcome, <%= session.getAttribute("username") %></h1>
<form action="logout" method="post">
    <button type="submit" class="shadow-md rounded bg-slate-400 text-white w-16 h-8">Log out</button>
</form>
</div>
</body>
</html>
