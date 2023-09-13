package com.steelzen.todolist;

import java.io.*;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name="taskListServlet", urlPatterns = "/dashboard")
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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TASKS (id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(128), username UNIQUE NOT NULL VARCHAR(50) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");

            // Select all data from table
            ResultSet rs = stmt.executeQuery("SELECT * FROM TASKS");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        try {
            // Connect to MySQL server
            con = ds.getConnection();

            // Check and Create Database and Table
            stmt = con.createStatement();
            stmt.executeUpdate("USE todolist");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TASKS (id INT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(128), username UNIQUE NOT NULL VARCHAR(50) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");

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
