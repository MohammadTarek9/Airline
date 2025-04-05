import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                String bookingId = resultSet.getString("bookingID");
                String passengerEmail = resultSet.getString("email");
                String seat_id = resultSet.getString("seat_id");
                Passenger passenger = UserModel.getPassengerDetails(passengerEmail);
                Flight flight = FlightsModel.getFlightDetails(flightNumber);
                String seatType = SeatModel.getSeatType(seat_id, flightNumber);
                Seat seat = new Seat(seat_id, false, seatType);
                Booking booking = new Booking(bookingId, flight, passenger, seat);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return bookings;


    }


    // public static ArrayList<Booking> findAllBookings() {
    //     ArrayList<Booking> bookings = new ArrayList<>();
    //     String query = "SELECT * FROM booking WHERE isConfirmed = 1";
    //     try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    //         ResultSet resultSet = preparedStatement.executeQuery();
    //         while (resultSet.next()) {
    //             String flightNumber = resultSet.getString("flightNumber");
    //             String bookingId = resultSet.getString("bookingID");
    //             String passengerEmail = resultSet.getString("email");
    //             String seat_id = resultSet.getString("seat_id");
    //             Passenger passenger = UserModel.getPassengerDetails(passengerEmail);
    //             Flight flight = FlightsModel.getFlightDetails(flightNumber);
    //             Seat seat = new Seat(seat_id, false, resultSet.getString("seatType")); 
    //             Booking booking = new Booking(bookingId, flight, passenger, seat);
    //             bookings.add(booking);
    //         }

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    //     return bookings;
    // }

    public static boolean deleteBooking(String bookingId) {
        String query = "DELETE FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingId);
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

    public static Seat getBookingSeat(String bookingId) {
        String query = "SELECT seat_id, flightNumber FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingId);
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

    public static Flight getBookingFlight(String bookingId) {
        String query = "SELECT flightNumber FROM booking WHERE bookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingId);
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
}
