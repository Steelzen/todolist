package com.steelzen.todolist;

import java.io.*;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet implements DataBaseEnv {
    private Connection con = null;
    private Statement stmt = null;
    private HikariDataSource ds = DataSource.getDataSource();

    private boolean userMatched (ResultSet users, String enteredUsername, String enteredPassword) {
        try {
            while (users.next()) {
                String retrievedUsername = users.getString("username");
                String retrievedPassword = users.getString("hashed_password");

                if (enteredUsername.equals(retrievedUsername) && BCrypt.checkpw(enteredPassword, retrievedPassword)) {
                    System.out.println("Match!");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "Login" + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String inputUsername = req.getParameter("username");
        String inputPassword = req.getParameter("password");

        try {
            // Connect to MySQL server
            con = ds.getConnection();

            // Create statement
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");

            // Get the result fromm USERS table
            ResultSet users = stmt.executeQuery("SELECT * FROM USERS");
            Boolean userMatch = userMatched(users, inputUsername, inputPassword);

            if(userMatch) {
                // Login success logic
                System.out.println("Login Success!");
                HttpSession session = req.getSession();
                session.setAttribute("username", inputUsername);
                resp.sendRedirect("Dashboard.jsp");

            } else {
                System.out.println("Login Fail!");
                req.setAttribute("errorMessage", "Username or password is not matched, try again.");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                resp.sendRedirect("index.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
