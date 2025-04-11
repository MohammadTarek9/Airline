import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

public class BookingsModel {
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

    public static ArrayList<Booking> getAllBookings(String email) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking WHERE email = ? and isConfirmed = 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flightNumber");
                int bookingId = resultSet.getInt("bookingID");
                String passengerEmail = resultSet.getString("email");
                String seat_id = resultSet.getString("seat_id");
                Passenger passenger = UserModel.getPassengerDetails(passengerEmail);
                Flight flight = FlightsModel.getFlightDetails(flightNumber);
                String seatType = SeatModel.getSeatType(seat_id, flightNumber);
                Seat seat = new Seat(seat_id, false, seatType);
                Booking booking = new Booking(bookingId, flight, passenger, seat, true);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return bookings;


    }

    public static boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking deleted successfully.");
                return true;
            } else {
                System.out.println("Booking not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int addBooking(String flight_number, Passenger passenger, String seat_id) {
        int bookingId = 0;
        String query = "INSERT INTO booking (flightNumber, email, isConfirmed, seat_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, flight_number);
            preparedStatement.setString(2, passenger.getEmail());
            preparedStatement.setBoolean(3, false);
            preparedStatement.setString(4, seat_id);
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    bookingId = generatedKeys.getInt(1);
                    System.out.println("Booking added successfully with ID: " + bookingId);
                    return bookingId;
                }
            }
            System.out.println("Failed to retrieve generated booking ID.");
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static Seat getBookingSeat(int bookingId) {
        String query = "SELECT seat_id, flightNumber FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String seat_id = resultSet.getString("seat_id");
                String flightNumber = resultSet.getString("flightNumber");
                String seatType = SeatModel.getSeatType(seat_id, flightNumber);
                return new Seat(seat_id, false, seatType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Flight getBookingFlight(int bookingId) {
        String query = "SELECT flightNumber FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String flightNumber = resultSet.getString("flightNumber");
                return FlightsModel.getFlightDetails(flightNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateBookingStatus(int bookingId, boolean status) {
        String query = "UPDATE booking SET isConfirmed = ? WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, status);
            preparedStatement.setInt(2, bookingId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteNotConfirmedBookings(String email) {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking WHERE email = ? and isConfirmed = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flightNumber");
                int bookingId = resultSet.getInt("bookingID");
                String seat_id = resultSet.getString("seat_id");
                SeatModel.updateSeatAvailability(flightNumber, seat_id, true);
                FlightsModel.decrementBookedSeats(flightNumber);
                BookingsModel.deleteBooking(bookingId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }
}
