import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {
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

    public static boolean isAdmin(String email) {
        try {
            String query = "SELECT * FROM admin WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Admin getAdminDetails(String email) {
        try {
            String query = "SELECT * FROM admin WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                return new Admin(email, password, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
