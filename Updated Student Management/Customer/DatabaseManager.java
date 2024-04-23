package Customer;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentsdb";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "qwerty123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
    }

    public static int getBalance(int userId) {
        int balance = 0;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT current_balance FROM bankcraft WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                balance = resultSet.getInt("current_balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public static void updateBalance(int userId, int newBalance) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE bankcraft SET current_balance = ? WHERE user_id = ?")) {
            preparedStatement.setInt(1, newBalance);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePin(int userId, int newPin) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE bankcraft SET pin = ? WHERE user_id = ?")) {
            preparedStatement.setInt(1, newPin);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertTransaction(int userId, String transactionType, int amount) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO transaction_history (user_id, transaction_type, amount, timestamp) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, transactionType);
                preparedStatement.setInt(3, amount);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getTransactionHistory(int userId) {
        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM transaction_history WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
