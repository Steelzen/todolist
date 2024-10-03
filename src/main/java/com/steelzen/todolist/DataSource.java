package com.steelzen.todolist;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/** Singleton class to get datasource connection pool without closing resource */
public class DataSource implements DataBaseEnv {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        // Load the driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        config.setJdbcUrl(mySqlUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(150); // Set based on your app needs
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setLeakDetectionThreshold(2000);
        ds = new HikariDataSource(config);
    }

    private DataSource() {}

    public static HikariDataSource getDataSource() {
        return ds;
    }
}

