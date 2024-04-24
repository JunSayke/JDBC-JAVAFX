package com.example.jdbcjavafx.sqlquery;

import com.example.jdbcjavafx.datas.UserData;
import com.example.jdbcjavafx.mysqlconnection.MySqlConnection;

import java.sql.*;

public class UserTable {
    private static UserTable userTable = null;
    public static UserTable getInstance() {
        if (userTable == null) {
            userTable = new UserTable();
        }
        return userTable;
    }

    private UserTable() {
        createTable();
    }

    public void createTable() {
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tbluseraccount (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL)";
            statement.execute(query);
            System.out.println("User table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserData loginUser(String username, String password) {
        UserData userData = null;
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM tbluseraccount WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next() && resultSet.getString("password").equals(password))
                userData = new UserData(resultSet.getInt("id"), resultSet.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userData;
    };

    public boolean registerUser(String username, String password) {
        boolean success = false;
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("INSERT INTO tbluseraccount (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rows = statement.executeUpdate();
            if (rows > 0)
                success = true;
            System.out.println("Rows inserted: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean deleteAccount(int userId) {
        boolean success = false;
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM tbluseraccount WHERE id = ?")) {
            preparedStatement.setInt(1, userId);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0)
                success = true;
            System.out.println("Rows deleted: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public UserData updateAccount(int id, String username, String password) {
        UserData userData = null;
        if (username != null || password != null) {
            try (Connection c = MySqlConnection.getConnection()) {
                StringBuilder queryBuilder = new StringBuilder("UPDATE tbluseraccount SET ");
                if (username != null) {
                    queryBuilder.append("username = ?, ");
                }
                if (password != null) {
                    queryBuilder.append("password = ?, ");
                }
                queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
                queryBuilder.append(" WHERE id = ?");
                PreparedStatement preparedStatement = c.prepareStatement(queryBuilder.toString());

                int parameterIndex = 1;
                if (username != null) {
                    preparedStatement.setString(parameterIndex++, username);
                }
                if (password != null) {
                    preparedStatement.setString(parameterIndex++, password);
                }
                preparedStatement.setInt(parameterIndex, id);

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    try (PreparedStatement selectStatement = c.prepareStatement("SELECT username FROM tbluseraccount WHERE id = ?")) {
                        selectStatement.setInt(1, id);
                        ResultSet resultSet = selectStatement.executeQuery();
                        if (resultSet.next()) {
                            userData = new UserData(id, resultSet.getString("username"));
                        }
                    }
                }
                System.out.println("Rows updated: " + rows);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userData;
    }
}
