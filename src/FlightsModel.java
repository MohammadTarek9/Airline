
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public static Flight getFlightDetails(String flightNumber) {
        String query = "SELECT * FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int capacity = resultSet.getInt("capacity");
                int bookedSeats = resultSet.getInt("bookedSeats");
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                String departureTime = resultSet.getString("departureTime");
                String arrivalTime = resultSet.getString("arrivalTime");
                double fare = resultSet.getDouble("baseFare");
                return new Flight(flightNumber, capacity, bookedSeats, source, destination, departureTime, arrivalTime, fare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean decrementBookedSeats(String flightNumber) {
        String query = "UPDATE flight SET bookedSeats = bookedSeats - 1 WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void incrementBookedSeats(String flightNumber) {
        String query = "UPDATE flight SET bookedSeats = bookedSeats + 1 WHERE flightNumber = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, flightNumber);
            statement.executeUpdate();
            System.out.println("Booked seats incremented for flight: " + flightNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfDelayed(String flightNumber) {
        String query = "SELECT isDelayed FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("isDelayed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDelayReason(String flightNumber) {
        String query = "SELECT delayReason FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("delayReason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDepartureTime (String flightNumber) {
        String query = "SELECT departureTime FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("departureTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getArrivalTime (String flightNumber) {
        String query = "SELECT arrivalTime FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("arrivalTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkIfCancelled (String flightNumber) {
        String query = "SELECT isCancelled FROM flight WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("isCancelled");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Flight> getAllAvailableFlights() {
        ArrayList<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flight WHERE isCancelled = 0";
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

    public static String addFlight(Flight flight) {
        String query = "INSERT INTO flight (flightNumber, capacity, bookedSeats, source, destination, departureTime, arrivalTime, baseFare) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flight.getFlightNumber());
            preparedStatement.setInt(2, flight.getCapacity());
            preparedStatement.setInt(3, flight.getBookedSeats());
            preparedStatement.setString(4, flight.getSource());
            preparedStatement.setString(5, flight.getDestination());
            preparedStatement.setString(6, flight.getDepartureTime());
            preparedStatement.setString(7, flight.getArrivalTime());
            preparedStatement.setDouble(8, flight.getBaseFare());
            preparedStatement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error adding flight";
    }

    public static void delayFlight(String flightNumber, String reason, String newDepartureTime, String newArrivalTime) {
        String query = "UPDATE flight SET isDelayed = 1, delayReason = ?, departureTime = ?, arrivalTime = ? WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reason);
            preparedStatement.setString(2, newDepartureTime);
            preparedStatement.setString(3, newArrivalTime);
            preparedStatement.setString(4, flightNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean cancelFlight(String flightNumber) {
        String query = "UPDATE flight SET isCancelled = 1 WHERE flightNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumber);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
