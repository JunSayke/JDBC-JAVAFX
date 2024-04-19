package com.example.jdbcjavafx.mysqlconnection;

import com.example.jdbcjavafx.datas.UserData;

import java.sql.*;

public class MySqlConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/dbcsit228";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("DB Connected!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    private static MySqlConnection mySqlConnection = null;
    public static MySqlConnection getInstance() {
        if (mySqlConnection == null) {
            mySqlConnection = new MySqlConnection();
        }
        return mySqlConnection;
    }

    private MySqlConnection() {}

    public void createdTable() {
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";
            statement.execute(query);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserData loginUser(String username, String password) {
        UserData userData = null;
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users WHERE username='" + username + "'";
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
             PreparedStatement statement = c.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?) ")) {
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

    public boolean deleteAccount(String username) {
        boolean success = false;
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM users WHERE username=?")) {
            preparedStatement.setString(1, username);
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
                StringBuilder queryBuilder = new StringBuilder("UPDATE users SET ");
                if (username != null) {
                    queryBuilder.append("username=?, ");
                }
                if (password != null) {
                    queryBuilder.append("password=?, ");
                }
                queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
                queryBuilder.append(" WHERE id=?");
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
                    try (PreparedStatement selectStatement = c.prepareStatement("SELECT username FROM users WHERE id=?")) {
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
