package org.example.exhibitly.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "exhibitly";
        String databaseUser = "exhibitlygroup";
        String databasePassword = "RPL123__";
        String databaseUrl = "jdbc:mysql://exhibitly.mysql.database.azure.com:3306/exhibitly?useSSL=true"; //////////not done yet

        try {
            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
