package org.example.mealwise.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection conn;

    public static Connection connect() {
        try {
            String url = System.getenv("MEALWISE_DB_URL");
            String user = System.getenv("MEALWISE_DB_USER");
            String pass = System.getenv("MEALWISE_DB_PASS");

            // if (url == null || user == null || pass == null) {
            //     throw new RuntimeException("Missing environment variables for DB connection.");
            // }

            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        return conn;
    }
}

