<%
if(session.getAttribute("username") !=null) {
    response.sendRedirect("/tasks");
}
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>TO-DO List by Taehyung, Kwon</title>
    <style><%@include file="/WEB-INF/output.css"%></style>
    <script><%@include file="/WEB-INF/script.js"%></script>
</head>
<body class="flex items-center justify-center h-screen">
<div class="flex flex-col flex-wrap items-center w-96 rounded bg-indigo-100 shadow-lg p-2">
    <div class="flex justify-center my-10 mx-10">
        <h1 class="text-xl font-mono"><%= "TO-DO List" %>
        </h1>
    </div>
    <form action="login" method="post" class="flex flex-col flex-wrap items-center mb-5" onsubmit="handleLogin()">
        <input type="text" name="username" class="form-input rounded text-blue-600 mb-5" placeholder="Username" required minlength="3"/>
        <input type="password" name="password" class="form-input rounded text-blue-600 mb-5" placeholder="Password" required minlength="6"/>
        <% if(request.getAttribute("errorMessage") != null) { %>
        <p class="text-red-500 mb-3"><%= request.getAttribute("errorMessage") %></p>
        <% } %>
        <button type="submit" class="shadow-md rounded bg-slate-400 text-white w-16 h-8">Log In</button>
    </form>
    <button class="shadow-md rounded bg-slate-400 text-white w-32 h-8 flex items-center justify-center text-center mb-5" onclick="handleCreateAccount()">Create Account</button>
</div>
<!-- Modal for creating account-->
<div id="create-account-modal" class="fixed inset-0 flex items-center justify-center p-4 bg-black bg-opacity-50 hidden">
    <div class="bg-white p-8 rounded-lg w-96">
        <div class="flex flex-col flex-wrap items-center">
        <h2 class="text-xl mb-4 font-mono">Create Account</h2>
        </div>
        <form action="create-account" method="post" onsubmit="return handleCreateAccountSubmit()">
            <input id="create-account-username" class="w-full p-2 mb-4 border rounded" type="text" name="username" placeholder="Username" required minlength="3">
            <input id="create-account-password" class="w-full p-2 mb-4 border rounded" type="password" name="password" placeholder="Password" required minlength="6">
            <input id="create-account-passwordConfirm" class="w-full p-2 mb-4 border rounded" type="password" name="password-confirm" placeholder="Confirm password" required minlength="6">
            <button type="submit" class="bg-blue-500 text-white p-2 rounded hover:bg-blue-600">Submit</button>
            <button type="button" id="closeModalButton" class="ml-4 p-2 rounded hover:bg-gray-300">Cancel</button>
        </form>
    </div>
</div>
<%
    String duplicateAccountMessage = (String) session.getAttribute("duplicateAccountMessage");
    if (duplicateAccountMessage != null) {
        session.removeAttribute("duplicateAccountMessage"); // so it doesn't show up on subsequent requests
%>
<script type="text/javascript">
    window.onload = function() {
        alert("The username already exists. Please choose another one.");
    }
</script>
<%
    }
%>
</body>
</html>