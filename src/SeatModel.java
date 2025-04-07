import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeatModel {
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

    public static boolean updateSeatAvailability(String flightNumber, String seat_id, boolean available) {
        String query = "UPDATE seat SET available = ? WHERE flightNumber = ? AND seat_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, available);
            preparedStatement.setString(2, flightNumber);
            preparedStatement.setString(3, seat_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    public static ArrayList<Seat> getAllSeats(String flightNumber) {
        ArrayList<Seat> seats = new ArrayList<>();
        String query = "SELECT * FROM seat WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String seat_id = resultSet.getString("seat_id");
                boolean available = resultSet.getBoolean("available");
                String seatType = resultSet.getString("seatType");
                Seat seat = new Seat(seat_id, available, seatType);
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public static String getSeatType(String seat_id, String flightNumber) {
        String query = "SELECT seatType FROM seat WHERE seat_id = ? AND flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, seat_id);
            preparedStatement.setString(2, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("seatType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Seat getSeatDetails(String seat_id, String flightNumber) {
        String query = "SELECT * FROM seat WHERE seat_id = ? AND flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, seat_id);
            preparedStatement.setString(2, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean available = resultSet.getBoolean("available");
                String seatType = resultSet.getString("seatType");
                return new Seat(seat_id, available, seatType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
