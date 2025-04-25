
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingsModelTest {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/airvista";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private MockedStatic<UserModel> userModelStatic;
    private MockedStatic<FlightsModel> flightModelStatic;
    private MockedStatic<SeatModel> seatModelStatic;

    @BeforeAll
    void setUpAll() throws Exception {
        BookingsModel.connectToDatabase();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO passenger (email, firstName, lastName, phone, password, age) " +
                "VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, "jana@example.com");
                ps.setString(2, "Jana");
                ps.setString(3, "Hany");
                ps.setString(4, "0123456789");
                ps.setString(5, "pass123");
                ps.setInt(6, 21);
                ps.executeUpdate();
            }
            String[][] flights = {
                {"FL123","100","Cairo","London","2025-04-14 10:00:00","2025-04-14 16:00:00","200.0"},
                {"FL456","100","Cairo","London","2025-04-14 10:00:00","2025-04-14 16:00:00","150.0"},
                {"FL200","200","NYC","LA","2025-05-01 08:00:00","2025-05-01 11:00:00","300.0"},
                {"FL300","150","Miami","Paris","2025-06-01 09:00:00","2025-06-01 12:00:00","250.0"},
                {"FL400","120","Berlin","Rome","2025-07-10 07:30:00","2025-07-10 10:00:00","180.0"},
                {"FL500","180","Tokyo","Seoul","2025-08-20 14:00:00","2025-08-20 16:30:00","220.0"}
            };
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO flight (flightNumber, capacity, source, destination, departureTime, arrivalTime, baseFare) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)") ) {
                for (var f : flights) {
                    ps.setString(1, f[0]);
                    ps.setInt(2, Integer.parseInt(f[1]));
                    ps.setString(3, f[2]);
                    ps.setString(4, f[3]);
                    ps.setTimestamp(5, Timestamp.valueOf(f[4]));
                    ps.setTimestamp(6, Timestamp.valueOf(f[5]));
                    ps.setDouble(7, Double.parseDouble(f[6]));
                    ps.executeUpdate();
                }
            }

            // Seed seat
            String[][] seats = {
                {"FL123","1A"},{"FL456","2B"},{"FL200","3C"},{"FL200","4D"},
                {"FL300","5E"},{"FL400","6F"},{"FL400","7G"},{"FL500","8H"},{"FL500","9I"}
            };
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO seat (flightNumber, seat_id, available, seatType) VALUES (?, ?, ?, ?)")) {
                for (var s : seats) {
                    ps.setString(1, s[0]);
                    ps.setString(2, s[1]);
                    ps.setBoolean(3, true);
                    ps.setString(4, "Economy");
                    ps.executeUpdate();
                }
            }
        }

        // Mock static helpers
        userModelStatic = Mockito.mockStatic(UserModel.class);
        flightModelStatic = Mockito.mockStatic(FlightsModel.class);
        seatModelStatic = Mockito.mockStatic(SeatModel.class);
    }

    @BeforeEach
    void cleanBookingTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM booking");
        }
    }

    @AfterAll
    void cleanUp() throws SQLException {   
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM seat WHERE flightNumber IN (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, "FL123");
                ps.setString(2, "FL456");
                ps.setString(3, "FL200");
                ps.setString(4, "FL300");
                ps.setString(5, "FL400");
                ps.setString(6, "FL500");
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM flight WHERE flightNumber IN (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, "FL123");
                ps.setString(2, "FL456");
                ps.setString(3, "FL200");
                ps.setString(4, "FL300");
                ps.setString(5, "FL400");
                ps.setString(6, "FL500");
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM passenger WHERE email = ?")) {
                ps.setString(1, "jana@example.com");
                ps.executeUpdate();
            }
        }
        userModelStatic.close();
        flightModelStatic.close();
        seatModelStatic.close();
        BookingsModel.closeConnection();
    }

    @Test
    void testAddAndRetrieveBooking() throws Exception {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", 
        "jana@example.com", "pass123", 21);
        int bookingId = BookingsModel.addBooking("FL123", passenger, "1A");
        assertTrue(bookingId > 0, "Booking ID should be generated and positive");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                "SELECT flightNumber, email, isConfirmed, seat_id FROM booking WHERE bookingID = ?")) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Inserted booking should exist in DB");
                assertEquals("FL123", rs.getString("flightNumber"));
                assertEquals("jana@example.com", rs.getString("email"));
                assertFalse(rs.getBoolean("isConfirmed"));
                assertEquals("1A", rs.getString("seat_id"));
            }
        }
    }
    @Test
    void testGetBookingSeatAndFlight() {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", "jana@example.com", 
        "pass123", 21);
        int id = BookingsModel.addBooking("FL456", passenger, "2B");
        assertTrue(id > 0);
        seatModelStatic.when(() -> SeatModel.getSeatType("2B", "FL456")).thenReturn("Economy");
        flightModelStatic.when(() -> FlightsModel.getFlightDetails("FL456"))
                          .thenReturn(new Flight("FL456", 100, "Cairo", "London",
                           "2025-04-14T10:00:00", "2025-04-14T16:00:00", 150.0));

        Seat seat = BookingsModel.getBookingSeat(id);
        assertNotNull(seat);
        assertEquals("2B", seat.getSeat_id());
        assertEquals("Economy", seat.getSeatType());
        Flight flight = BookingsModel.getBookingFlight(id);
        assertNotNull(flight);
        assertEquals("FL456", flight.getFlightNumber());
    }

    @Test
    void testUpdateStatusAndGetAllBookings() {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", 
        "jana@example.com", "pass123", 21);
        int id1 = BookingsModel.addBooking("FL200", passenger, "3C");
        int id2 = BookingsModel.addBooking("FL200", passenger, "4D");
        assertTrue(id1 > 0 && id2 > 0);
        assertTrue(BookingsModel.updateBookingStatus(id1, true));

        userModelStatic.when(() -> UserModel.getPassengerDetails("jana@example.com")).thenReturn(passenger);
        flightModelStatic.when(() -> FlightsModel.getFlightDetails("FL200"))
                          .thenReturn(new Flight("FL200", 200, "NYC", "LA", 
                          "2025-05-01T08:00:00", "2025-05-01T11:00:00", 300.0));
        seatModelStatic.when(() -> SeatModel.getSeatType("3C", "FL200")).thenReturn("Business");

        ArrayList<Booking> list = BookingsModel.getAllBookings("jana@example.com");
        assertEquals(1, list.size());
        Booking b = list.get(0);
        assertEquals(id1, b.getBookingID());
        assertEquals("Business", b.getSeat().getSeatType());
    }
    @Test
    void testDeleteBooking() throws Exception {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", "jana@example.com", 
        "pass123", 21);
        int id = BookingsModel.addBooking("FL300", passenger, "5E");
        assertTrue(id > 0);

        assertTrue(BookingsModel.deleteBooking(id));
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM booking WHERE bookingID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testDeleteNotConfirmedBookings() throws Exception {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", 
        "jana@example.com", "pass123", 21);
        int id1 = BookingsModel.addBooking("FL400", passenger, "6F");
        int id2 = BookingsModel.addBooking("FL400", passenger, "7G");
        assertTrue(id1 > 0 && id2 > 0);

        seatModelStatic.when(() -> SeatModel.updateSeatAvailability("FL400", "6F", true))
                       .thenAnswer(inv -> null);
        seatModelStatic.when(() -> SeatModel.updateSeatAvailability("FL400", "7G", true))
                       .thenAnswer(inv -> null);
        flightModelStatic.when(() -> FlightsModel.decrementBookedSeats("FL400"))
                          .thenAnswer(inv -> null);

        BookingsModel.deleteNotConfirmedBookings("jana@example.com");
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM booking WHERE email = ? AND isConfirmed = 0")) {
            ps.setString(1, "jana@example.com");
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1));
            }
        }
    }

    @Test
    void testGetAllBookingsForFlightNumber() {
        Passenger passenger = new Passenger("Jana", "Hany", "0123456789", 
        "jana@example.com", "pass123", 21);
        int id1 = BookingsModel.addBooking("FL500", passenger, "8H");
        int id2 = BookingsModel.addBooking("FL500", passenger, "9I");
        assertTrue(id1 > 0 && id2 > 0);

        BookingsModel.updateBookingStatus(id2, true);

        userModelStatic.when(() -> UserModel.getPassengerDetails("jana@example.com")).thenReturn(passenger);
        flightModelStatic.when(() -> FlightsModel.getFlightDetails("FL500"))
                          .thenReturn(new Flight("FL500", 180, "Tokyo",
                           "Seoul", "2025-08-20T14:00:00", "2025-08-20T16:30:00", 220.0));
        seatModelStatic.when(() -> SeatModel.getSeatType("9I", "FL500")).thenReturn("FirstClass");

        ArrayList<Booking> list = BookingsModel.getAllBookingsForFlightNumber("FL500");
        assertEquals(1, list.size());
        assertEquals(id2, list.get(0).getBookingID());
    }
    
}
