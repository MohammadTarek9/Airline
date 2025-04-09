
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/airvista";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection = null;
    public static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            if(connection != null){
            System.out.println("Connection established");
            }
            else{
            System.out.println("Connection failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void storeUserData(String email, String password, String firstName, String lastName, String phone, int age) {
        String query = "INSERT INTO passenger (firstName, lastName, phone, email, password, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);
            preparedStatement.setInt(6, age);
            preparedStatement.executeUpdate();
            System.out.println("User data stored successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
    }

    public static boolean isEmailExists(String email) {
        String query = "SELECT * FROM passenger WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true; // Email exists
            } else {
                return false; // Email does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred while checking email
        } 
    }

    public static String getPassword(String email) {
        String query = "SELECT password FROM passenger WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Passenger getPassengerDetails(String email) {
        String query = "SELECT * FROM passenger WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phone = resultSet.getString("phone");
                int age = resultSet.getInt("age");
                String password = resultSet.getString("password"); 
                return new Passenger(firstName, lastName, phone, email, password, age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String changePassword(String email, String newPassword) {
        String query = "UPDATE passenger SET password = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setString(2, email);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating password.";
        }
    }

    public static String changeEmail(String oldEmail, String newEmail) {
        String query = "UPDATE passenger SET email = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newEmail);
            statement.setString(2, oldEmail);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating email.";
        }
    }

    public static String changePhone(String email, String newPhone) {
        String query = "UPDATE passenger SET phone = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPhone);
            statement.setString(2, email);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating phone number.";
        }
    }

    public static String changeAge(String email, int newAge) {
        String query = "UPDATE passenger SET age = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newAge);
            statement.setString(2, email);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating age.";
        }
    }
    
    
}
