import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class FlightsModel {
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

    public static ArrayList<Flight> getAllFlights() {
        ArrayList<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flight";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flightNumber");
                int capacity = resultSet.getInt("capacity");
                int bookedSeats = resultSet.getInt("bookedSeats");
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                String departureTime = resultSet.getString("departureTime");
                String arrivalTime = resultSet.getString("arrivalTime");
                double fare = resultSet.getDouble("baseFare");
                Flight flight = new Flight(flightNumber, capacity, bookedSeats, source, destination, departureTime, arrivalTime, fare);
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
}
