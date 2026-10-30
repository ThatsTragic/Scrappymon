/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;

/**
 *
 * @author Mitchell
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseManager {
    private static final String DEFAULT_URL = "jdbc:derby:db/petgame;create=true";
    private static DatabaseManager instance;
    private Connection connection;
    private final String url;

    private DatabaseManager() {
        // lets the unit tests point at a separate database
        this.url = System.getProperty("petgame.db.url", DEFAULT_URL);
        createTables();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    private void createTables() {
        try (Statement st = getConnection().createStatement()) {
            createTable(st, "CREATE TABLE USERS ("
                    + "USERNAME VARCHAR(20) PRIMARY KEY, "
                    + "COINS INT NOT NULL, "
                    + "ACTIVE_PET VARCHAR(20))");
            createTable(st, "CREATE TABLE PETS ("
                    + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "OWNER VARCHAR(20) NOT NULL REFERENCES USERS(USERNAME), "
                    + "TYPE VARCHAR(10) NOT NULL, "
                    + "NAME VARCHAR(20) NOT NULL, "
                    + "HEALTH INT NOT NULL, MAX_HEALTH INT NOT NULL, "
                    + "ATTACK INT NOT NULL, "
                    + "SPECIAL INT NOT NULL, SPECIAL_MAX INT NOT NULL)");
            createTable(st, "CREATE TABLE ITEMS ("
                    + "OWNER VARCHAR(20) NOT NULL REFERENCES USERS(USERNAME), "
                    + "ITEM_NAME VARCHAR(20) NOT NULL, "
                    + "AMOUNT INT NOT NULL, "
                    + "PRIMARY KEY (OWNER, ITEM_NAME))");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not set up the database", e);
        }
    }

    // Runs a CREATE TABLE but ignores the error Derby gives if the table already exists. 
    private void createTable(Statement st, String sql) throws SQLException {
        try {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {   // X0Y32 = table already exists
                throw e;
            }
        }
    }
}
