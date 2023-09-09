package com.steelzen.todolist;

public interface DataBaseEnv {
    static final String mySqlUrl = System.getenv("DATABASE_URL");
    static final String rootUser = "root";
    static final String rootPassword = System.getenv("MYSQL_ROOT_PASSWORD");
    static final String username = System.getenv("DATABASE_USER");
    static final String password = System.getenv("DATABASE_PASSWORD");
}
