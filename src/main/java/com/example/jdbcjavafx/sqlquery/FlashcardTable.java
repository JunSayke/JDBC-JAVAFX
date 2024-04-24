package com.example.jdbcjavafx.sqlquery;

import com.example.jdbcjavafx.SessionManager;
import com.example.jdbcjavafx.datas.FlashcardData;
import com.example.jdbcjavafx.mysqlconnection.MySqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardTable {
    private static FlashcardTable flashcardTable = null;
    public static FlashcardTable getInstance() {
        if (flashcardTable == null) {
            flashcardTable = new FlashcardTable();
        }
        return flashcardTable;
    }

    private FlashcardTable() {
        createTable();
    }

    public void createTable() {
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS tblflashcard (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "userid INT(11) NOT NULL," +
                    "front VARCHAR(255) NOT NULL," +
                    "back VARCHAR(255) NOT NULL)";
            statement.execute(query);
            System.out.println("Flashcard table created successfully.");
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    public int insertUserFlashcard(int userId, String front, String back) {
        int flashcardId = -1;
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("INSERT INTO tblflashcard (userid, front, back)  VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, front);
            statement.setString(3, back);

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                flashcardId = generatedKeys.getInt(1);

        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return flashcardId;
    }

    public List<FlashcardData> getUserFlashcards(int userId) {
        List<FlashcardData> flashcardDataList = new ArrayList<>();
        try (Connection c = MySqlConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM tblflashcard WHERE userid = " + userId;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                flashcardDataList.add(new FlashcardData(
                        resultSet.getInt("id"),
                        resultSet.getInt("userid"),
                        resultSet.getString("front"),
                        resultSet.getString("back")
                ));
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return flashcardDataList;
    }

    public FlashcardData updateFlashcard(int flashcardId, String front, String back) {
        FlashcardData flashcardData = null;
        try (Connection c = MySqlConnection.getConnection()) {
            c.setAutoCommit(false);

            StringBuilder queryBuilder = new StringBuilder("UPDATE tblflashcard SET ");
            if (front != null) {
                queryBuilder.append("front = ?, ");
            }
            if (back != null) {
                queryBuilder.append("back = ?, ");
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(" WHERE id = ?");
            PreparedStatement preparedStatement = c.prepareStatement(queryBuilder.toString());

            int parameterIndex = 1;
            if (front != null) {
                preparedStatement.setString(parameterIndex++, front);
            }
            if (back != null) {
                preparedStatement.setString(parameterIndex++, back);
            }
            preparedStatement.setInt(parameterIndex, flashcardId);

            int rows = preparedStatement.executeUpdate();

            if (rows > 0) {
                try (PreparedStatement selectStatement = c.prepareStatement("SELECT userid, front, back FROM tblflashcard WHERE id = ?")) {
                    selectStatement.setInt(1, flashcardId);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        flashcardData = new FlashcardData(
                                flashcardId,
                                resultSet.getInt("userid"),
                                resultSet.getString("front"),
                                resultSet.getString("back"));
                    }
                }
                c.commit();
            } else {
                c.rollback();
            }
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return flashcardData;
    }


    public boolean deleteFlashcard(int flashcardId) {
        boolean success = false;
        try (Connection c = MySqlConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("DELETE FROM tblflashcard WHERE id = ?")) {
            statement.setInt(1, flashcardId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0)
                success = true;
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return success;
    }
}
