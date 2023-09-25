package com.steelzen.todolist;

import java.io.*;

import com.zaxxer.hikari.HikariDataSource;
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
    private HikariDataSource ds = DataSource.getDataSource();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        resp.setContentType("text/html");

        out.println("<html><body>");
        out.println("<h1>Create Account Test</h1>");

        try{
            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS todolist");
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS USERS (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE NOT NULL, hashed_password VARCHAR(128) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");

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
            con.close();
            stmt.close();
            preparedStatement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
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
            // Connect to MySQL server
            con = ds.getConnection();


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

            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            resp.sendRedirect("/tasks");

            // Close resources
            con.close();
            stmt.close();
            preparedStatement.close();
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
