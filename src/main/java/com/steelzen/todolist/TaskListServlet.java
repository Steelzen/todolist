package com.steelzen.todolist;

import java.io.*;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name="taskListServlet", urlPatterns = "/tasks")
public class TaskListServlet extends HttpServlet implements DataBaseEnv {
    private Connection con = null;
    private Statement stmt = null;
    private PreparedStatement preparedStatement = null;
    private HikariDataSource ds = DataSource.getDataSource();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        // get session to retrieve username for foreign key

        try {
            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TASKS (task_id INT PRIMARY KEY AUTO_INCREMENT, task VARCHAR(500) NOT NULL, username VARCHAR(50), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, done TINYINT(1), FOREIGN KEY (username) REFERENCES USERS(username))");

            // Select all data from table
            ResultSet rs = stmt.executeQuery("SELECT * FROM TASKS");

            resp.sendRedirect("Dashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String task = req.getParameter("task");

        try {
            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TASKS (task_id INT PRIMARY KEY AUTO_INCREMENT, task VARCHAR(500) NOT NULL, username VARCHAR(50), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, done TINYINT(1), FOREIGN KEY (username) REFERENCES USERS(username))");

            HttpSession session = req.getSession();
            String username = (String) session.getAttribute("username");

            // Insert data into TASKS table
           String insertQuery = "INSERT INTO TASKS (task, username, created_at, done) VALUES(?, ?, NOW(), 0)";

           preparedStatement = con.prepareStatement(insertQuery);

           preparedStatement.setString(1, task);
           preparedStatement.setString(2,username);

           preparedStatement.executeUpdate();

           resp.sendRedirect("Dashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
