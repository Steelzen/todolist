package com.steelzen.todolist;

import java.io.*;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name="taskListServlet", urlPatterns = {"/tasks", "/tasks/*"})
public class TaskListServlet extends HttpServlet implements DataBaseEnv {
    private Connection con = null;
    private Statement stmt = null;
    private PreparedStatement preparedStatement = null;
    private HikariDataSource ds = DataSource.getDataSource();
    private HttpSession session;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        // get session to retrieve username for foreign key

        try {
            // Get username from session
            session = req.getSession();
            String username = (String) session.getAttribute("username");

            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TASKS (task_id INT PRIMARY KEY AUTO_INCREMENT, task VARCHAR(500) NOT NULL, username VARCHAR(50), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, done TINYINT(1), FOREIGN KEY (username) REFERENCES USERS(username))");

            // Select all data from table
            String query = "SELECT * FROM TASKS WHERE username = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            // Store each rows and pass to jsp
            List<Map<String, Object>> rows = new ArrayList<>();

            while(rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getObject("task_id"));
                row.put("task", rs.getObject("task"));
                row.put("time", rs.getObject("created_at"));
                row.put("done", rs.getObject("done"));

                rows.add(row);
            }
            req.setAttribute("tasks", rows);
            req.getRequestDispatcher("Dashboard.jsp").forward(req, resp);

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

            session = req.getSession();
            String username = (String) session.getAttribute("username");

            // Insert data into TASKS table
           String insertQuery = "INSERT INTO TASKS (task, username, created_at, done) VALUES(?, ?, NOW(), 0)";

           preparedStatement = con.prepareStatement(insertQuery);

           preparedStatement.setString(1, task);
           preparedStatement.setString(2,username);

           preparedStatement.executeUpdate();

           resp.sendRedirect("/tasks");

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
        PrintWriter out = resp.getWriter();

        int delTaskId = Integer.parseInt(req.getParameter("del"));

        try {
            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");

            String deleteQuery = "DELETE FROM TASKS WHERE task_id = ?";
            preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1,delTaskId);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Task with ID " + delTaskId + " has been deleted successfully");
            } else {
                System.out.println("No task found with ID " + delTaskId + ".");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }
}
