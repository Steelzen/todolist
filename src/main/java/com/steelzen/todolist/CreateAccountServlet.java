package com.steelzen.todolist;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;


@WebServlet(name="createAccountServlet", urlPatterns = "/create-account")
public class CreateAccountServlet extends HttpServlet implements DataBaseEnv{
    private Connection con = null;
    private Statement stmt = null;
    private PreparedStatement preparedStatement = null;

//    private String adminUsername = "admin";
//    private String hashedAdminPassword = BCrypt.hashpw("Zkxksk88!!", BCrypt.gensalt());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");

        out.println("<html><body>");
        out.println("<h1>Create Account Test</h1>");

        try {
            // Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL server
            con = DriverManager.getConnection(mySqlUrl, rootUser, rootPassword);

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS todolist");
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE NOT NULL, hashed_password VARCHAR(128) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");

//            // Populate USERS table with Admin conditionally
//            ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM USERS");
//            if(rsCount.next() && rsCount.getInt(1) == 0) {
//                stmt.executeUpdate("INSERT INTO USERS (username, hashed_password, created_at) VALUES ('" + adminUsername + "', '" + hashedAdminPassword + "', NOW())");
//            };
//            rsCount.close();

            // Execute a query to fetch data
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS");

            // Generate the table
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Username</th><th>Password</th><th>Time</th></tr>");
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String hashed_password = rs.getString("hashed_password");
                Timestamp time = rs.getTimestamp("created_at");
                out.println("<tr><td>" + id + "</td><td>" + username + "</td><td>" + hashed_password + "</td><td>" + time +"</td></tr>");
            }
            out.println("</table>");

            // Close resources
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Got Post! (CreateAccount) " + req.getParameter("username"));
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("Password: " + password);

        // Hashing requested password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed Password: " + hashedPassword);

        try {
            // Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL server
            con = DriverManager.getConnection(mySqlUrl, rootUser, rootPassword);

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS todolist");
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE NOT NULL, hashed_password VARCHAR(128) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");

            // Store username and password to USERS table - Safe way to execute a query
            String insertQuery = "INSERT INTO USERS (username, hashed_password, created_at) VALUES (?, ?, NOW())";

            preparedStatement = con.prepareStatement(insertQuery);

            preparedStatement.setString(1,username);
            preparedStatement.setString(2, hashedPassword);

            preparedStatement.executeUpdate();

//            //logic for ajax
//            resp.setContentType("application/json");
//            PrintWriter out = resp.getWriter();
//            out.print("{\"status\":\"success\"}");
//            out.flush();

            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            req.getRequestDispatcher("Dashboard.jsp").forward(req, resp);

            // Close resources
            stmt.close();
            con.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            if(e.getErrorCode() == 1062) {
                HttpSession session = req.getSession();
                session.setAttribute("duplicateAccountMessage", "The username already exists");
                resp.sendRedirect("index.jsp");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
