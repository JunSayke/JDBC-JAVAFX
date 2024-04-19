package com.example.jdbcjavafx.mysqlconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySqlConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/dbubaldof3";
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

    public void createdTable() {
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(100) NOT NULL)";
            statement.execute(query);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ResultSet> readData(int id) {
        List<ResultSet> resultSets = new ArrayList<>();
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users";
            if (id > -1)
                query = "SELECT * FROM users WHERE id=" + id;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                resultSets.add(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return resultSets;
        }
    };

    public void updateData() {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("UPDATE users SET name=? WHERE id=? RETURNING *")) {
            String name = "Antonio";
            int id = 1;
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            int rows = preparedStatement.executeUpdate();
            System.out.println("Rows updated: " + rows + "\nResult set: " + preparedStatement.getResultSet());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData() {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("DELETE FROM users WHERE id=? RETURNING *")) {
            int id = 3;
            preparedStatement.setInt(1, id);
            int rows = preparedStatement.executeUpdate();
            System.out.println("Rows deleted: " + rows);
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("name: " + name +
                        "\nemail: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(String username, String password) {
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rows = statement.executeUpdate();
            System.out.println("Rows inserted: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
